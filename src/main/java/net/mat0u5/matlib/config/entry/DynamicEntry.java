package net.mat0u5.matlib.config.entry;

import java.util.List;

public record DynamicEntry(String typeId, String key, String name, String description, String group, List<String> args) {}
