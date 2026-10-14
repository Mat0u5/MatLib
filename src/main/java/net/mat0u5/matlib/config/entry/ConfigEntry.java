package net.mat0u5.matlib.config.entry;

public interface ConfigEntry {
	String getKey();
	String getName();
	String getDescription();
	String getGroup();
	boolean isVisible();
}
