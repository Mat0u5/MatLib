package net.mat0u5.matlib.events;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class ExampleEvents {
	public static class ExampleFiring {
		public void fireEvents() {
			RenderEvent.RENDER.invoker().onRender(0.5f);
			SimpleEvent.EVENT.invoker().trigger(null);

			EventResult result = MultiEventsClass.ALLOW_LOGIN.invoker().allowLogin(null, null);
			if (!result.isDeny()) {
				// continue logic
			}

			MultiEventsClass.LOGIN.invoker().onLogin(null, null);
			MultiEventsClass.DISCONNECT.invoker().onDisconnect(null, null);
		}

		public void registerListeners() {
			RenderEvent.RENDER.register(tickDelta -> {
			});
			SimpleEvent.EVENT.register(player -> {
			});
			MultiEventsClass.ALLOW_LOGIN.register((player, server) -> {
				return EventResult.ALLOW;
			});
			MultiEventsClass.LOGIN.register((player, server) -> {
			});
			MultiEventsClass.DISCONNECT.register((player, server) -> {
			});
		}
	}

	public static final class MultiEventsClass
	{
		/**
		 * Multiple events in a single class, utilizing functional interfaces.
		 */
		public static final Event<Login> LOGIN = EventFactory.create(Login.class,
				listeners -> (player, server) -> EventFactory.dispatch(listeners, listener -> listener.onLogin(player, server))
		);

		public static final Event<Disconnect> DISCONNECT = EventFactory.create(Disconnect.class,
				listeners -> (player, server) -> EventFactory.dispatch(listeners, listener -> listener.onDisconnect(player, server))
		);

		/**
		 * Fires before a player is allowed to join.
		 * <p> EventFactory.dispatchResult goes through all listeners until one returns {@link EventResult#ALLOW} or {@link EventResult#DENY}
		 * <p>{@link EventResult} decides whether to allow the login or not (or pass -> allow).
		 */
		public static final Event<AllowLogin> ALLOW_LOGIN = EventFactory.create(AllowLogin.class,
				listeners -> (player, server) -> EventFactory.dispatchResult(listeners, listener -> listener.allowLogin(player, server))
		);

		@FunctionalInterface
		public interface Login {
			void onLogin(ServerPlayer player, MinecraftServer server);
		}

		@FunctionalInterface
		public interface Disconnect {
			void onDisconnect(ServerPlayer player, MinecraftServer server);
		}

		@FunctionalInterface
		public interface AllowLogin {
			EventResult allowLogin(ServerPlayer player, MinecraftServer server);
		}
	}

	public interface SimpleEvent {
		/**
		 * As simple as it gets.
		 */
		Event<SimpleEvent> EVENT = EventFactory.create(SimpleEvent.class,
				listeners -> (player) -> EventFactory.dispatch(listeners, listener -> listener.trigger(player))
		);

		void trigger(ServerPlayer player);
	}

	public static class RenderEvent {
		/**
		 * Fired once per frame
		 * <p>{@link EventFactory#dispatch} would cost unnecessary performance, so we do a raw loop here.
		 */
		public static final Event<RenderEvent.Render> RENDER = EventFactory.create(RenderEvent.Render.class,
				listeners -> tickDelta -> {
					for (RenderEvent.Render listener : listeners) {
						try {
							listener.onRender(tickDelta);
						}
						catch (Exception e) {
							EventFactory.logListenerError(listener, e);
						}
					}
				}
		);

		@FunctionalInterface
		public interface Render {
			void onRender(float tickDelta);
		}
	}
}
