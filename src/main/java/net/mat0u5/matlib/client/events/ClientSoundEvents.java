package net.mat0u5.matlib.client.events;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.client.resources.sounds.SoundInstance;

public class ClientSoundEvents {

	/**
	 * Fires when a sound is played.
	 */
	public static final Event<PlaySound> PLAY_SOUND = EventFactory.createClient(PlaySound.class,
			listeners -> sound -> EventFactory.dispatch(listeners, listener -> listener.onPlay(sound))
	);

	@FunctionalInterface
	public interface PlaySound {
		void onPlay(SoundInstance sound);
	}
}
