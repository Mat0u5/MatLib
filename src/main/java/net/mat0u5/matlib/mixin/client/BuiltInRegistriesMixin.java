package net.mat0u5.matlib.mixin.client;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.client.MatLibClient;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? fabric && <= 1.20.5
import net.mat0u5.matlib.MatLib;

@Mixin(BuiltInRegistries.class)
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
public class BuiltInRegistriesMixin {
    /**
     * fabric && <= 1.20.5 has this logic in {@link MatLibClient#onInitializeClient()} ()}
     */
    @Inject(method = "freeze", at = @At("HEAD"))
    private static void registerPreFreeze(CallbackInfo ci) {
		//? fabric && <= 1.20.5 {
		/*if (MatLib.platform().isModLoaded("fabric-api")) {
			return;
		}
		*///?} else {
        MatLibClient.onRegister();
		//?}
    }
}