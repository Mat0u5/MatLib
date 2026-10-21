package net.mat0u5.matlib.client.network;

import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.client.events.ClientNetworkEvents;
import net.mat0u5.matlib.network.packets.*;
import net.mat0u5.matlib.network.packets.simple.SimplePacket;
import net.mat0u5.matlib.utils.interfaces.SimplePacketPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.Objects;
//? if <= 1.20.3 {
/*import net.minecraft.network.FriendlyByteBuf;
 *///?}
//? if <= 1.20 {
/*import io.netty.buffer.Unpooled;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
*///?} else {
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
//?}

public class NetworkHandlerClient {

	public static boolean onCustomPayload(CustomPacketPayload customPacketPayload) {
		//~ if > 1.20.3 'customPacketPayload.id()' -> 'customPacketPayload.type().id()' {
		Identifier id = customPacketPayload.type().id();
		//~}
		MatLib.DEBUG.network.packetsReceived.log("[SERVER -> CLIENT] Received {}", id);

		Minecraft client = Minecraft.getInstance();
		boolean received = false;
		if (customPacketPayload instanceof SimplePacketPayload simplePacketPayload) {
			//String modId = simplePacketPayload.modId();
			String packetName = simplePacketPayload.name();
			SimplePacket<?, ?> packet = SimplePacket.registeredPackets.get(packetName);
			received = true;
			if (packet != null) {
				client.execute(() -> packet.receiveClient(customPacketPayload));
			}
			else {
				MatLib.DEBUG.network.unhandledPackets.warn("[SERVER -> CLIENT] Received unregistered SimplePacketPayload: {}", packetName);
			}
		}
		else {
			received = ClientNetworkEvents.RECEIVE_CUSTOM_PACKET.invoker().onReceivePacket(customPacketPayload);
		}

		return received;
	}

	public static void send(CustomPacketPayload payload) {
		send(payload, false);
	}

	private static void send(CustomPacketPayload payload, boolean resent) {
		Objects.requireNonNull(payload, "Payload cannot be null");
		//~ if > 1.20.3 'payload.id()' -> 'payload.type().id()' {
		Identifier id = payload.type().id();
		//~}

		var connection = Minecraft.getInstance().getConnection();
		if (connection != null) {
			MatLib.DEBUG.network.packetsSent.log("[CLIENT -> SERVER] Sending {}", id);
			//? if <= 1.20 {
            /*FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            payload.write(buf);
            connection.send(new ServerboundCustomPayloadPacket(payload.id(), buf));
            *///?} else {
			connection.send(new ServerboundCustomPayloadPacket(payload));
			//?}
		}
		else if (!resent) {
			Minecraft.getInstance().execute(() -> send(payload, true));
		}
		else {
			MatLib.DEBUG.network.droppedPackets.warn("[CLIENT -> SERVER] Dropped {}, no connection", id);
		}
	}
}
