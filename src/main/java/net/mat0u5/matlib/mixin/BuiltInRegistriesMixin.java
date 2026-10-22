package net.mat0u5.matlib.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.MatLib;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if <= 26.3 {
/*import net.minecraft.core.registries.BuiltInRegistries;
@Mixin(BuiltInRegistries.class)
*///?} else {
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltinRegistryBuilder;
@Mixin(BuiltinRegistryBuilder.class)
//?}
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public class BuiltInRegistriesMixin {
//? if <= 26.3 {
    /*/^*
     * fabric && <= 1.20.5 has this logic in {@link MatLib#onInitialize()}
     ^/
    @Inject(method = "freeze", at = @At("HEAD"))
    private static void registerPreFreeze(CallbackInfo ci) {
		//? fabric && <= 1.20.5 {
		/^if (MatLib.platform().isModLoaded("fabric-api")) {
			return;
		}
		^///?} else {
        MatLib.onRegister();
		//?}
    }
*///?} else {
	@Inject(method = "createContents", at = @At("HEAD"))
	private void registerPreFreeze(CallbackInfo ci) {
		BuiltinRegistryBuilder bulider = (BuiltinRegistryBuilder) (Object) this;
		if (!bulider.registry.key().identifier().equals(Registries.ROOT_REGISTRY_NAME)) { // Filters out ClientRegistries.ROOT_REGISTRY_NAME
			return;
		}
		MatLib.onRegister();
	}
//?}
}