package net.mat0u5.matlib.config;

import net.mat0u5.matlib.config.entry.ConfigEntry;
import net.mat0u5.matlib.config.entry.ConfigGuiEntry;
import net.mat0u5.matlib.config.entry.ConfigurableValue;
import net.mat0u5.matlib.config.entry.DynamicEntry;
import net.mat0u5.matlib.network.MatLibSimplePackets;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
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
		if (e instanceof ConfigurableValue<?> v) {
			values.remove(v.getKey());
		}
	}

	public void openGuiFor(ServerPlayer player) {
		MatLibSimplePackets.CLEAR_CONFIG.sendToClient(player);

		if (hasPermissionsToOpen(player)) {
			//Server
			for (ConfigGuiEntry entry : buildGuiEntries()) {
				MatLibSimplePackets.CONFIG_GUI_ENTRY.sendToClient(entry.encode(), List.of(player));
			}
		}
		String clientConfigId = getClientCounterpartId();
		if (clientConfigId != null) {
			MatLibSimplePackets.LOAD_CLIENT_CONFIG.sendToClient(clientConfigId, player); //Client
		}

		MatLibSimplePackets.OPEN_CONFIG.sendToClient(getGuiTitle(), player);
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
