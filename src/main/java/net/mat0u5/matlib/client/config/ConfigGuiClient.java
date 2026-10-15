package net.mat0u5.matlib.client.config;

import com.google.auto.service.AutoService;
import net.mat0u5.matlib.client.services.RegistrableClient;
import net.mat0u5.matlib.config.entry.ConfigGuiEntry;
import net.mat0u5.matlib.network.MatLibSimplePackets;

import java.util.ArrayList;
import java.util.List;

@AutoService(RegistrableClient.class)
public class ConfigGuiClient implements RegistrableClient {
	private static final List<ConfigGuiEntry> pendingEntries = new ArrayList<>();

	@Override
	public void onRegister() {
		MatLibSimplePackets.CLEAR_CONFIG.setClientReceive(empty -> pendingEntries.clear());
		MatLibSimplePackets.OPEN_CONFIG.setClientReceive(empty -> {
			List<ConfigGuiEntry> entries = List.copyOf(pendingEntries);
			pendingEntries.clear();
			//Open GUI
		});
		MatLibSimplePackets.CONFIG_GUI_ENTRY.setClientReceive(payload -> ConfigGuiClient.onEntry(payload.value()));
		MatLibSimplePackets.LOAD_CLIENT_CONFIG.setClientReceive(payload -> {
			String clientConfigId = payload.value();
			//TODO
		});
	}

	public static void onEntry(List<String> payload) {
		try {
			ConfigGuiEntry entry = ConfigGuiEntry.decode(payload);
			pendingEntries.add(entry);
		}catch(Exception e) {}
	}
}
