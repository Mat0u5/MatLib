package net.mat0u5.matlib.client.gui.config.entries.extra;

import net.mat0u5.matlib.client.gui.config.entries.StringListPopupConfigEntry;
import net.mat0u5.matlib.config.entry.ConfigGuiEntry;
import net.mat0u5.matlib.config.type.ConfigType;
import net.mat0u5.matlib.config.type.ConfigTypes;
import net.mat0u5.matlib.utils.other.TextUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StringListConfigEntry extends StringListPopupConfigEntry<String> {

    private List<String> allowedValues;

    public StringListConfigEntry(ConfigGuiEntry guiEntry, String value, String defaultValue, List<String> allowedValues) {
        super(guiEntry, value, defaultValue);
        this.allowedValues = allowedValues;
        forceReloadEntries();
    }

    @Override
    protected void reloadEntries(List<String> items) {
        if (entries != null) {
            entries.clear();
        }

        List<String> newList = new ArrayList<>();
        boolean errors = false;

        for (String entry : items) {
            if (entry.isEmpty()) continue;
            if (allowedValues != null && allowedValues.stream().noneMatch(entry::equalsIgnoreCase)) {
                setError(TextUtils.formatString("Invalid entry: '{}'", entry));
                errors = true;
                continue;
            }
            newList.add(entry.toLowerCase(Locale.ROOT));
        }

        entries = newList;
        if (!errors) {
            clearError();
        }
    }

    @Override
    protected void renderListEntry(GuiGraphicsExtractor context, String entry, int x, int y, int mouseX, int mouseY, float tickDelta) {

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
        return ConfigTypes.STRING_LIST;
    }
}
