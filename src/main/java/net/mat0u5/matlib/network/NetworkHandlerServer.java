package net.mat0u5.matlib.network;

import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.events.server.ServerNetworkEvents;
import net.mat0u5.matlib.mixin.ServerLoginPacketListenerImplAccessor;
import net.mat0u5.matlib.network.packets.simple.SimplePacket;
import net.mat0u5.matlib.utils.interfaces.SimplePacketPayload;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static net.mat0u5.matlib.MatLib.server;

//? neoforge && <= 1.20.3 {
//import net.mat0u5.matlib.network.packets.HandshakePayload;
//?} else if neoforge {
//import net.neoforged.neoforge.network.registration.NetworkRegistry;
//?}
//? if <= 1.20 {
/*import io.netty.buffer.Unpooled;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
*///?} else {
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.login.custom.DiscardedQueryPayload;
//?}

public class NetworkHandlerServer {
	private static final Set<Integer> preLoginTransactionIds = ConcurrentHashMap.newKeySet();

	public static void onCustomPayload(CustomPacketPayload customPacketPayload, Player player) {
		if (player instanceof ServerPlayer serverPlayer) {
			onCustomPayload(customPacketPayload, serverPlayer);
		}
	}

	public static void onCustomPayload(CustomPacketPayload customPacketPayload, ServerPlayer player) {
		//~ if > 1.20.3 'customPacketPayload.id()' -> 'customPacketPayload.type().id()' {
		Identifier id = customPacketPayload.type().id();
		//~}
		MatLib.DEBUG.network.packetsReceived.log("[{} -> PACKET_SERVER] Received {}", player, id);

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
			else {
				MatLib.DEBUG.network.unhandledPackets.warn("[{} -> PACKET_SERVER] Received unregistered SimplePacketPayload: {}", player, packetName);
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
        /*//? if <= 1.20.3 {
		/^String packetModId = id.getNamespace();
		if (!packetModId.equals("minecraft") && id != HandshakePayload.ID) {
           if (!wasHandshakeSuccessful(player, packetModId)) {
               MatLib.DEBUG.network.droppedPackets.log("[PACKET_SERVER -> {}] Dropped {}, client has no handshake for {}", player, id, packetModId);
               return;
           }
       }
		^///?} else {
		if (!NetworkRegistry.hasChannel(player.connection, id)) {
			MatLib.DEBUG.network.droppedPackets.log("[PACKET_SERVER -> {}] Dropped {}, client has no channel for it", player, id);
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
		MatLib.DEBUG.network.packetsSent.log("[PACKET_SERVER -> {}] Sending {}", player, id);
	}

	public static void sendPreLoginPacket(ServerLoginPacketListenerImpl handler, int transactionId, Identifier id) {
		Objects.requireNonNull(handler, "Login handler cannot be null");
		Objects.requireNonNull(id, "Packet id cannot be null");

		// Must be known before the answer arrives, the answer payload is decoded off the login handler
		preLoginTransactionIds.add(transactionId);

		Connection connection = ((ServerLoginPacketListenerImplAccessor) handler).getConnection();
		//? if <= 1.20 {
		/*connection.send(new ClientboundCustomQueryPacket(transactionId, id, new FriendlyByteBuf(Unpooled.buffer())));
		*///?} else {
		connection.send(new ClientboundCustomQueryPacket(transactionId, new DiscardedQueryPayload(id)));
		//?}
	}

	public static boolean isPreLoginTransaction(int transactionId) {
		return preLoginTransactionIds.contains(transactionId);
	}

	public static void onPreLoginAnswer(ServerLoginPacketListenerImpl handler, Identifier id, FriendlyByteBuf data) {
		ServerNetworkEvents.RECEIVE_PRELOGIN_ANSWER.invoker().onReceivePreLogin(handler, id, data);
	}

	public static boolean wasHandshakeSuccessful(ServerPlayer player, String modId) {
		return MatLibNetworkHandlerServer.wasHandshakeSuccessful(player, modId);
	}

	public static boolean wasHandshakeSuccessful(UUID uuid, String modId) {
		return MatLibNetworkHandlerServer.wasHandshakeSuccessful(uuid, modId);
	}

	public static boolean wasPreLoginHandshakeSuccessful(UUID uuid, String modId) {
		return MatLibNetworkHandlerServer.wasPreLoginHandshakeSuccessful(uuid, modId);
	}
}
