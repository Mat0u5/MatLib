package net.mat0u5.matlib.events.common;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;

//? if <= 1.20.5 {
/*import net.minecraft.network.chat.Component;
*///?} else {
import net.minecraft.network.DisconnectionDetails;
//?}

public class ServerPlayerEvents {

	/**
	 * Fires when a player joins the server.
	 */
	public static final Event<Connect> CONNECT = EventFactory.create(Connect.class,
			listeners -> (connection, player) -> EventFactory.dispatch(listeners, listener -> listener.connect(connection, player))
	);

	/**
	 * Fires when a player leaves the server.
	 */
	public static final Event<Disconnect> DISCONNECT = EventFactory.create(Disconnect.class,
			listeners -> (details, player) -> EventFactory.dispatch(listeners, listener -> listener.disconnect(details, player))
	);

	@FunctionalInterface
	public interface Connect {
		void connect(Connection connection, ServerPlayer player);
	}

	@FunctionalInterface
	public interface Disconnect {
		//? if <= 1.20.5 {
		/*void disconnect(Component reason, ServerPlayer player);
		*///?} else {
		void disconnect(DisconnectionDetails details, ServerPlayer player);
		//?}
	}
}
