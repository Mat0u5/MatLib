package net.mat0u5.matlib.network;

import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.events.server.ServerNetworkEvents;
import net.mat0u5.matlib.network.packets.simple.SimplePacket;
import net.mat0u5.matlib.utils.interfaces.SimplePacketPayload;
import net.mat0u5.matlib.utils.other.TextUtils;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

import static net.mat0u5.matlib.MatLib.server;

//? if neoforge && > 1.20.3
//import net.neoforged.neoforge.network.registration.NetworkRegistry;

//? if <= 1.20.3 {
/*import net.minecraft.network.FriendlyByteBuf;
 *///?}
 
//? if <= 1.20 {
/*import io.netty.buffer.Unpooled;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
*///?} else {
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
//?}

public class NetworkHandlerServer {

	public static void onCustomPayload(CustomPacketPayload customPacketPayload, Player player) {
		if (player instanceof ServerPlayer serverPlayer) {
			onCustomPayload(customPacketPayload, serverPlayer);
		}
	}

	public static void onCustomPayload(CustomPacketPayload customPacketPayload, ServerPlayer player) {
		//~ if > 1.20.3 'customPacketPayload.id()' -> 'customPacketPayload.type().id()' {
		Identifier id = customPacketPayload.type().id();
		//~}
		if (MatLib.DEBUG) MatLib.LOGGER.info(TextUtils.formatString("[{} -> PACKET_SERVER] Received {}", player, id.toString()));

		if (server != null) {
			server.execute(() -> onCustomPayloadSync(customPacketPayload, player, id));
		}
	}

	private static void onCustomPayloadSync(CustomPacketPayload customPacketPayload, ServerPlayer player, Identifier id) {
		if (player.hasDisconnected()) {
			return;
		}

		if (customPacketPayload instanceof SimplePacketPayload simplePacketPayload) {
			//String modId = simplePacketPayload.modId();
			String packetName = simplePacketPayload.name();
			SimplePacket<?, ?> packet = SimplePacket.registeredPackets.get(packetName);
			if (packet != null) {
				packet.receiveServer(player, customPacketPayload);
			}
		}
		else {
			ServerNetworkEvents.RECEIVE_CUSTOM_PACKET.invoker().onReceivePacket(customPacketPayload, player);
		}
	}


	public static void sendPacket(ServerPlayer player, CustomPacketPayload payload) {
		Objects.requireNonNull(player, "Server player cannot be null");
		Objects.requireNonNull(payload, "Payload cannot be null");

		//~ if > 1.20.3 'payload.id()' -> 'payload.type().id()' {
		Identifier id = payload.type().id();
		//~}

	//? if neoforge {
        /*//? if <= 1.20.3 {//TODO
		/^if (!wasHandshakeSuccessful(player) && id != HandshakePayload.ID) {
			return;
		}
		^///?} else {
		if (!NetworkRegistry.hasChannel(player.connection, id)) {
			return;
		}
		//?}
	*///?}

		//? if <= 1.20 {
        /*FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        payload.write(buf);
        player.connection.send(new ClientboundCustomPayloadPacket(payload.id(), buf));
        *///?} else {
		player.connection.send(new ClientboundCustomPayloadPacket(payload));
		//?}
		if (MatLib.DEBUG) MatLib.LOGGER.info(TextUtils.formatString("[PACKET_SERVER -> {}] Sending {}", player, id.toString()));
	}
}
