package net.mat0u5.matlib.client.gui.config.entries.main;

import net.mat0u5.matlib.config.entry.ConfigGuiEntry;
import net.mat0u5.matlib.client.gui.config.entries.NumberConfigEntry;
import net.mat0u5.matlib.config.type.*;

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
        if (minValue == null || maxValue == null) return true;
        if (value == null) return false;
        return value >= minValue && value <= maxValue;
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