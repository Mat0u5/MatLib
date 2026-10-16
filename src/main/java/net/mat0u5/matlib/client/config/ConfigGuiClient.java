package net.mat0u5.matlib.client.config;

import com.google.auto.service.AutoService;
import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.client.gui.config.ConfigScreen;
import net.mat0u5.matlib.client.gui.config.entries.ConfigEntry;
import net.mat0u5.matlib.client.gui.config.entries.GroupConfigEntry;
import net.mat0u5.matlib.client.gui.config.entries.NumberConfigEntry;
import net.mat0u5.matlib.client.gui.config.entries.extra.*;
import net.mat0u5.matlib.client.gui.config.entries.extra.time.MinutesConfigEntry;
import net.mat0u5.matlib.client.gui.config.entries.extra.time.SecondsConfigEntry;
import net.mat0u5.matlib.client.gui.config.entries.interfaces.IEntryGroupHeader;
import net.mat0u5.matlib.client.gui.config.entries.main.*;
import net.mat0u5.matlib.client.render.RenderUtils;
import net.mat0u5.matlib.client.services.RegistrableClient;
import net.mat0u5.matlib.config.GuiConfig;
import net.mat0u5.matlib.config.SimpleConfig;
import net.mat0u5.matlib.config.entry.ConfigGuiEntry;
import net.mat0u5.matlib.config.type.ConfigType;
import net.mat0u5.matlib.config.type.ConfigTypes;
import net.mat0u5.matlib.network.MatLibSimplePackets;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoService(RegistrableClient.class)
public class ConfigGuiClient implements RegistrableClient {
	private static final List<ConfigGuiEntry> pendingEntries = new ArrayList<>();

	public interface EntryFactory {
		ConfigEntry create(ConfigGuiEntry guiEntry) throws Exception;
	}
	private static final Map<String, EntryFactory> entryFactories = new HashMap<>();

	public static void registerEntryFactory(ConfigType<?> type, EntryFactory factory) {
		entryFactories.put(type.id(), factory);
	}

	public static void registerEventFactories() {
		registerEntryFactory(ConfigTypes.HEADER, e -> new TextConfigEntry(e));
		registerEntryFactory(ConfigTypes.BOOLEAN, e -> new BooleanConfigEntry(e, ConfigTypes.BOOLEAN.parse(e.value()), ConfigTypes.BOOLEAN.parse(e.defaultValue)));

		registerEntryFactory(ConfigTypes.INTEGER, e -> new IntegerConfigEntry(e, ConfigTypes.INTEGER.parse(e.value()), ConfigTypes.INTEGER.parse(e.defaultValue)));
		registerEntryFactory(ConfigTypes.HEARTS, e -> new HeartsConfigEntry(e, ConfigTypes.INTEGER.parse(e.value()), ConfigTypes.INTEGER.parse(e.defaultValue)));
		registerEntryFactory(ConfigTypes.SECONDS, e -> new SecondsConfigEntry(e, ConfigTypes.INTEGER.parse(e.value()), ConfigTypes.INTEGER.parse(e.defaultValue)));
		registerEntryFactory(ConfigTypes.NULLABLE_INTEGER, e -> new NullableIntegerConfigEntry(e, ConfigTypes.NULLABLE_INTEGER.parse(e.value()), ConfigTypes.NULLABLE_INTEGER.parse(e.defaultValue)));

		registerEntryFactory(ConfigTypes.DOUBLE, e -> new DoubleConfigEntry(e, ConfigTypes.DOUBLE.parse(e.value()), ConfigTypes.DOUBLE.parse(e.defaultValue)));
		registerEntryFactory(ConfigTypes.PERCENTAGE, e -> new PercentageConfigEntry(e, ConfigTypes.DOUBLE.parse(e.value()), ConfigTypes.DOUBLE.parse(e.defaultValue)));
		registerEntryFactory(ConfigTypes.MINUTES, e -> new MinutesConfigEntry(e, ConfigTypes.DOUBLE.parse(e.value()), ConfigTypes.DOUBLE.parse(e.defaultValue)));

		registerEntryFactory(ConfigTypes.STRING, e -> new StringConfigEntry(e, e.value(), e.defaultValue));
		registerEntryFactory(ConfigTypes.STRING_LIST, e -> new StringListConfigEntry(e, e.value(), e.defaultValue, e.options().isEmpty() ? null : e.options()));
		registerEntryFactory(ConfigTypes.ITEM_LIST, e -> new ItemListConfigEntry(e, e.value(), e.defaultValue));
		registerEntryFactory(ConfigTypes.BLOCK_LIST, e -> new BlockListConfigEntry(e, e.value(), e.defaultValue));
		registerEntryFactory(ConfigTypes.EFFECT_LIST, e -> new EffectListConfigEntry(e, e.value(), e.defaultValue));
		registerEntryFactory(ConfigTypes.ENCHANT_LIST, e -> new EnchantListConfigEntry(e, e.value(), e.defaultValue));
		registerEntryFactory(ConfigTypes.ENUM, e -> new EnumConfigEntry(e, e.value(), e.defaultValue, e.options()));
	}

