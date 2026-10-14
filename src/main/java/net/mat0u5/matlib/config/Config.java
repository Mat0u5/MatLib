package net.mat0u5.matlib.config;

import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.config.entry.ConfigurableValue;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public abstract class Config {

	protected final List<ConfigurableValue<?>> values = new ArrayList<>();
	protected Properties properties = new Properties();
	protected String folderPath;
	protected String filePath;

	public Map<String, String> getRenamedProperties() {
		return Map.of();
	}

	public List<ConfigurableValue<?>> getConfigurableValues() {
		return values;
	}

	public void addConfigurableValue(ConfigurableValue<?> value) {
		values.add(value);
	}

	protected Config(String folderPath, String filePath) {
		this.folderPath = folderPath;
		this.filePath = folderPath + "/" + filePath;
		createFileIfNotExists();
		loadProperties();
		renamedProperties();
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
		getRenamedProperties().forEach(this::renamedProperty);
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
		if (folderPath == null || filePath == null) return "";
		if (properties == null) return "";

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
