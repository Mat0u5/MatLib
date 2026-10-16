package net.mat0u5.matlib.config.entry;

public interface ConfigEntry {
	String getKey();
	String getName();
	String getDescription();
	String getGroup();
	boolean isNew();
	boolean isGroupHeader();
	boolean groupShowSidebar();
	boolean groupOpenByDefault();
	boolean isVisible();
}
