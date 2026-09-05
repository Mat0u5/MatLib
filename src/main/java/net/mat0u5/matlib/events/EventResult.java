package net.mat0u5.matlib.events;

/**
 * The result of a single listener of a cancellable event.
 */
public enum EventResult {
	/** No decision, continue iterating **/
	PASS,
	/** Stop iterating, allow event **/
	ALLOW,
	/** Stop iterating, cancel event **/
	DENY;

	public boolean isPass() {
		return this == PASS;
	}

	public boolean isAllow() {
		return this == ALLOW;
	}

	public boolean isDeny() {
		return this == DENY;
	}

	public boolean interrupts() {
		return this != PASS;
	}

	public static EventResult of(boolean allowed) {
		return allowed ? ALLOW : DENY;
	}
}
