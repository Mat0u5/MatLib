package net.mat0u5.matlib.client.gui.config.entries.extra;

import net.mat0u5.matlib.client.gui.config.entries.StringListPopupConfigEntry;
import net.mat0u5.matlib.config.entry.ConfigGuiEntry;
import net.mat0u5.matlib.config.type.ConfigType;
import net.mat0u5.matlib.config.type.ConfigTypes;
import net.mat0u5.matlib.utils.other.RegistryUtils;
import net.mat0u5.matlib.utils.other.TextUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockListConfigEntry extends StringListPopupConfigEntry<Block> {
    public BlockListConfigEntry(ConfigGuiEntry guiEntry, String value, String defaultValue) {
        super(guiEntry, value, defaultValue);
        reloadEntriesRaw(value);
    }

    @Override
    protected void reloadEntries(List<String> items) {
        if (entries != null) {
            entries.clear();
        }

        List<Block> newList = new ArrayList<>();
        boolean errors = false;

        for (String blockId : items) {
            if (blockId.isEmpty()) continue;
            try {
                newList.add(RegistryUtils.resolveRegistryEntry(BuiltInRegistries.BLOCK, blockId));
            } catch (Exception e) {
                setError(TextUtils.formatString("Invalid block: '{}'", blockId));
                errors = true;
            }
        }

        entries = newList;
        if (!errors) {
            clearError();
        }
    }

    @Override
    protected void renderListEntry(GuiGraphicsExtractor context, Block block, int x, int y, int mouseX, int mouseY, float tickDelta) {
        context.item(block.asItem().getDefaultInstance(), x, y);
    }

    @Override
    public boolean hasCustomErrors() {
        return true;
    }

    @Override
    public ConfigType<?> getValueType() {
        return ConfigTypes.BLOCK_LIST;
    }
}
