package net.mat0u5.matlib.client.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.utils.other.OtherUtils;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;

//? if >= 1.20.3 {
import net.mat0u5.matlib.client.utils.TickRateSoundScaling;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.util.Mth;
import net.minecraft.world.TickRateManager;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//?}

@Mixin(value = SoundEngine.class, priority = 1)
@MixinEnvironment(value = "client", type = MixinEnvironment.Env.CLIENT)
public class SoundEngineMixin {
	//? if >= 1.20.3 {
	@Inject(method = "calculatePitch", at = @At("HEAD"), cancellable = true)
	private void scalePitchWithTickRate(SoundInstance sound, CallbackInfoReturnable<Float> cir) {
		if (!TickRateSoundScaling.shouldScaleSound(sound)) return;
		Minecraft client = Minecraft.getInstance();
		if (client.level == null) return;
		TickRateManager tickManager = client.level.tickRateManager();
		if (tickManager.tickrate() != 20) {
			cir.setReturnValue(OtherUtils.clamp(sound.getPitch(), 0.5F, 2.0F) * (tickManager.tickrate() / 20.0f));
		}
	}
	//?}
}
