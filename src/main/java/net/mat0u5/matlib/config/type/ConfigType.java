package net.mat0u5.matlib.config.type;

public interface ConfigType<T> {
	T parse(String string) throws Exception;
	String serialize(T value);
	default String id() { return "default"; }
}
