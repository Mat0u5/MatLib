package net.mat0u5.matlib.config.type;

import java.util.*;
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
	public static final ConfigType<Double> MINUTES = withId("minutes", DOUBLE);
	public static final ConfigType<Integer> HEARTS = withId("hearts", INTEGER);
	public static final ConfigType<Integer> SECONDS = withId("seconds", INTEGER);
	public static final ConfigType<Integer> NULLABLE_INTEGER = fromParser("nullable_integer", str -> {
		try {
			return Integer.parseInt(str);
		} catch(Exception e) {
			return null;
		}
	});

	public static final ConfigType<Object> HEADER = new ConfigType<>() {
		public Object parse(String string) throws Exception {
			return null;
		}
		public String serialize(Object value) {
			return null;
		}
		public String id() {
			return "header";
		}
	};

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
	public static final ConfigType<List<String>> ITEM_LIST = withId("item_list", STRING_LIST);
	public static final ConfigType<List<String>> BLOCK_LIST = withId("block_list", STRING_LIST);
	public static final ConfigType<List<String>> EFFECT_LIST = withId("effect_list", STRING_LIST);
	public static final ConfigType<List<String>> ENCHANT_LIST = withId("enchant_list", STRING_LIST);
	public static final ConfigType<List<String>> ENUM = withId("enum", STRING_LIST);

	/*
		Helpers
	 */
	private static final Map<String, ConfigType<?>> registeredConfigTypes = new HashMap<>();
	public static void registerConfigType(ConfigType<?> configType) {
		registeredConfigTypes.put(configType.id(), configType);
	}

	public static ConfigType<?> fromId(String id) throws Exception {
		ConfigType<?> returnValue = registeredConfigTypes.get(id);
		if (returnValue == null) throw new IllegalArgumentException("Invalid ConfigType id: " + id);
		return returnValue;
	}

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
