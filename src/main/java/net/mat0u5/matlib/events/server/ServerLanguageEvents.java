package net.mat0u5.matlib.events.server;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;

import java.util.ArrayList;
import java.util.List;

public class ServerLanguageEvents {
	/**
	 * Fires when the server loads language files.
	 * <p>Return: List of paths to language files - ex. "/resourcepacks/matlib/assets/matlib/lang/en_us.json"
	 */
	public static final Event<LoadLangFiles> LOAD_LANG_FILES = EventFactory.create(LoadLangFiles.class,
			listeners -> () -> {
				List<String> result = new ArrayList<>();
				for (var listener : listeners) {
					try {
						List<String> returned = listener.onLoadFiles();
						if (returned != null) result.addAll(returned);
					}
					catch (Exception e) {
						EventFactory.logListenerError(listener, e);
					}
				}
				return result;
			}
	);

	@FunctionalInterface
	public interface LoadLangFiles {
		List<String> onLoadFiles();
	}
}
