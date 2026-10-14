package net.mat0u5.matlib.client.config;

import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.config.ConfigManager;
import net.mat0u5.matlib.config.entry.ConfigurableValue;

import java.util.List;

public class ConfigManagerClient {
	public static void onConfigSyncPacket(List<String> payload) {
		if (payload.size() < 3) return;
		if (MatLib.isLogicalSide()) return;
		String configId = payload.get(0);
		String key = payload.get(1);
		String valueString = payload.get(2);
		ConfigurableValue<?> value = ConfigManager.getValue(configId, key);
		if (value != null && value.getEnv().isSynced()) {
			value.setSyncedValue(valueString);
		}
	}
}
