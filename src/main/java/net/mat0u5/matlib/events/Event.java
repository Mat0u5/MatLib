package net.mat0u5.matlib.events;

import net.mat0u5.matlib.MatLib;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

public class Event<T> {
	private volatile T invoker;
	private boolean loud = false;

	public final T invoker() {
		if (MatLib.DEBUG && !loud) {
			MatLib.LOGGER.info("Event {} invoker called.", invoker.getClass().getName());
		}
		return invoker;
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
	public Event(Class<? super T> type, Function<T[], T> invokerFactory) {
		this.invokerFactory = invokerFactory;
		this.listeners = (T[]) Array.newInstance(type, 0);
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
