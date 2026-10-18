package net.mat0u5.matlib.events.server;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.mat0u5.matlib.events.EventResult;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class ServerChatEvents {

	/**
	 * Fires when a system message is broadcast to all players.
	 */
	public static final Event<BroadcastSystem> BROADCAST_SYSTEM = EventFactory.createServer(BroadcastSystem.class,
			listeners -> (message, overlay) -> EventFactory.dispatchEventResult(listeners, listener -> listener.onBroadcastSystem(message, overlay))
	);

	@FunctionalInterface
	public interface BroadcastSystem {
		EventResult onBroadcastSystem(Component message, boolean overlay);
	}

	/**
	 * Fires when a player chat message is broadcast to all players.
	 */
	public static final Event<BroadcastChat> BROADCAST_CHAT = EventFactory.createServer(BroadcastChat.class,
			listeners -> (message, sender, params) -> EventFactory.dispatchEventResult(listeners, listener -> listener.onBroadcastChat(message, sender, params))
	);

	@FunctionalInterface
	public interface BroadcastChat {
		EventResult onBroadcastChat(PlayerChatMessage message, @Nullable ServerPlayer sender, ChatType.Bound params);
	}
}
