package net.mat0u5.matlib.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixin;

//? if <= 1.20 {
/*import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
*///?}
//? if <= 1.20.3 {
/*import net.mat0u5.lifeseries.network.NetworkHandlerCommon;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
*///?}

//? if <= 1.20 {
/*import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
@Mixin(value = ServerGamePacketListenerImpl.class, priority = 1)
*///?} else {
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
@Mixin(value = ServerCommonPacketListenerImpl.class, priority = 1)
//?}
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public class ServerCommonPacketListenerImplMixin {
	//? if <= 1.20 {
    /*@Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
    private void onHandlePayload(ServerboundCustomPayloadPacket packet, CallbackInfo ci) {
        if ((Object)this instanceof ServerGamePacketListenerImpl gameListener) {
            Identifier id = packet.getIdentifier();
        	var payloadReaders = NetworkHandlerCommon.getPayloadReaders();
            if (payloadReaders.containsKey(id)) {
                CustomPacketPayload payload = payloadReaders.get(id).apply(packet.getData());
                NetworkHandlerServer.onCustomPayload(payload, gameListener.player);
            }
        }
    }
    *///?} else if <= 1.20.3 {
    /*@Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
    private void onHandlePayload(ServerboundCustomPayloadPacket packet, CallbackInfo ci) {
        if ((Object)this instanceof ServerGamePacketListenerImpl gameListener) {
            NetworkHandlerServer.onCustomPayload(packet.payload(), gameListener.player);
        }
    }
    *///?}
}
