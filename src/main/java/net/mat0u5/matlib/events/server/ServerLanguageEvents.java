package net.mat0u5.matlib.events.server;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;

import java.util.List;

public class ServerLanguageEvents {
	/**
	 * Fires when the server loads language files.
	 * <p>Return: List of paths to language files - ex. "/resourcepacks/matlib/assets/matlib/lang/en_us.json"
	 */
	public static final Event<LoadLangFiles> LOAD_LANG_FILES = EventFactory.createServer(LoadLangFiles.class,
			listeners -> () -> EventFactory.dispatchCollect(listeners, listener -> listener.getPaths())
	);

	@FunctionalInterface
	public interface LoadLangFiles {
		List<String> getPaths();
	}
}