	@Override
	public void onRegister() {
		registerEventFactories();
		MatLibSimplePackets.CLEAR_CONFIG.setClientReceive(empty -> pendingEntries.clear());
		MatLibSimplePackets.OPEN_CONFIG.setClientReceive(empty -> {
			List<ConfigGuiEntry> entries = List.copyOf(pendingEntries);
			pendingEntries.clear();
			openConfig(entries);
		});
		MatLibSimplePackets.CONFIG_GUI_ENTRY.setClientReceive(payload -> ConfigGuiClient.onEntry(payload.value()));
		MatLibSimplePackets.LOAD_CLIENT_CONFIG.setClientReceive(payload -> {
			String clientConfigId = payload.value();
			SimpleConfig configToOpen = ClientConfigManager.get(clientConfigId);
			// We fake the packets
			if (configToOpen instanceof GuiConfig guiConfig) {
				for (ConfigGuiEntry entry : guiConfig.buildGuiEntries()) {
					onEntry(entry.encode());
				}
			}
		});
	}

	public static void onEntry(List<String> payload) {
		try {
			ConfigGuiEntry entry = ConfigGuiEntry.decode(payload);
			pendingEntries.add(entry);
		} catch(Exception e) {
			MatLib.LOGGER.error("Failed to decode config gui entry {}", payload, e);
		}
	}

	public static void openConfig(List<ConfigGuiEntry> entries) {
		List<ConfigGuiEntry> serverEntries = new ArrayList<>();
		List<ConfigGuiEntry> clientEntries = new ArrayList<>();
		for (ConfigGuiEntry entry : entries) {
			if (ClientConfigManager.has(entry.configId)) {
				clientEntries.add(entry);
			}
			else {
				serverEntries.add(entry);
			}
		}
		if (serverEntries.isEmpty() && clientEntries.isEmpty() && !MatLib.DEBUG) return;

		ConfigScreen.Builder builder = new ConfigScreen.Builder(RenderUtils.getScreen(), Component.nullToEmpty("Config"));
		if (!serverEntries.isEmpty()) {
			addConfig(builder.addCategory("Server"), serverEntries);
		}
		if (!clientEntries.isEmpty()) {
			addConfig(builder.addCategory("Client"), clientEntries);
		}

		if (MatLib.DEBUG) {
			addTestingCategory(builder);
		}

		RenderUtils.setScreen(builder.build());
	}

	public static void addConfig(ConfigScreen.Builder.CategoryBuilder category, List<ConfigGuiEntry> guiEntries) {
		Map<String, GroupConfigEntry<?>> groupEntries = new HashMap<>();

		for (ConfigGuiEntry guiEntry : guiEntries) {
			ConfigEntry configEntry = createEntry(guiEntry);
			if (configEntry == null) continue;
			if (guiEntry.isNew) {
				configEntry.setNew();
			}

			if (guiEntry.groupHeader && !guiEntry.group.isEmpty()) {
				GroupConfigEntry<?> groupEntry = createGroupEntry(configEntry);
				if (groupEntry != null) {
					groupEntries.put(groupKey(guiEntry, guiEntry.group), groupEntry);
					addToParentOrCategory(guiEntry, groupEntry, groupEntries, category);
					continue;
				}
			}

			if (guiEntry.group.isEmpty()) {
				category.addEntry(configEntry);
			}
			else {
				addToGroup(guiEntry, configEntry, groupEntries, category);
			}
		}
	}

