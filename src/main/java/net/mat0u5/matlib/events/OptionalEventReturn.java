package net.mat0u5.matlib.events;

public class OptionalEventReturn<T> {
	private static final OptionalEventReturn<?> PASS_INSTANCE = new OptionalEventReturn<>(null);

	private final T value;

	private OptionalEventReturn(T value) {
		this.value = value;
	}

	public T get() {
		return this.value;
	}

	public T getOrDefault(T defaultValue) {
		if (isPass()) return defaultValue;
		return this.value;
	}

	public boolean isPass() {
		return this == PASS_INSTANCE;
	}

	public static <T> OptionalEventReturn<T> of(T value) {
		return new OptionalEventReturn<>(value);
	}

	@SuppressWarnings("unchecked")
	public static <T> OptionalEventReturn<T> pass() {
		return (OptionalEventReturn<T>) PASS_INSTANCE;
	}
}