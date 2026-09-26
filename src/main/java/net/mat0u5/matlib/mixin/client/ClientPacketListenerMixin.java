package net.mat0u5.matlib.mixin.client;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.client.events.ClientLocalPlayerEvents;
import net.mat0u5.matlib.client.network.NetworkHandlerClient;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if <= 1.20 {
/*import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.resources.Identifier;
import net.mat0u5.matlib.network.NetworkHandlerCommon;
*///?}

@Mixin(value = ClientPacketListener.class, priority = 1)
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
public class ClientPacketListenerMixin {
	@Inject(method = "handleLogin", at = @At("RETURN"))
	private void handleServerPlayReady(ClientboundLoginPacket packet, CallbackInfo ci) {
		ClientLocalPlayerEvents.JOIN.invoker().onJoin(packet);
	}

//? if fabric || forge {
	//? if <= 1.20 {
    /*@Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
    private void onHandlePayload(ClientboundCustomPayloadPacket clientboundCustomPayloadPacket, CallbackInfo ci) {
        Identifier id = clientboundCustomPayloadPacket.getIdentifier();
		var payloadReaders = NetworkHandlerCommon.getPayloadReaders();
        if (payloadReaders.containsKey(id)) {
            CustomPacketPayload payload = payloadReaders.get(id).apply(clientboundCustomPayloadPacket.getData());
            if (NetworkHandlerClient.onCustomPayload(payload)) {
                ci.cancel();
            }
        }
    }
    *///?} else {
	@Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
	private void onHandlePayload(CustomPacketPayload payload, CallbackInfo ci) {
		if (NetworkHandlerClient.onCustomPayload(payload)) {
			ci.cancel();
		}
	}
	//?}
//?}
}