	private static String groupKey(ConfigGuiEntry guiEntry, String groupPath) {
		return guiEntry.configId + ":" + groupPath;
	}

	private static void addToParentOrCategory(ConfigGuiEntry guiEntry, GroupConfigEntry<?> groupEntry, Map<String, GroupConfigEntry<?>> groupEntries, ConfigScreen.Builder.CategoryBuilder category) {
		String groupPath = guiEntry.group;
		int lastDotIndex = groupPath.lastIndexOf('.');
		if (lastDotIndex != -1) {
			String parentPath = groupPath.substring(0, lastDotIndex);
			GroupConfigEntry<?> parentGroup = groupEntries.get(groupKey(guiEntry, parentPath));
			if (parentGroup != null) {
				parentGroup.addChildEntry(groupEntry);
			}
			else {
				category.addEntry(groupEntry);
			}
		}
		else {
			category.addEntry(groupEntry);
		}
	}

	private static void addToGroup(ConfigGuiEntry guiEntry, ConfigEntry configEntry, Map<String, GroupConfigEntry<?>> groupEntries, ConfigScreen.Builder.CategoryBuilder category) {
		GroupConfigEntry<?> targetGroup = groupEntries.get(groupKey(guiEntry, guiEntry.group));
		if (targetGroup != null) {
			targetGroup.addChildEntry(configEntry);
		}
		else {
			MatLib.LOGGER.error("Could not find parent group {} for entry {}", guiEntry.group, guiEntry.key);
			category.addEntry(configEntry);
		}
	}

	private static GroupConfigEntry<?> createGroupEntry(ConfigEntry configEntry) {
		if (!(configEntry instanceof IEntryGroupHeader)) {
			MatLib.LOGGER.error("Group entry {} does not implement IEntryGroupHeader", configEntry.getFieldName());
			return null;
		}

		boolean showSidebar = true;
		boolean openByDefault = false;

		if (configEntry instanceof TextConfigEntry textConfigEntry) {
			return new GroupConfigEntry<>(textConfigEntry, new ArrayList<>(), showSidebar, openByDefault);
		}
		if (configEntry instanceof BooleanConfigEntry booleanConfigEntry) {
			return new GroupConfigEntry<>(booleanConfigEntry, new ArrayList<>(), showSidebar, openByDefault);
		}
		if (configEntry instanceof NumberConfigEntry<?> numberConfigEntry) {
			return new GroupConfigEntry<>(numberConfigEntry, new ArrayList<>(), showSidebar, openByDefault);
		}

		return null;
	}

	public static ConfigEntry createEntry(ConfigGuiEntry guiEntry) {
		EntryFactory factory = entryFactories.get(guiEntry.type.id());
		if (factory == null) {
			MatLib.LOGGER.error("No config gui entry registered for type {} (entry {})", guiEntry.type.id(), guiEntry.key);
			return null;
		}
		try {
			return factory.create(guiEntry);
		} catch (Exception e) {
			MatLib.LOGGER.error("Failed to create config gui entry {}", guiEntry.key, e);
			return null;
		}
	}

	private static ConfigGuiEntry testEntry(String key, String name, String description, ConfigType<?> type, String value) {
		return new ConfigGuiEntry("matlib:testing", key, type, name, description, "", false, false, false, value, new ArrayList<>(List.of(value)));
	}

	public static void addTestingCategory(ConfigScreen.Builder builder) {
		ConfigScreen.Builder.CategoryBuilder category = builder.addCategory("Testing");
		category.addEntry(new TextConfigEntry(testEntry("", "Test", "", ConfigTypes.HEADER, "")));
		category.addEntry(new PercentageConfigEntry(testEntry("id2", "Percentage", "description2", ConfigTypes.PERCENTAGE, "0.5"), 0.5, 0.5));
		category.addEntry(new HeartsConfigEntry(testEntry("id3", "Hearts", "description3", ConfigTypes.HEARTS, "20"), 20, 20));
	}
}
