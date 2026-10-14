package net.mat0u5.matlib.config.type;

import java.util.List;

public interface ConfigType<T> {
	T parse(String string) throws Exception;
	String serialize(T value);
	String id();
	default List<String> options() { return List.of(); }
}
