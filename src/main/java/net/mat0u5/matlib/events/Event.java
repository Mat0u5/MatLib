package net.mat0u5.matlib.events;

import net.mat0u5.matlib.MatLib;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

public class Event<T> {
	private volatile T invoker;
	private boolean loud;
	private final Environment environment;

	public enum Environment {
		CLIENT,
		COMMON,
		SERVER;
	}
	public final T invoker() {
		if (MatLib.DEBUG) invokerTests();
		return invoker;
	}

	private void invokerTests() {
		if (environment == Environment.CLIENT && !MatLib.platform().isClient()) {
			MatLib.LOGGER.error("Event {} marked client triggered on server.", invoker.getClass().getName());
		}
		if (environment == Environment.SERVER && MatLib.platform().isClient()) {
			MatLib.LOGGER.warn("Event {} marked server triggered on client.", invoker.getClass().getName());
		}
		if (!loud) {
			MatLib.LOGGER.info("Event {} invoker called.", invoker.getClass().getName());
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
	}

	public int listenerCount() {
		return listeners.length;
	}

	public Event<T> markLoud() {
		loud = true;
		return this;
	}
}
