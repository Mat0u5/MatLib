package net.mat0u5.matlib.client.events;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.client.Minecraft;

public class ClientLifecycleEvents {

	/**
	 * Fires upon client starting.
	 */
	public static final Event<ClientStarted> CLIENT_STARTED = EventFactory.create(ClientStarted.class,
			listeners -> minecraft -> EventFactory.dispatch(listeners, listener -> listener.onStarted(minecraft))
	);

	/**
	 * Fires upon client stopping.
	 */
	public static final Event<ClientStopping> CLIENT_STOPPING = EventFactory.create(ClientStopping.class,
			listeners -> minecraft -> EventFactory.dispatch(listeners, listener -> listener.onStopping(minecraft))
	);

	@FunctionalInterface
	public interface ClientStarted {
		void onStarted(Minecraft minecraft);
	}

	@FunctionalInterface
	public interface ClientStopping {
		void onStopping(Minecraft minecraft);
	}
}
