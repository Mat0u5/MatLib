package net.mat0u5.matlib.client.gui.config.entries.main;

import net.mat0u5.matlib.client.gui.config.entries.NumberConfigEntry;
import net.mat0u5.matlib.config.entry.ConfigGuiEntry;
import net.mat0u5.matlib.config.type.ConfigType;
import net.mat0u5.matlib.config.type.ConfigTypes;

public class DoubleConfigEntry extends NumberConfigEntry<Double> {

    public DoubleConfigEntry(ConfigGuiEntry guiEntry, double value, double defaultValue) {
        super(guiEntry, value, defaultValue);
    }

    public DoubleConfigEntry(ConfigGuiEntry guiEntry, double value, double defaultValue, Double minValue, Double maxValue) {
        super(guiEntry, value, defaultValue, minValue, maxValue);
    }

    @Override
    protected Double parseValue(String text) throws NumberFormatException {
        return Double.parseDouble(text);
    }

    @Override
    protected boolean isValueInRange(Double value) {
        if (minValue != null && value < minValue) return false;
        if (maxValue != null && value > maxValue) return false;
        return true;
    }

    @Override
    protected boolean isValidType(Object value) {
        return value instanceof Double;
    }

    @Override
    protected Double castValue(Object value) {
        return (Double) value;
    }

    @Override
    public ConfigType<?> getValueType() {
        return ConfigTypes.DOUBLE;
    }
}