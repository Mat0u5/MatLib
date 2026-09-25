package net.mat0u5.matlib.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import net.mat0u5.matlib.MatLib;

public final class EventFactory {
	private EventFactory() { }
	/**
	 * Creates a client event.
	 */
	public static <T> Event<T> createClient(Class<? super T> type, Function<T[], T> invokerFactory) {
		return new Event<>(type, invokerFactory, Event.Environment.CLIENT);
	}

	/**
	 * Creates a common event.
	 */
	public static <T> Event<T> createCommon(Class<? super T> type, Function<T[], T> invokerFactory) {
		return new Event<>(type, invokerFactory, Event.Environment.COMMON);
	}

	/**
	 * Creates a server event.
	 */
	public static <T> Event<T> createServer(Class<? super T> type, Function<T[], T> invokerFactory) {
		return new Event<>(type, invokerFactory, Event.Environment.SERVER);
	}

	/**
	 * Creates an event.
	 */
	private static <T> Event<T> create(Class<? super T> type, Function<T[], T> invokerFactory, Event.Environment environment) {
		return new Event<>(type, invokerFactory, environment);
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
	public static <T> EventResult dispatchEventResult(T[] listeners, Function<T, EventResult> call) {
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

	/**
	 * Calls all listeners until one returns something other than {@link OptionalEventReturn#pass()}, returning that answer.
	 */
	public static <T, U> OptionalEventReturn<U> dispatchOptionalReturn(T[] listeners, Function<T, OptionalEventReturn<U>> call) {
		for (T listener : listeners) {
			OptionalEventReturn<U> result;

			try {
				result = call.apply(listener);
			}
			catch (Exception e) {
				logListenerError(listener, e);
				continue;
			}

			if (result != null && !result.isPass()) return result;
		}

		return OptionalEventReturn.pass();
	}

	/**
	 * Calls all listeners returning a list, collecting all answers.
	 */
	public static <T, U> List<U> dispatchCollect(T[] listeners, Function<T, List<U>> call) {
		List<U> result = new ArrayList<>();
		for (T listener : listeners) {
			try {
				List<U> returned = call.apply(listener);
				if (returned != null) result.addAll(returned);
			}
			catch (Exception e) {
				logListenerError(listener, e);
			}
		}

		return result;
	}

	/**
	 * Calls all listeners until one returns something other than {@code null}, returning that answer.
	 */
	public static <T, U> U dispatchReturn(T[] listeners, Function<T, U> call) {
		return dispatchReturn(listeners, null, call);
	}

	/**
	 * Calls all listeners until one returns something other than {@param defaultReturnValue}, returning that answer.
	 *
	 * @param defaultReturnValue The value to ignore and default to if no listener provides a valid response.
	 */
	public static <T, U> U dispatchReturn(T[] listeners, U defaultReturnValue, Function<T, U> call) {
		for (T listener : listeners) {
			U result;

			try {
				result = call.apply(listener);
			}
			catch (Exception e) {
				logListenerError(listener, e);
				continue;
			}

			if (!Objects.equals(result, defaultReturnValue)) return result;
		}

		return defaultReturnValue;
	}

	public static void logListenerError(Object listener, Throwable error) {
		MatLib.LOGGER.error("Event listener {} threw an exception", listener.getClass().getName(), error);
	}
}
