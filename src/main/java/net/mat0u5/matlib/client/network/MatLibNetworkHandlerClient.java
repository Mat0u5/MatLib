package net.mat0u5.matlib.client.network;

import com.google.auto.service.AutoService;
import io.netty.buffer.Unpooled;
import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.client.config.ClientConfigManager;
import net.mat0u5.matlib.client.events.ClientNetworkEvents;
import net.mat0u5.matlib.client.services.RegistrableClient;
import net.mat0u5.matlib.client.utils.ClientUtils;
import net.mat0u5.matlib.client.utils.SharedClientInfo;
import net.mat0u5.matlib.events.OptionalEventReturn;
import net.mat0u5.matlib.network.MatLibNetworkHandlerCommon;
import net.mat0u5.matlib.network.MatLibNetworkHandlerServer;
import net.mat0u5.matlib.network.MatLibSimplePackets;
import net.mat0u5.matlib.network.packets.HandshakePayload;
import net.mat0u5.matlib.utils.other.IdentifierHelper;
import net.mat0u5.matlib.utils.other.TextUtils;
import net.mat0u5.matlib.utils.other.VersionCompatibility;
import net.mat0u5.matlib.utils.other.VersionTrackedMods;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import net.minecraft.resources.Identifier;

import java.util.List;

//? if <= 1.20 {
//import net.minecraft.network.protocol.login.ServerboundCustomQueryPacket;
//?} else {
import net.minecraft.network.protocol.login.ServerboundCustomQueryAnswerPacket;
import net.mat0u5.matlib.network.packets.CustomQueryPacket;
//?}

@AutoService(RegistrableClient.class)
public class MatLibNetworkHandlerClient implements RegistrableClient {

	@Override
	public void onRegister() {
		ClientNetworkEvents.RECEIVE_CUSTOM_PACKET.register(MatLibNetworkHandlerClient::onCustomPayload);
		ClientNetworkEvents.RECEIVE_PRELOGIN_PACKET.register(MatLibNetworkHandlerClient::onPreLoginPacket);
		initializeSimplePacketReceivers();
	}

	public void initializeSimplePacketReceivers() {
		MatLibSimplePackets.SYNCED_CONFIG.setClientReceive(payload -> ClientConfigManager.onConfigSyncPacket(payload.value()));
	}

	public static boolean onCustomPayload(CustomPacketPayload customPacketPayload) {
		if (customPacketPayload instanceof HandshakePayload payload) {
			Minecraft.getInstance().execute(() -> handleHandshake(payload));
		}
		else {
			return false;
		}
		return true;
	}

	public static OptionalEventReturn<Packet<?>> onPreLoginPacket(Connection connection, ClientboundCustomQueryPacket packet) {
		//~ if > 1.20 'packet.getIdentifier()' -> 'packet.payload().id()' {
		Identifier id = packet.payload().id();
		//~}

		if (id.equals(IdentifierHelper.matlib(MatLibNetworkHandlerServer.preLoginPacketID))) {
			FriendlyByteBuf responseBuf = new FriendlyByteBuf(Unpooled.buffer());
			responseBuf.writeBoolean(true);
			List<String> modIds = VersionTrackedMods.getTrackedModIds();
			responseBuf.writeInt(modIds.size());
			for (String str : modIds) {
				responseBuf.writeUtf(str);
			}

			//? if <= 1.20 {
			//return OptionalEventReturn.of(new ServerboundCustomQueryPacket(packet.getTransactionId(), responseBuf));
			//?} else {
			CustomQueryPacket answerPayload = new CustomQueryPacket(responseBuf);
			return OptionalEventReturn.of(new ServerboundCustomQueryAnswerPacket(packet.transactionId(), answerPayload));
			//?}
		}
		return OptionalEventReturn.pass();
	}

	public static void handleHandshake(HandshakePayload payload) {
		SharedClientInfo.getHandshakeStatus().setReceivedMods(payload.modIds());
		VersionCompatibility.Result result = MatLibNetworkHandlerCommon.getCompatibilityResult(payload, false);

		if (!result.passes()) {
			ClientUtils.disconnect(Component.literal(result.getError()));
			return;
		}

		ClientNetworkEvents.RECEIVE_HANDSHAKE.invoker().onReceiveHandshake();
		MatLib.LOGGER.info(TextUtils.formatString("[PACKET_CLIENT] Received handshake from server with {}", payload.modIds()));
		sendHandshake();
	}

	public static void sendHandshake() {
		HandshakePayload payload = MatLibNetworkHandlerCommon.getHandshakePayload();
		NetworkHandlerClient.send(payload);
		MatLib.DEBUG.network.handshakes.log("[PACKET_CLIENT] Sending handshake");
	}
}
