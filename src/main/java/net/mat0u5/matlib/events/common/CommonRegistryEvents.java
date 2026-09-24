package net.mat0u5.matlib.events.common;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.mat0u5.matlib.registries.util.AttributeEntity;
import net.mat0u5.matlib.registries.util.IdentifiedParticle;

import java.util.List;

public class CommonRegistryEvents {
	/**
	 * Fires when registries begin freezing - the point at which you should register custom stuff.
	 */
	public static final Event<PreFreeze> PRE_FREEZE = EventFactory.createCommon(PreFreeze.class,
			listeners -> () -> EventFactory.dispatch(listeners, listener -> listener.onPreFreeze())
	);

	@FunctionalInterface
	public interface PreFreeze {
		void onPreFreeze();
	}

	/**
	 * Fires when mob attributes are being registered.
	 */
	public static final Event<MobAttribute> MOB_ATTRIBUTE = EventFactory.createCommon(MobAttribute.class,
			listeners -> () -> EventFactory.dispatchCollect(listeners, listener -> listener.getAttributeEntities())
	);

	@FunctionalInterface
	public interface MobAttribute {
		List<AttributeEntity> getAttributeEntities();
	}

	/**
	 * Fires when custom particles are being registered.
	 */
	public static final Event<Particle> PARTICLE = EventFactory.createCommon(Particle.class,
			listeners -> () -> EventFactory.dispatchCollect(listeners, listener -> listener.getIdentifiedParticles())
	);

	@FunctionalInterface
	public interface Particle {
		List<IdentifiedParticle> getIdentifiedParticles();
	}
}
