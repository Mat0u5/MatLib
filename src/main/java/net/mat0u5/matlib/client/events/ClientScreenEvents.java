package net.mat0u5.matlib.client.events;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.client.gui.screens.Screen;

public class ClientScreenEvents {


	/**
	 * Fires when you open a screen.
	 */
	public static final Event<OpenScreen> OPEN_SCREEN = EventFactory.createClient(OpenScreen.class,
			listeners -> (screen, width, height) -> EventFactory.dispatch(listeners, listener -> listener.onOpen(screen, width, height))
	);

	@FunctionalInterface
	public interface OpenScreen {
		void onOpen(Screen screen, int width, int height);
	}


	/**
	 * Fires when you close a screen.
	 */
	public static final Event<CloseScreen> CLOSE_SCREEN = EventFactory.createClient(CloseScreen.class,
			listeners -> screen -> EventFactory.dispatch(listeners, listener -> listener.onClose(screen))
	);

	@FunctionalInterface
	public interface CloseScreen {
		void onClose(Screen screen);
	}

	/**
	 * Fires the screen resizes.
	 */
	public static final Event<ResizeScreen> RESIZE_SCREEN = EventFactory.createClient(ResizeScreen.class,
			listeners -> (screen, width, height) -> EventFactory.dispatch(listeners, listener -> listener.onResize(screen, width, height))
	).markLoud();

	@FunctionalInterface
	public interface ResizeScreen {
		void onResize(Screen screen, int width, int height);
	}
}
