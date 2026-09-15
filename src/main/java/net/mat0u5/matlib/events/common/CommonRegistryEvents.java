package net.mat0u5.matlib.events.common;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.mat0u5.matlib.util.AttributeEntity;

import java.util.List;

public class CommonRegistryEvents {
	/**
	 * Fires when registries begin freezing - the point at which you should register custom stuff.
	 */
	public static final Event<PreFreeze> PRE_FREEZE = EventFactory.create(PreFreeze.class,
			listeners -> () -> EventFactory.dispatch(listeners, listener -> listener.onPreFreeze())
	);

	@FunctionalInterface
	public interface PreFreeze {
		void onPreFreeze();
	}

	/**
	 * Fires when mob attributes are being registered.
	 */
	public static final Event<MobAttribute> MOB_ATTRIBUTE = EventFactory.create(MobAttribute.class,
			listeners -> () -> EventFactory.dispatchCollect(listeners, listener -> listener.getAttributeEntities())
	);

	@FunctionalInterface
	public interface MobAttribute {
		List<AttributeEntity> getAttributeEntities();
	}
}
