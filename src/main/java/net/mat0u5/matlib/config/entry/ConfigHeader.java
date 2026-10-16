package net.mat0u5.matlib.config.entry;

import net.mat0u5.matlib.config.GuiConfig;

import java.util.function.BooleanSupplier;

public class ConfigHeader implements ConfigEntry {
	private final String key;
	private String name;
	private String description = "";
	private String group = "";
	private boolean isGroupHeader = false;
	private boolean groupShowSidebar = true;
	private boolean groupOpenByDefault = false;
	private BooleanSupplier visibleIf = () -> true;

	private ConfigHeader(String key, String name) {
		this.key = key;
		this.name = name;
	}

	public static ConfigHeader register(GuiConfig config, String key, String name) {
		ConfigHeader header = new ConfigHeader(key, name);
		config.addEntry(header);
		return header;
	}

	public ConfigHeader group(String group) {
		this.group = group;
		return this;
	}

	public ConfigHeader groupHeader(boolean isGroupHeader) {
		this.isGroupHeader = isGroupHeader;
		return this;
	}

	public ConfigHeader groupShowSidebar(boolean groupShowSidebar) {
		this.groupShowSidebar = groupShowSidebar;
		return this;
	}

	public ConfigHeader groupOpenByDefault(boolean groupOpenByDefault) {
		this.groupOpenByDefault = groupOpenByDefault;
		return this;
	}

	public ConfigHeader description(String description) {
		this.description = description;
		return this;
	}

	public ConfigHeader visibleIf(BooleanSupplier supplier) {
		this.visibleIf = supplier;
		return this;
	}

	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public String getGroup() {
		return this.group;
	}

	@Override
	public boolean isGroupHeader() {
		return this.isGroupHeader;
	}

	@Override
	public boolean groupShowSidebar() {
		return this.groupShowSidebar;
	}

	@Override
	public boolean groupOpenByDefault() {
		return this.groupOpenByDefault;
	}

	@Override
	public boolean isVisible() {
		return this.visibleIf.getAsBoolean();
	}
}