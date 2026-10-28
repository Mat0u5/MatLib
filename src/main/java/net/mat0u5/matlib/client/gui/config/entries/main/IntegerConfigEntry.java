package net.mat0u5.matlib.client.gui.config.entries.main;

import net.mat0u5.matlib.client.gui.config.entries.NumberConfigEntry;
import net.mat0u5.matlib.config.entry.ConfigGuiEntry;
import net.mat0u5.matlib.config.type.ConfigType;
import net.mat0u5.matlib.config.type.ConfigTypes;

public class IntegerConfigEntry extends NumberConfigEntry<Integer> {

    public IntegerConfigEntry(ConfigGuiEntry guiEntry, int value, int defaultValue) {
        super(guiEntry, value, defaultValue);
    }

    public IntegerConfigEntry(ConfigGuiEntry guiEntry, int value, int defaultValue, Integer minValue, Integer maxValue) {
        super(guiEntry, value, defaultValue, minValue, maxValue);
    }

    @Override
    protected Integer parseValue(String text) throws NumberFormatException {
        return Integer.parseInt(text);
    }

    @Override
    protected boolean isValueInRange(Integer value) {
        if (minValue == null || maxValue == null) return true;
        return value >= minValue && value <= maxValue;
    }

    @Override
    protected boolean isValidType(Object value) {
        return value instanceof Integer;
    }

    @Override
    protected Integer castValue(Object value) {
        return (Integer) value;
    }

    @Override
    public ConfigType<?> getValueType() {
        return ConfigTypes.INTEGER;
    }
}