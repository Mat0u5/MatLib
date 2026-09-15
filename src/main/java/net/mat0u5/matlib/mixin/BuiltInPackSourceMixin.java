package net.mat0u5.matlib.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.events.server.ServerPackSourceEvents;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.ServerPacksSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(BuiltInPackSource.class)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public class BuiltInPackSourceMixin {
    @Inject(method = "loadPacks", at = @At("RETURN"))
    private void addBuiltInResourcepacks(Consumer<Pack> consumer, CallbackInfo ci) {
        if ((Object) this instanceof ServerPacksSource) {
            ServerPackSourceEvents.LOAD_PACK.invoker().onLoad(consumer);
        }
    }
}