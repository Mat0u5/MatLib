package net.mat0u5.matlib.client.config;

import com.google.auto.service.AutoService;
import net.mat0u5.matlib.client.services.RegistrableClient;
import net.mat0u5.matlib.config.entry.ConfigGuiEntry;
import net.mat0u5.matlib.network.MatLibSimplePackets;

import java.util.ArrayList;
import java.util.List;

@AutoService(RegistrableClient.class)
public class ConfigGuiClient implements RegistrableClient {
	private static final List<ConfigGuiEntry> pending = new ArrayList<>();

	@Override
	public void onRegister() {
		MatLibSimplePackets.CLEAR_CONFIG.setClientReceive(empty -> pending.clear());
		MatLibSimplePackets.OPEN_CONFIG.setClientReceive(empty -> {
			List<ConfigGuiEntry> entries = List.copyOf(pending);
			pending.clear();
			//Open GUI
		});
		MatLibSimplePackets.LOAD_CLIENT_CONFIG.setClientReceive(stringPayload -> {
			String clientConfigId = stringPayload.value();
			//TODO
		});
	}

	public static void onEntry(List<String> payload) {
		ConfigGuiEntry entry = ConfigGuiEntry.decode(payload);
		pending.add(entry);
	}
}
