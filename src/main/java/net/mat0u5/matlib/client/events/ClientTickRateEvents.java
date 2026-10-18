package net.mat0u5.matlib.client.events;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.mat0u5.matlib.events.OptionalEventReturn;

public class ClientTickRateEvents {

	/**
	 * Fires when the client calculates how many milliseconds a tick should last for.
	 */
	public static final Event<ModifyTargetMspt> MODIFY_TARGET_MSPT = EventFactory.createClient(ModifyTargetMspt.class,
			listeners -> defaultMillis -> EventFactory.dispatchOptionalReturn(listeners, listener -> listener.onModifyTargetMspt(defaultMillis))
	).markLoud();

	@FunctionalInterface
	public interface ModifyTargetMspt {
		OptionalEventReturn<Float> onModifyTargetMspt(float defaultMillis);
	}
}
