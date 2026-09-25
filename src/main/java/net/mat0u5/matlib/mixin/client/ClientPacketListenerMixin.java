package net.mat0u5.matlib.mixin.client;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.client.events.ClientLocalPlayerEvents;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientPacketListener.class, priority = 1)
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
public class ClientPacketListenerMixin {
	@Inject(method = "handleLogin", at = @At("RETURN"))
	private void handleServerPlayReady(ClientboundLoginPacket packet, CallbackInfo ci) {
		ClientLocalPlayerEvents.JOIN.invoker().onJoin(packet);
	}
}
