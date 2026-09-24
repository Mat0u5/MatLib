package net.mat0u5.matlib.mixin.client;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.client.events.ClientSoundEvents;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

//? if <= 1.21.5
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if >= 1.21.6 {
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//?}

@Mixin(value = SoundManager.class, priority = 1)
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
public class SoundManagerMixin {
    //? if <= 1.21.5 {
    /*@Inject(method = "play(Lnet/minecraft/client/resources/sounds/SoundInstance;)V", at = @At("HEAD"))
    private void play(SoundInstance sound, CallbackInfo ci) {
        ClientSoundEvents.PLAY_SOUND.invoker().onPlay(sound);
    }
    *///?} else {
    @Inject(method = "play(Lnet/minecraft/client/resources/sounds/SoundInstance;)Lnet/minecraft/client/sounds/SoundEngine$PlayResult;", at = @At("HEAD"))
    private void play(SoundInstance sound, CallbackInfoReturnable<SoundEngine.PlayResult> cir) {
        ClientSoundEvents.PLAY_SOUND.invoker().onPlay(sound);
    }
    //?}
}
