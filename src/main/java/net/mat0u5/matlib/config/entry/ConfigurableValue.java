package net.mat0u5.matlib.config.entry;

import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.config.Config;
import net.mat0u5.matlib.config.type.ConfigType;
import net.mat0u5.matlib.config.type.ConfigTypes;
import net.mat0u5.matlib.utils.other.TextUtils;

public class ConfigurableValue<T> {
	public enum Environment {
		UNKNOWN,
		SERVER,
		SERVER_SHARED,
		CLIENT
	}

	private final Config config;
	private final String key;
	private final T defaultValue;
	private final String defaultValueString;
	private final ConfigType<T> type;
	private volatile boolean loaded = false;
	private volatile T cachedValue;
	private volatile String cachedStringValue;

	private Environment env = Environment.UNKNOWN;
	private String name = "";
	private String description = "";
	private String group = "";

	private ConfigurableValue(Config config, String key, T defaultValue, ConfigType<T> type) {
		this.config = config;
		this.key = key;
		this.type = type;
		this.defaultValue = defaultValue;
		this.defaultValueString = getValueAsString(defaultValue);
	}

	public static <T> ConfigurableValue<T> register(Config config, String key, T defaultValue) {
		return register(config, key, defaultValue, ConfigTypes.detect(defaultValue));
	}

	public static <T> ConfigurableValue<T> register(Config config, String key, T defaultValue, ConfigType<T> type) {
		ConfigurableValue<T> value = new ConfigurableValue<>(config, key, defaultValue, type);
		config.addConfigurableValue(value);
		value.reload();
		return value;
	}

	public ConfigurableValue<T> server() {
		this.env = Environment.SERVER;
		return this;
	}

	public ConfigurableValue<T> client() {
		this.env = Environment.CLIENT;
		return this;
	}

	public ConfigurableValue<T> serverShared() {
		this.env = Environment.SERVER_SHARED;
		return this;
	}

	public ConfigurableValue<T> name(String name) {
		this.name = name;
		return this;
	}

	public ConfigurableValue<T> description(String description) {
		this.description = description;
		return this;
	}

	public T getDefaultValue() {
		return this.defaultValue;
	}

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public T get() {
		if (!loaded) reload();
		return cachedValue;
	}

	public String getAsString() {
		if (!loaded) reload();
		return cachedStringValue;
	}

	public T reload() {
		cachedValue = getFromConfig();
		cachedStringValue = getValueAsString(cachedValue);
		loaded = true;
		return cachedValue;
	}

	public void set(T value) {
		cachedValue = value;
		cachedStringValue = getValueAsString(value);
		config.setProperty(this.key, getValueAsString(value));
	}

	public void setFromString(String valueStr) throws Exception {
		cachedValue = this.type.parse(valueStr);
		cachedStringValue = valueStr;
		config.setProperty(this.key, valueStr);
	}

	public void setOrRemove(T value) {
		if (value == null) {
			remove();
		}
		else {
			set(value);
		}
	}

	public void remove() {
		config.removeProperty(this.key);
	}

	public ConfigType<T> getType() {
		return type;
	}

	private T getFromConfig() {
		String strValue = config.getOrCreateProperty(key, getValueAsString(defaultValue));
		try {
			return type.parse(strValue);
		} catch (Exception e) {
			MatLib.LOGGER.error(TextUtils.formatString("Invalid value '{}' for config {}, returning default value {}", strValue, key, defaultValue), e);
			return defaultValue;
		}
	}

	private String getValueAsString(T value) {
		return value == null ? "" : type.serialize(value);
	}
}
