package net.mat0u5.matlib.mixin.client;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.client.events.ClientRegistryEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(Options.class)
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
public class OptionsMixin {
    @Shadow
    @Final
    @Mutable
    public KeyMapping[] keyMappings;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initKeysInConstructor(CallbackInfo ci) {

        List<KeyMapping> allKeys = new ArrayList<>(Arrays.asList(this.keyMappings));
        ClientRegistryEvents.KEYBIND.invoker().modifyKeymappings(allKeys);

        //? if <= 1.21.6 {
        /*int currentCategory = 20;
        for (KeyMapping mapping : allKeys) {
            String category = mapping.getCategory();
            if (!KeyMapping.CATEGORY_SORT_ORDER.containsKey(category)) {
                KeyMapping.CATEGORY_SORT_ORDER.put(category, currentCategory++);
            }
        }
        *///?}

        this.keyMappings = allKeys.toArray(new KeyMapping[0]);
    }

    @Inject(method = "load", at = @At("HEAD"))
    private void ensureKeysBeforeLoad(CallbackInfo ci) {
        List<KeyMapping> allKeys = new ArrayList<>(Arrays.asList(this.keyMappings));
        ClientRegistryEvents.KEYBIND.invoker().modifyKeymappings(allKeys);
        this.keyMappings = allKeys.toArray(new KeyMapping[0]);
    }
}