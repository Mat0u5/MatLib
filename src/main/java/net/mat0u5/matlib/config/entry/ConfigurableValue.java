package net.mat0u5.matlib.config.entry;

import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.config.GuiConfig;
import net.mat0u5.matlib.config.SimpleConfig;
import net.mat0u5.matlib.config.type.ConfigType;
import net.mat0u5.matlib.config.type.ConfigTypes;
import net.mat0u5.matlib.network.MatLibSimplePackets;
import net.mat0u5.matlib.utils.other.TextUtils;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;

public class ConfigurableValue<T> implements ConfigEntry {
	public enum Environment {
		UNKNOWN,
		SERVER,
		SERVER_SHARED,
		CLIENT;
		public boolean isSynced() {
			return this == SERVER_SHARED;
		}
	}

	private final SimpleConfig config;
	private final String key;
	private T defaultValue;
	private ConfigType<T> type;
	private volatile boolean loaded = false;
	private volatile T cachedValue;
	private volatile boolean syncedValueLoaded = false;
	private volatile T syncedValue;
	private volatile String cachedStringValue;

	//TODO check for doubled calls
	private final List<BiConsumer<T, T>> listeners = new CopyOnWriteArrayList<>();
	private Environment env = Environment.UNKNOWN;
	private String name = "";
	private String description = "";
	private String group = "";
	private boolean isGroupHeader = false;
	private boolean isNew = false;
	private boolean requiresReload = false;
	private BooleanSupplier visibleIf = () -> true;

	private ConfigurableValue(SimpleConfig config, String key, T defaultValue, ConfigType<T> type) {
		this.config = config;
		this.key = key;
		this.type = type;
		this.defaultValue = defaultValue;
	}

	public static <T> ConfigurableValue<T> register(SimpleConfig config, String key, T defaultValue) {
		return register(config, key, defaultValue, ConfigTypes.detect(defaultValue));
	}

	public static <T> ConfigurableValue<T> register(SimpleConfig config, String key, T defaultValue, ConfigType<T> type) {
		ConfigurableValue<T> value = new ConfigurableValue<>(config, key, defaultValue, type);
		if (config instanceof GuiConfig guiConfig) {
			guiConfig.addEntry(value);
		}
		else {
			config.addConfigurableValue(value);
		}
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

	public ConfigurableValue<T> group(String group) {
		this.group = group;
		return this;
	}

	public ConfigurableValue<T> groupHeader(boolean isGroupHeader) {
		this.isGroupHeader = isGroupHeader;
		return this;
	}

	public ConfigurableValue<T> setNew(boolean isNew) {
		this.isNew = isNew;
		return this;
	}

	public ConfigurableValue<T> setRequiresReload(boolean requiresReload) {
		this.requiresReload = requiresReload;
		return this;
	}

	public ConfigurableValue<T> visibleIf(BooleanSupplier supplier) {
		this.visibleIf = supplier;
		return this;
	}
	public ConfigurableValue<T> defaultValue(T v) {
		if (!loaded) this.defaultValue = v;
		return this;
	}
	public ConfigurableValue<T> type(ConfigType<T> t) {
		if (!loaded) this.type = t;
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

	public String getGroup() {
		return this.group;
	}

	public boolean isNew() {
		return this.isNew;
	}

	public boolean requiresReload() {
		return this.requiresReload;
	}

	public boolean isGroupHeader() {
		return this.isGroupHeader;
	}

	public boolean isVisible() {
		return this.visibleIf.getAsBoolean();
	}

	public Environment getEnv() {
		return this.env;
	}

	public String getKey() {
		return key;
	}

	public void setSyncedValue(String raw) {
		try {
			T old = get();
			syncedValue = type.parse(raw);
			syncedValueLoaded = true;
			if (!Objects.equals(old, syncedValue)){
				listeners.forEach(l -> l.accept(old, syncedValue));
			}
		} catch (Exception e) {
			MatLib.LOGGER.error("Bad synced value '{}' for {}", raw, key, e);
		}
	}

	public void clearSynced() {
		syncedValueLoaded = false;
		syncedValue = null;
	}

	//TODO check for doubled calls
	public void sendSyncTo(List<ServerPlayer> targets) {
		if (MatLib.isLogicalSide() && env.isSynced()) {
			MatLibSimplePackets.SYNCED_CONFIG.sendToClient(List.of(config.getConfigId(), getKey(), getAsString()), targets);
		}
	}

	public T get() {
		if (syncedValueLoaded) return syncedValue;
		if (!loaded) reload();
		return cachedValue;
	}

	public String getAsString() {
		if (!loaded) reload();
		return cachedStringValue;
	}

	public T reload() {
		boolean wasLoaded = loaded;
		T old = cachedValue;
		T value = getFromConfig();
		cachedValue = value;
		cachedStringValue = getValueAsString(value);
		loaded = true;
		if (wasLoaded && !Objects.equals(old, value)) {
			listeners.forEach(l -> l.accept(old, value));
			config.onValueChanged(this, old, value);
		}
		return value;
	}

	public void setFromString(String valueStr) throws Exception {
		set(this.type.parse(valueStr));
	}

	public void set(T value) {
		T old = cachedValue;
		cachedValue = value;
		cachedStringValue = getValueAsString(value);
		config.setProperty(this.key, getValueAsString(value));
		if (!Objects.equals(old, value)) {
			listeners.forEach(listener -> listener.accept(old, value));
			config.onValueChanged(this, old, value);
		}
	}

	public ConfigurableValue<T> onChange(BiConsumer<T, T> l) {
		listeners.add(l);
		return this;
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
		cachedValue = null;
		cachedStringValue = null;
		loaded = true;
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
