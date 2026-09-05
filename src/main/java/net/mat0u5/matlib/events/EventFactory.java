package net.mat0u5.matlib.events;

import java.util.function.Consumer;
import java.util.function.Function;

import net.mat0u5.matlib.MatLib;

public final class EventFactory {
	private EventFactory() { }
	/**
	 * Creates an event.
	 */
	public static <T> Event<T> create(Class<? super T> type, Function<T[], T> invokerFactory) {
		return new Event<>(type, invokerFactory);
	}

	/**
	 * Calls all listeners, logging any errors along the way.
	 */
	public static <T> void dispatch(T[] listeners, Consumer<T> call) {
		for (T listener : listeners) {
			try {
				call.accept(listener);
			}
			catch (Exception e) {
				logListenerError(listener, e);
			}
		}
	}

	/**
	 * Calls all listeners until one returns something other than {@link EventResult#PASS}, returning that answer.
	 */
	public static <T> EventResult dispatchResult(T[] listeners, Function<T, EventResult> call) {
		for (T listener : listeners) {
			EventResult result;

			try {
				result = call.apply(listener);
			}
			catch (Exception e) {
				logListenerError(listener, e);
				continue;
			}

			if (result != null && result.interrupts()) return result;
		}

		return EventResult.PASS;
	}

	public static void logListenerError(Object listener, Throwable error) {
		MatLib.LOGGER.error("Event listener {} threw an exception", listener.getClass().getName(), error);
	}
}
