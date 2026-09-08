package net.mat0u5.matlib.events.common;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.mat0u5.matlib.events.EventResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class ServerEntityEvents {

	/**
	 * Fires when an entity dies.
	 */
	public static final Event<Death> DEATH = EventFactory.create(Death.class,
			listeners -> (entity, source) -> EventFactory.dispatch(listeners, listener -> listener.onDeath(entity, source))
	);

	/**
	 * Fires when an entity drops their.
	 */
	public static final Event<DropLoot> DROP_LOOT = EventFactory.create(DropLoot.class,
			listeners -> (entity, source) -> EventFactory.dispatchResult(listeners, listener -> listener.onDropLoot(entity, source))
	);

	@FunctionalInterface
	public interface Death {
		void onDeath(LivingEntity entity, DamageSource source);
	}

	@FunctionalInterface
	public interface DropLoot {
		EventResult onDropLoot(LivingEntity entity, DamageSource source);
	}
}
