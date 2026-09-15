package net.mat0u5.matlib.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.events.common.CommonRegistryEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltInRegistries.class)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public class BuiltInRegistriesMixin {
    /**
     * fabric && <= 1.20.5 has this logic in {@link MatLib#onInitialize()}
     */
    @Inject(method = "freeze", at = @At("HEAD"))
    private static void registerPreFreeze(CallbackInfo ci) {
		//? fabric && <= 1.20.5 {
		/*if (MatLib.platform().isModLoaded("fabric-api")) {
			return;
		}
		*///?} else {
        MatLib.onRegister();
        CommonRegistryEvents.PRE_FREEZE.invoker().onPreFreeze();
		//?}
    }
}