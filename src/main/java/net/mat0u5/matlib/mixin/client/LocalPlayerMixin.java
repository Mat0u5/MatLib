package net.mat0u5.matlib.mixin.client;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.client.events.ClientLocalPlayerEvents;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LocalPlayer.class, priority = 1)
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
public abstract class LocalPlayerMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickHead(CallbackInfo ci) {
        ClientLocalPlayerEvents.START_TICK.invoker().onTickStart((LocalPlayer) (Object) this);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tickTail(CallbackInfo ci) {
        ClientLocalPlayerEvents.END_TICK.invoker().onTickEnd((LocalPlayer) (Object) this);
    }
}
