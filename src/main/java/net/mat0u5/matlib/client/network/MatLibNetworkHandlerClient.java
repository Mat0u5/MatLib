package net.mat0u5.matlib.client.network;

import com.google.auto.service.AutoService;
import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.client.events.ClientNetworkEvents;
import net.mat0u5.matlib.client.services.RegistrableClient;
import net.mat0u5.matlib.client.utils.ClientUtils;
import net.mat0u5.matlib.client.utils.SharedClientInfo;
import net.mat0u5.matlib.network.MatLibNetworkHandlerCommon;
import net.mat0u5.matlib.network.packets.HandshakePayload;
import net.mat0u5.matlib.utils.enums.HandshakeStatus;
import net.mat0u5.matlib.utils.other.TextUtils;
import net.mat0u5.matlib.utils.other.VersionCompatibility;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

@AutoService(RegistrableClient.class)
public class MatLibNetworkHandlerClient implements RegistrableClient {

	@Override
	public void onRegister() {
		ClientNetworkEvents.RECEIVE_CUSTOM_PACKET.register(MatLibNetworkHandlerClient::onCustomPayload);
	}

	public static boolean onCustomPayload(CustomPacketPayload customPacketPayload) {
		if (customPacketPayload instanceof HandshakePayload payload) {
			handleHandshake(payload);
		}
		else {
			return false;
		}
		return true;
	}

	public static void handleHandshake(HandshakePayload payload) {
		SharedClientInfo.getHandshakeStatus().setReceivedMods(payload.modIds());
		VersionCompatibility.Result result = MatLibNetworkHandlerCommon.getCompatibilityResult(payload, false);

		if (!result.passes()) {
			ClientUtils.disconnect(Component.literal(result.getError()));
		}

		ClientNetworkEvents.RECEIVE_HANDSHAKE.invoker().onReceiveHandshake();
		MatLib.LOGGER.info(TextUtils.formatString("[PACKET_CLIENT] Received handshake from server"));
		sendHandshake();
	}

	public static void sendHandshake() {
		HandshakePayload payload = MatLibNetworkHandlerCommon.getHandshakePayload();
		NetworkHandlerClient.send(payload);
		if (MatLib.DEBUG) MatLib.LOGGER.info("[PACKET_CLIENT] Sending handshake");
	}
}
