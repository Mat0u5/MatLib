package net.mat0u5.matlib.client.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.client.MatLibClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? fabric && <= 1.20.5
//import net.mat0u5.matlib.MatLib;

//? if <= 26.3 {
/*import net.minecraft.core.registries.BuiltInRegistries;
@Mixin(BuiltInRegistries.class)
*///?} else {
import net.minecraft.client.core.registries.ClientRegistries;
import net.minecraft.core.registries.BuiltinRegistryBuilder;
@Mixin(BuiltinRegistryBuilder.class)
//?}
@MixinEnvironment(value = "client", type = MixinEnvironment.Env.CLIENT)
public class BuiltInRegistriesMixin {
//? if <= 26.3 {
    /*/^*
     * fabric && <= 1.20.5 has this logic in {@link MatLibClient#onInitializeClient()}
     ^/
    @Inject(method = "freeze", at = @At("HEAD"))
    private static void registerPreFreeze(CallbackInfo ci) {
		//? fabric && <= 1.20.5 {
		/^if (MatLib.platform().isModLoaded("fabric-api")) {
			return;
		}
		^///?} else {
        MatLibClient.onRegister();
		//?}
    }
*///?} else {
	@Inject(method = "createContents", at = @At("HEAD"))
	private void registerPreFreeze(CallbackInfo ci) {
		BuiltinRegistryBuilder bulider = (BuiltinRegistryBuilder) (Object) this;
		if (!bulider.registry.key().identifier().equals(ClientRegistries.ROOT_REGISTRY_NAME)) { // Filters out Registries.ROOT_REGISTRY_NAME
			return;
		}
		MatLibClient.onRegister();
	}
//?}
}