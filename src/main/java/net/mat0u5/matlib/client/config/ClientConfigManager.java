package net.mat0u5.matlib.client.config;

import com.google.auto.service.AutoService;
import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.client.services.RegistrableClient;
import net.mat0u5.matlib.config.SimpleConfig;
import net.mat0u5.matlib.config.entry.ConfigurableValue;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AutoService(RegistrableClient.class)
public class ClientConfigManager implements RegistrableClient {
	@Override
	public void onRegister() {
	}

	private static final Map<String, SimpleConfig> registeredConfigs = new ConcurrentHashMap<>();
	public static void registerConfig(SimpleConfig config) {
		String configId = config.getConfigId();
		if (registeredConfigs.containsKey(configId)) {
			MatLib.LOGGER.error("Tried to register duplicate config: {}", configId);
			return;
		}
		registeredConfigs.put(configId, config);
		// Instantiate all properties
		config.reload();
	}

	public static Collection<SimpleConfig> getAll() {
		return registeredConfigs.values();
	}

	public static boolean has(String configId) {
		return registeredConfigs.containsKey(configId);
	}

	public static SimpleConfig get(String configId) {
		return registeredConfigs.get(configId);
	}

	public static ConfigurableValue<?> getValue(String configId, String key) {
		SimpleConfig config = registeredConfigs.get(configId);
		return config == null ? null : config.getValue(key);
	}

	public static void reloadConfigs(Collection<String> configIds) {
		for (String clientConfig : configIds) {
			SimpleConfig config = get(clientConfig);
			if (config != null) {
				config.reload();
			}
		}
	}
}