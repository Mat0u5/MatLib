package net.mat0u5.matlib.client.gui.config.entries.extra;

import net.mat0u5.matlib.client.gui.config.entries.StringListPopupConfigEntry;
import net.mat0u5.matlib.config.entry.ConfigGuiEntry;
import net.mat0u5.matlib.config.type.ConfigType;
import net.mat0u5.matlib.config.type.ConfigTypes;
import net.mat0u5.matlib.utils.other.TextUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IntegerListConfigEntry extends StringListPopupConfigEntry<Integer> {

    private final List<Integer> allowedValues;
    private Integer minValue;
    private Integer maxValue;

    public IntegerListConfigEntry(ConfigGuiEntry guiEntry, String value, String defaultValue, List<String> allowedValues) {
        super(guiEntry, value, defaultValue);
        this.allowedValues = allowedValues == null ? null : allowedValues.stream()
                .map(IntegerListConfigEntry::parseValueOrNull)
                .filter(Objects::nonNull)
                .toList();
        if (guiEntry.isCustomBounded) {
            if (guiEntry.args.size() >= 2) this.minValue = parseValueOrNull(guiEntry.args.get(1));
            if (guiEntry.args.size() >= 3) this.maxValue = parseValueOrNull(guiEntry.args.get(2));
        }
        forceReloadEntries();
    }

    private static Integer parseValueOrNull(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (Exception ignored) {}
        return null;
    }

    private boolean isValueInRange(int value) {
        if (minValue != null && value < minValue) return false;
        if (maxValue != null && value > maxValue) return false;
        return true;
    }

    private String getRangeError(String entry) {
        if (minValue == null) {
            return TextUtils.formatString("'{}' must be <= {}", entry, maxValue);
        }
        if (maxValue == null) {
            return TextUtils.formatString("'{}' must be >= {}", entry, minValue);
        }
        return TextUtils.formatString("'{}' must be between {} and {}", entry, minValue, maxValue);
    }

    @Override
    protected void reloadEntries(List<String> items) {
        if (entries != null) {
            entries.clear();
        }

        List<Integer> newList = new ArrayList<>();
        String error = null;

        for (String entry : items) {
            if (entry.isEmpty()) continue;
            Integer number = parseValueOrNull(entry);
            if (number == null) {
                error = TextUtils.formatString("Invalid number: '{}'", entry);
                continue;
            }
            if (allowedValues != null && !allowedValues.contains(number)) {
                error = TextUtils.formatString("Invalid entry: '{}'", entry);
                continue;
            }
            if (!isValueInRange(number)) {
                error = getRangeError(entry);
                continue;
            }
            newList.add(number);
        }

        entries = newList;
        if (error != null) {
            setError(error);
        }
        else {
            clearError();
        }
    }

    @Override
    protected void renderListEntry(GuiGraphicsExtractor context, Integer entry, int x, int y, int mouseX, int mouseY, float tickDelta) {

    }

    @Override
    public boolean shouldShowPopup() {
        return false;
    }

    @Override
    public boolean hasCustomErrors() {
        return true;
    }

    @Override
    public ConfigType<?> getValueType() {
        return ConfigTypes.INTEGER_LIST;
    }
}
