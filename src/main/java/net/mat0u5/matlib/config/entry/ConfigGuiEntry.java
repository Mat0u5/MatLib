package net.mat0u5.matlib.config.entry;

import net.mat0u5.matlib.config.type.ConfigType;
import net.mat0u5.matlib.config.type.ConfigTypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConfigGuiEntry {
	public String configId;
	public String key;
	public ConfigType<?> type;
	public String name;
	public String description;
	public String group;
	public boolean isNew;
	public boolean groupHeader;
	public boolean requiresReload;
	public List<String> args;

	public ConfigGuiEntry(
			String configId,
			String key,
			ConfigType<?> type,
			String name,
			String description,
			String group,
			boolean isNew,
			boolean groupHeader,
			boolean requiresReload,
			List<String> args
	) {
		this.configId = configId;
		this.key = key;
		this.type = type;
		this.name = name;
		this.description = description;
		this.group = group;
		this.isNew = isNew;
		this.groupHeader = groupHeader;
		this.requiresReload = requiresReload;
		this.args = args;
	}


	public String value() {
		return args.isEmpty() ? "" : args.get(0);
	}

	public List<String> options() {
		return args.isEmpty() ? List.of() : args.subList(1, args.size());
	}

	public static ConfigGuiEntry fromValue(String configId, ConfigurableValue<?> value) {
		List<String> args = new ArrayList<>();
		args.add(value.getAsString());
		args.addAll(value.getType().options());
		return new ConfigGuiEntry(
				configId,
				value.getKey(),
				value.getType(),
				displayName(value),
				value.getDescription(),
				value.getGroup(),
				value.isNew(),
				value.isGroupHeader(),
				value.requiresReload(),
				args
		);
	}

	public static ConfigGuiEntry fromHeader(String configId, ConfigEntry header) {
		return new ConfigGuiEntry(
				configId,
				header.getKey(),
				ConfigTypes.HEADER,
				displayName(header),
				header.getDescription(),
				header.getGroup(),
				false,
				false,
				false,
				List.of()
		);
	}

	public static ConfigGuiEntry fromDynamic(String configId, DynamicEntry dynamic) {
		return new ConfigGuiEntry(
				configId,
				dynamic.key(),
				dynamic.type(),
				dynamic.name(),
				dynamic.description(),
				dynamic.group(),
				false,
				false,
				false,
				dynamic.args()
		);
	}

	private static String displayName(ConfigEntry e) {
		return e.getName().isEmpty() ? e.getKey() : e.getName();
	}

	public List<String> encode() {
		List<String> out = new ArrayList<>();
		out.add(configId);
		out.add(key);
		out.add(type.id());
		out.add(name);
		out.add(description);
		out.add(group);
		out.add(String.valueOf(isNew));
		out.add(String.valueOf(groupHeader));
		out.add(String.valueOf(requiresReload));
		out.addAll(args);
		return out;
	}

	public static ConfigGuiEntry decode(List<String> data) throws Exception {
		Iterator<String> in = data.iterator();
		String configId = in.next();
		String key = in.next();
		ConfigType<?> type = ConfigTypes.fromId(in.next());
		String name = in.next();
		String description = in.next();
		String group = in.next();
		boolean isNew = Boolean.parseBoolean(in.next());
		boolean groupHeader = Boolean.parseBoolean(in.next());
		boolean requiresReload = Boolean.parseBoolean(in.next());
		List<String> args = new ArrayList<>();
		in.forEachRemaining(args::add);
		return new ConfigGuiEntry(
				configId,
				key,
				type,
				name,
				description,
				group,
				isNew,
				groupHeader,
				requiresReload,
				args
		);
	}
}