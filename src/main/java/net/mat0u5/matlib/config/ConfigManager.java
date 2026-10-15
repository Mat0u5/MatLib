package net.mat0u5.matlib.config;

import com.google.auto.service.AutoService;
import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.config.entry.ConfigurableValue;
import net.mat0u5.matlib.events.server.ServerPlayerEvents;
import net.mat0u5.matlib.events.server.ServerResourceEvents;
import net.mat0u5.matlib.services.RegistrableServer;
import net.mat0u5.matlib.utils.player.PlayerUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AutoService(RegistrableServer.class)
public class ConfigManager implements RegistrableServer {
	@Override
	public void onRegister() {
		ServerPlayerEvents.CONNECT.register((connection, player) -> {
			getAll().forEach(config -> config.sendAllSyncedConfigs(List.of(player)));
		});
		ServerResourceEvents.RELOAD_START.register((server, resourceManager) -> {
			getAll().forEach(SimpleConfig::reload);
		});
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
		config.sendAllSyncedConfigs(PlayerUtils.getAllPlayers());
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

	public static void clearSynced() {
		registeredConfigs.values().forEach(config -> config.getConfigurableValues().forEach(ConfigurableValue::clearSynced));
	}
}
