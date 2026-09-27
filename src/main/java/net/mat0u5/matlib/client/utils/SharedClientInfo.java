package net.mat0u5.matlib.client.utils;

import com.google.auto.service.AutoService;
import net.mat0u5.matlib.client.events.ClientLocalPlayerEvents;
import net.mat0u5.matlib.client.services.RegistrableClient;
import net.mat0u5.matlib.utils.enums.HandshakeStatus;

import java.util.List;

@AutoService(RegistrableClient.class)
public class SharedClientInfo implements RegistrableClient {
	private static volatile HandshakeStatus handshakeWithServer = new HandshakeStatus();

	@Override
	public void onRegister() {
		ClientLocalPlayerEvents.LEAVE.register(SharedClientInfo::resetClientData);
		ClientLocalPlayerEvents.JOIN.register(packet -> {
			DefaultClientTaskScheduler.schedulePriorityTask(20, () -> {
				if (handshakeWithServer.isWaiting()) {
					handshakeWithServer.setReceivedMods(List.of());
				}
			});
		});
	}

	public static void resetClientData() {
		handshakeWithServer = new HandshakeStatus();
	}

	public static HandshakeStatus getHandshakeStatus() {
		return handshakeWithServer;
	}
}
