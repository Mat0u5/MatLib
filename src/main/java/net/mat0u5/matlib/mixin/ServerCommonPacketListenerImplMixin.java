package net.mat0u5.matlib.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.entity.fakeplayer.FakeClientConnection;
import net.mat0u5.matlib.utils.other.RegistrySyncUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if <= 1.21.5
//import net.minecraft.network.PacketSendListener;
//? if >= 1.21.6
import io.netty.channel.ChannelFutureListener;

//? if <= 1.20 {
/*import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
*///?}
//? if <= 1.20.3 {
/*import net.mat0u5.matlib.network.NetworkHandlerCommon;
import net.mat0u5.matlib.network.NetworkHandlerServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
*///?}
//? if <= 1.20 {
/*import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
*///?} else if <= 1.20.3 {
/*import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
*///?}

//? if <= 1.20 {
/*@Mixin(value = ServerGamePacketListenerImpl.class, priority = 1)
*///?} else {
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
@Mixin(value = ServerCommonPacketListenerImpl.class, priority = 1)
//?}
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public class ServerCommonPacketListenerImplMixin {
	@Final
	@Shadow
	protected Connection connection;

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

	@Inject(method = "send(Lnet/minecraft/network/protocol/Packet;)V", at = @At("HEAD"), cancellable = true)
	private void dropFakePlayerPackets(Packet<?> packet, CallbackInfo ci) {
		if (connection instanceof FakeClientConnection) {
			ci.cancel();
		}
	}

	//? if <= 1.20 {
	/*@WrapOperation(
			method = "send(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketSendListener;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;send(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketSendListener;)V")
	)
	private void dropWrappedFakePlayerPackets(Connection instance, Packet<?> packet, PacketSendListener packetSendListener, Operation<Void> original) {
		if (connection instanceof FakeClientConnection) return;
		original.call(instance, packet, packetSendListener);
	}
	*///?} else if <= 1.21.5 {
	/*@WrapOperation(
			method = "send(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketSendListener;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;send(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketSendListener;Z)V")
	)
	private void dropWrappedFakePlayerPackets(Connection instance, Packet<?> packet, PacketSendListener callbacks, boolean flush, Operation<Void> original) {
		if (connection instanceof FakeClientConnection) return;
		original.call(instance, packet, callbacks, flush);
	}
	*///?} else {
	@WrapOperation(
			method = "send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;Z)V")
	)
	private void dropWrappedFakePlayerPackets(Connection instance, Packet packet, ChannelFutureListener channelFutureListener, boolean b, Operation<Void> original) {
		if (connection instanceof FakeClientConnection) return;
		original.call(instance, packet, channelFutureListener, b);
	}
	//?}

	@Inject(method = "disconnect(Lnet/minecraft/network/chat/Component;)V", at = @At("HEAD"), cancellable = true)
	private void preventDisconnect(Component reason, CallbackInfo ci) {
		if (RegistrySyncUtils.shouldPreventDisconnect(reason)) {
			ci.cancel();
			return;
		}
		if (connection instanceof FakeClientConnection) {
			ci.cancel();
		}
	}
}
