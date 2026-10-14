package net.mat0u5.matlib.config.type;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public final class ConfigTypes {
	public static final ConfigType<Integer> INTEGER = fromParser("integer", Integer::parseInt);
	public static final ConfigType<Double> DOUBLE = fromParser("double", Double::parseDouble);
	public static final ConfigType<String> STRING = fromParser("string", str -> str);
	public static final ConfigType<Boolean> BOOLEAN = fromParser("boolean", str -> {
		if (str.equalsIgnoreCase("true")) return true;
		if (str.equalsIgnoreCase("false")) return false;
		throw new IllegalArgumentException("Not a boolean: " + str);
	});
	public static final ConfigType<Double> PERCENTAGE = withId("percentage", DOUBLE);

	public static <E extends Enum<E>> ConfigType<E> enumOf(Class<E> clazz) {
		return new ConfigType<>() {
			public E parse(String raw) {
				return Enum.valueOf(clazz, raw.trim());
			}
			public String serialize(E value) {
				return value.name();
			}
			public String id() {
				return "enum";
			}
			public List<String> options() {
				return Arrays.stream(clazz.getEnumConstants()).map(Enum::name).toList();
			}
		};
	}

	public static final ConfigType<List<String>> STRING_LIST = new ConfigType<>() {
		public List<String> parse(String raw) {
			if (raw.startsWith("[") && raw.endsWith("]")) raw = raw.substring(1, raw.length()-1);
			if (raw.isBlank()) return List.of();
			return Arrays.stream(raw.split(",")).map(String::trim).toList();
		}
		public String serialize(List<String> value) {
			return "[" + String.join(",", value) + "]";
		}
		public String id() {
			return "string_list";
		}
	};

	public static <T> ConfigType<T> withId(String id, ConfigType<T> base) {
		return new ConfigType<>() {
			public T parse(String s) throws Exception { return base.parse(s); }
			public String serialize(T v) { return base.serialize(v); }
			public String id() { return id; }
			public List<String> options() { return base.options(); }
		};
	}

	private static <T> ConfigType<T> fromParser(String id, Function<String, T> parser) {
		return new ConfigType<>() {
			public T parse(String raw) {
				return parser.apply(raw.trim());
			}
			public String serialize(T value) {
				return String.valueOf(value);
			}
			public String id() {
				return id;
			}
		};
	}

	public static <T> ConfigType<T> detect(T defaultValue) {
		if (defaultValue instanceof Integer) return (ConfigType<T>) INTEGER;
		if (defaultValue instanceof Double) return (ConfigType<T>) DOUBLE;
		if (defaultValue instanceof Boolean) return (ConfigType<T>) BOOLEAN;
		if (defaultValue instanceof String) return (ConfigType<T>) STRING;
		if (defaultValue instanceof Enum<?> e) return (ConfigType<T>) enumOf((Class) e.getDeclaringClass());
		throw new IllegalArgumentException("Failed to detect Config Type for " + defaultValue.getClass().getName() + ". Set one directly.");
	}
}
