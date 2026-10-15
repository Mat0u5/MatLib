package net.mat0u5.matlib.config.entry;

import net.mat0u5.matlib.config.type.ConfigType;

import java.util.List;

public record DynamicEntry(ConfigType type, String key, String name, String description, String group, List<String> args) {}
