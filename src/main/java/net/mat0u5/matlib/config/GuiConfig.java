package net.mat0u5.matlib.config;

import net.mat0u5.matlib.config.entry.ConfigEntry;
import net.mat0u5.matlib.config.entry.ConfigGuiEntry;
import net.mat0u5.matlib.config.entry.ConfigurableValue;
import net.mat0u5.matlib.config.entry.DynamicEntry;
import net.mat0u5.matlib.network.MatLibSimplePackets;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class GuiConfig extends SimpleConfig {

	protected final List<ConfigEntry> entries = new ArrayList<>();
	protected final String guiTitle;

	protected GuiConfig(String configId, String guiTitle, String folderPath, String filePath) {
		super(configId, folderPath, filePath);
		this.guiTitle = guiTitle;
	}

	public String getGuiTitle() {
		return guiTitle;
	}

	public List<DynamicEntry> getDynamicEntries() {
		return List.of();
	}
	public abstract boolean hasPermissionsToOpen(ServerPlayer player);
	public String getClientCounterpartId() {
		return null;
	}

	public boolean hasClientCounterpart() {
		return getClientCounterpartId() != null;
	}

	public void onDynamicEntryUpdate(ServerPlayer player, ConfigGuiEntry entry) {
	}

	public void addEntry(ConfigEntry entry) {
		if (entry instanceof ConfigurableValue<?> value) {
			addConfigurableValue(value);
		}
		entries.add(entry);
	}

	public void removeEntry(ConfigEntry e) {
		entries.remove(e);
	}

	public List<ConfigEntry> getEntries() {
		return Collections.unmodifiableList(entries);
	}

	public GuiOpenResult openGuiFor(ServerPlayer player) {
		boolean hasPermission = hasPermissionsToOpen(player);
		if (!hasClientCounterpart() && !hasPermission) {
			return GuiOpenResult.NONE;
		}

		MatLibSimplePackets.CLEAR_CONFIG.sendToClient(player);

		boolean sentServerEntries = false;
		if (hasPermission) {
			//Server
			for (ConfigGuiEntry entry : buildGuiEntries()) {
				MatLibSimplePackets.CONFIG_GUI_ENTRY.sendToClient(entry.encode(), List.of(player));
				sentServerEntries = true;
			}
		}

		boolean sentClientEntries = false;
		String clientConfigId = getClientCounterpartId();
		if (clientConfigId != null) {
			MatLibSimplePackets.LOAD_CLIENT_CONFIG.sendToClient(clientConfigId, player); //Client
			sentClientEntries = true;
		}

		MatLibSimplePackets.OPEN_CONFIG.sendToClient(getGuiTitle(), player);

		if (sentServerEntries && !sentClientEntries) return GuiOpenResult.SERVER;
		if (!sentServerEntries && sentClientEntries) return GuiOpenResult.CLIENT;
		return GuiOpenResult.BOTH;
	}
	public enum GuiOpenResult {
		NONE,
		CLIENT,
		SERVER,
		BOTH;
		public boolean isNone() {
			return this == NONE;
		}
		public Component getText() {
			if (this == NONE) return Component.literal("§cYou do not have permissions to open the Server Config");
			if (this == CLIENT) return Component.literal("§7Opening Client Config GUI...");
			if (this == SERVER) return Component.literal("§7Opening Server Config GUI...");
			return Component.literal("§7Opening Config GUI...");
		}
	}

	public List<ConfigGuiEntry> buildGuiEntries() {
		List<ConfigGuiEntry> list = new ArrayList<>();
		for (ConfigEntry entry : entries) {
			if (!entry.isVisible()) continue;
			if (entry instanceof ConfigurableValue<?> value) {
				list.add(ConfigGuiEntry.fromValue(configId, value));
			}
			else {
				list.add(ConfigGuiEntry.fromHeader(configId, entry));
			}
		}
		for (DynamicEntry dynamicEntry : getDynamicEntries()) {
			list.add(ConfigGuiEntry.fromDynamic(configId, dynamicEntry));
		}
		return list;
	}
}
