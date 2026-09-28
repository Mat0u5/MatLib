package net.mat0u5.matlib.events.server;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.mat0u5.matlib.events.EventResult;
import net.mat0u5.matlib.events.common.CommonEntityEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class ServerEntityEvents extends CommonEntityEvents {

	/**
	 * Fires after an entity dies.
	 */
	public static final Event<Death> DEATH = EventFactory.createServer(Death.class,
			listeners -> (entity, source) -> EventFactory.dispatch(listeners, listener -> listener.onDeath(entity, source))
	);

	/**
	 * Fires when an entity drops their items.
	 */
	public static final Event<DropLoot> DROP_LOOT = EventFactory.createServer(DropLoot.class,
			listeners -> (entity, source) -> EventFactory.dispatchEventResult(listeners, listener -> listener.onDropLoot(entity, source))
	);

	@FunctionalInterface
	public interface Death {
		void onDeath(LivingEntity entity, DamageSource source);
	}

	@FunctionalInterface
	public interface DropLoot {
		EventResult onDropLoot(LivingEntity entity, DamageSource source);
	}

	/**
	 * Fires before an entity dies.
	 */
	public static final Event<PreDeath> PRE_DEATH = EventFactory.createServer(PreDeath.class,
			listeners -> (entity, source) -> EventFactory.dispatchEventResult(listeners, listener -> listener.onPreDeath(entity, source))
	);

	@FunctionalInterface
	public interface PreDeath {
		EventResult onPreDeath(LivingEntity entity, DamageSource source);
	}

	/**
	 * Fires when the entity gets ticked.
	 */
	public static final Event<Tick> TICK = EventFactory.createServer(Tick.class,
			listeners -> entity -> EventFactory.dispatch(listeners, listener -> listener.onTickStart(entity))
	).markLoud();

	@FunctionalInterface
	public interface Tick {
		void onTickStart(LivingEntity entity);
	}
}
