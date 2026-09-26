package net.mat0u5.matlib.client.network;

import com.google.auto.service.AutoService;
import net.mat0u5.matlib.client.events.ClientNetworkEvents;
import net.mat0u5.matlib.services.Registrable;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

@AutoService(Registrable.class)
public class MatLibNetworkHandlerClient implements Registrable {

	@Override
	public void onRegister() {
		ClientNetworkEvents.RECEIVE_CUSTOM_PACKET.register(MatLibNetworkHandlerClient::onCustomPayload);
	}

	public static boolean onCustomPayload(CustomPacketPayload customPacketPayload) {
		return false;
	}
}
