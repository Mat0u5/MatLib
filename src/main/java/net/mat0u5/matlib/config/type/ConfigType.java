package net.mat0u5.matlib.config.type;

import java.util.List;

public abstract class ConfigType<T> {
	public ConfigType() {
		this(true);
	}

	protected ConfigType(boolean register) {
		if (register) {
			ConfigTypes.registerConfigType(this);
		}
	}

	public abstract T parse(String string) throws Exception;
	public abstract String serialize(T value);
	public abstract String id();
	public List<String> options() { return List.of(); }
}
