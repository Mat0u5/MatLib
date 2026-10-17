package net.mat0u5.matlib.config;

import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.config.entry.ConfigurableValue;
import net.mat0u5.matlib.utils.other.OtherUtils;
import net.mat0u5.matlib.utils.player.PlayerUtils;
import net.minecraft.server.level.ServerPlayer;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public abstract class SimpleConfig {

	protected final Map<String, ConfigurableValue<?>> values = new LinkedHashMap<>();
	protected Properties properties = new Properties();
	protected final String configId;
	protected final String folderPath;
	protected final String filePath;

	protected SimpleConfig(String configId, String folderPath, String filePath) {
		this(configId, folderPath == null ? null : Paths.get("./config", folderPath), filePath);
	}

	protected SimpleConfig(String configId, Path folder, String filePath) {
		this.configId = configId;
		this.folderPath = (folder == null || filePath == null) ? null : folder.toString();
		this.filePath = this.folderPath == null ? null : Paths.get(this.folderPath, filePath).toString();
		createFileIfNotExists();
		loadProperties();
		renamedProperties();
	}

	public String getConfigId() {
		return configId;
	}

	public Map<String, String> getRenamedProperties() {
		return Map.of();
	}

	public Collection<ConfigurableValue<?>> getConfigurableValues() {
		return values.values();
	}

	public ConfigurableValue<?> getValue(String key) {
		return values.get(key);
	}

	public void reload() {
		loadProperties();
		values.values().forEach(ConfigurableValue::reload);
	}

	public void addConfigurableValue(ConfigurableValue<?> value) {
		String valueKey = value.getKey();
		if (values.containsKey(valueKey)) {
			MatLib.LOGGER.error("Tried to register duplicate configurable value: {}", valueKey);
			return;
		}
		values.put(valueKey, value);
	}


	public <T> void onValueChanged(ConfigurableValue<T> configurableValue, T oldValue, T newValue) {
		if (MatLib.isLogicalSide() && configurableValue.getEnv().isSynced()) {
			configurableValue.sendSyncTo(PlayerUtils.getAllPlayers());
		}
		if (configurableValue.requiresReload()) {
			OtherUtils.queueServerReload();
		}
	}

	public void sendAllSyncedConfigs(List<ServerPlayer> targets) {
		getConfigurableValues().forEach(configurableValue -> {
			if (configurableValue.getEnv().isSynced()) {
				configurableValue.sendSyncTo(targets);
			}
		});
	}

	private void createFileIfNotExists() {
		if (folderPath == null || filePath == null) return;
		File configDir = new File(folderPath);
		if (!configDir.exists()) {
			if (!configDir.mkdirs()) {
				MatLib.LOGGER.error("Failed to create folder {}", configDir);
				return;
			}
		}

		File configFile = new File(filePath);
		if (!configFile.exists()) {
			try {
				if (!configFile.createNewFile()) {
					MatLib.LOGGER.error("Failed to create file {}", configFile);
					return;
				}
				try (OutputStream output = new FileOutputStream(configFile)) {
					properties.store(output, null);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void loadProperties() {
		if (folderPath == null || filePath == null) return;

		properties = new Properties();
		try (InputStream input = new FileInputStream(filePath)) {
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void renamedProperties() {
		Map<String, String> renamed = getRenamedProperties();
		if (renamed == null) return;
		renamed.forEach(this::renamedProperty);
	}

	private void renamedProperty(String from, String to) {
		if (properties.containsKey(from)) {
			if (!properties.containsKey(to)) {
				String value = getProperty(from);
				if (value != null) {
					setProperty(to, value);
				}
			}
			removeProperty(from);
		}
	}

	public void setOrRemoveProperty(String key, String value) {
		if (value == null || value.isEmpty()) {
			removeProperty(key);
		}
		else {
			setProperty(key, value);
		}
	}

	public void setProperty(String key, String value) {
		if (folderPath == null || filePath == null) return;
		properties.setProperty(key, value);
		try (OutputStream output = new FileOutputStream(filePath)) {
			properties.store(output, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeProperty(String key) {
		if (folderPath == null || filePath == null) return;
		if (!properties.containsKey(key)) return;
		properties.remove(key);
		try (OutputStream output = new FileOutputStream(filePath)) {
			properties.store(output, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setPropertyCommented(String key, String value, String comment) {
		if (folderPath == null || filePath == null) return;
		properties.setProperty(key, value);
		try (OutputStream output = new FileOutputStream(filePath)) {
			properties.store(output, comment);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void resetProperties(String comment) {
		properties.clear();
		if (folderPath == null || filePath == null) return;
		try (OutputStream output = new FileOutputStream(filePath)) {
			properties.store(output, comment);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    /*
        Various getters
     */

	public String getProperty(String key) {
		if (folderPath == null || filePath == null) return null;
		if (properties == null) return null;

		if (properties.containsKey(key)) {
			return properties.getProperty(key);
		}
		return null;
	}

	public String getOrCreateProperty(String key, String defaultValue) {
		if (folderPath == null || filePath == null) return defaultValue;
		if (properties == null) return defaultValue;

		if (properties.containsKey(key)) {
			return properties.getProperty(key);
		}
		setProperty(key, defaultValue);
		return defaultValue;
	}

	public boolean getOrCreateBoolean(String key, boolean defaultValue) {
		String value = getOrCreateProperty(key, String.valueOf(defaultValue));
		if (value == null) return defaultValue;
		if (value.equalsIgnoreCase("true")) return true;
		if (value.equalsIgnoreCase("false")) return false;
		return defaultValue;
	}

	public double getOrCreateDouble(String key, double defaultValue) {
		String value = getOrCreateProperty(key, String.valueOf(defaultValue));
		if (value == null) return defaultValue;
		try {
			return Double.parseDouble(value);
		} catch (Exception ignored) {}
		return defaultValue;
	}

	public int getOrCreateInt(String key, int defaultValue) {
		String value = getOrCreateProperty(key, String.valueOf(defaultValue));
		if (value == null) return defaultValue;
		try {
			return Integer.parseInt(value);
		} catch (Exception ignored) {}
		return defaultValue;
	}
}
