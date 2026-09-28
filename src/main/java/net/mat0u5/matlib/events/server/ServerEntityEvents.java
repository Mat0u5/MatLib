package net.mat0u5.matlib.events.server;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.mat0u5.matlib.events.EventResult;
import net.mat0u5.matlib.events.common.CommonEntityEvents;
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
	 * Fires when the entity gets damaged.
	 */
	public static final Event<Damage> DAMAGE = EventFactory.createServer(Damage.class,
			listeners -> (entity, source, amount) -> EventFactory.dispatch(listeners, listener -> listener.onDamage(entity, source, amount))
	);

	@FunctionalInterface
	public interface Damage {
		void onDamage(LivingEntity entity, DamageSource source, float amount);
	}

	/**
	 * Fires before the entity gets damaged.
	 */
	public static final Event<PreDamage> PRE_DAMAGE = EventFactory.createServer(PreDamage.class,
			listeners -> (entity, source, amount) -> EventFactory.dispatchEventResult(listeners, listener -> listener.onPreDamage(entity, source, amount))
	);

	@FunctionalInterface
	public interface PreDamage {
		EventResult onPreDamage(LivingEntity entity, DamageSource source, float amount);
	}

	/**
	 * Fires when the entity gets healed.
	 */
	public static final Event<Heal> HEAL = EventFactory.createServer(Heal.class,
			listeners -> (entity, amount) -> EventFactory.dispatch(listeners, listener -> listener.onHeal(entity, amount))
	);

	@FunctionalInterface
	public interface Heal {
		void onHeal(LivingEntity entity, float amount);
	}

	/**
	 * Fires before the entity gets healed.
	 */
	public static final Event<PreHeal> PRE_HEAL = EventFactory.createServer(PreHeal.class,
			listeners -> (entity, amount) -> EventFactory.dispatchEventResult(listeners, listener -> listener.onPreHeal(entity, amount))
	);

	@FunctionalInterface
	public interface PreHeal {
		EventResult onPreHeal(LivingEntity entity, float amount);
	}
}
