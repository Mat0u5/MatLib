package net.mat0u5.matlib.client.gui.config.entries.main;

import net.mat0u5.matlib.client.gui.config.entries.NumberConfigEntry;
import net.mat0u5.matlib.config.entry.ConfigGuiEntry;
import net.mat0u5.matlib.config.type.ConfigType;
import net.mat0u5.matlib.config.type.ConfigTypes;

public class NullableIntegerConfigEntry extends NumberConfigEntry<Integer> {

    public NullableIntegerConfigEntry(ConfigGuiEntry guiEntry, Integer value, Integer defaultValue) {
        super(guiEntry, value, defaultValue);
    }

    public NullableIntegerConfigEntry(ConfigGuiEntry guiEntry, Integer value, Integer defaultValue, Integer minValue, Integer maxValue) {
        super(guiEntry, value, defaultValue, minValue, maxValue);
    }

    @Override
    protected Integer parseValue(String text) throws NumberFormatException {
        if (text.isEmpty()) return null;
        return Integer.parseInt(text);
    }

    @Override
    protected boolean isValueInRange(Integer value) {
        if (value == null) return true;
        if (minValue != null && value < minValue) return false;
        if (maxValue != null && value > maxValue) return false;
        return true;
    }

    @Override
    protected boolean isValidType(Object value) {
        return value == null || value instanceof Integer;
    }

    @Override
    protected Integer castValue(Object value) {
        if (value == null) return null;
        return (Integer) value;
    }

    @Override
    public ConfigType<?> getValueType() {
        return ConfigTypes.NULLABLE_INTEGER;
    }
}