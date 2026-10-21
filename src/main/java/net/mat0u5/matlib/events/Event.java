package net.mat0u5.matlib.events;

import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.utils.other.DebugOptions;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

public class Event<T> {
	private volatile T invoker;
	private boolean loud;
	private final Environment environment;
	private final Class<? super T> type;

	public enum Environment {
		CLIENT,
		COMMON,
		SERVER;
	}

	public final T invoker() {
		invokerTests();
		return invoker;
	}

	private void invokerTests() {
		DebugOptions.EventOptions debug = MatLib.DEBUG.events;
		if (debug.environmentMismatch.enabled()) {
			if (environment == Environment.CLIENT && !MatLib.platform().isClient()) {
				MatLib.LOGGER.error("Event {} marked client triggered on server.", type.getName());
			}
			if (environment == Environment.SERVER && (MatLib.platform().isClient() && MatLib.hasClient() && MatLib.getClientAccessor().isConnectedToRemoteServer())) {
				MatLib.LOGGER.warn("Event {} marked server triggered on client.", type.getName());
			}
		}
		if (loud ? debug.loudInvocations.enabled() : debug.invocations.enabled()) {
			MatLib.LOGGER.info("Event {} invoker called ({} listeners).", type.getName(), listeners.length);
		}
	}

	private final Function<T[], T> invokerFactory;
	protected final Object lock = new Object();
	private T[] listeners;

	private void addListener(T listener) {
		int oldLength = listeners.length;
		listeners = Arrays.copyOf(listeners, oldLength + 1);
		listeners[oldLength] = listener;
	}

	@SuppressWarnings("unchecked")
	public Event(Class<? super T> type, Function<T[], T> invokerFactory, Environment environment) {
		this.type = type;
		this.invokerFactory = invokerFactory;
		this.listeners = (T[]) Array.newInstance(type, 0);
		this.environment = environment;
		update();
	}

	private void update() {
		this.invoker = invokerFactory.apply(listeners);
	}

	public void register(T listener) {
		Objects.requireNonNull(listener, "Tried to register a null listener!");

		synchronized (lock) {
			addListener(listener);
			update();
		}
		if (MatLib.DEBUG.events.registrations.enabled()) {
			MatLib.LOGGER.info("Registered listener {} for event {}.", listener.getClass().getName(), type.getName());
		}
	}

	public int listenerCount() {
		return listeners.length;
	}

	public Event<T> markLoud() {
		loud = true;
		return this;
	}
}
