package net.mat0u5.matlib.client.events;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.mat0u5.matlib.events.OptionalEventReturn;
import net.mat0u5.matlib.events.common.CommonEntityEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class ClientEntityEvents extends CommonEntityEvents {

	/**
	 * Fires when an entity ticks.
	 */
	public static final Event<TickEntity> TICK_ENTITY = EventFactory.createClient(TickEntity.class,
			listeners -> entity -> EventFactory.dispatch(listeners, listener -> listener.onTick(entity))
	).markLoud();

	@FunctionalInterface
	public interface TickEntity {
		void onTick(Entity entity);
	}

	/**
	 * Fires when a nametag renders for an entity.
	 */
	public static final Event<RenderNameTag> RENDER_ENTITY_NAMETAG = EventFactory.createClient(RenderNameTag.class,
			listeners -> (entity, originalReturn) -> EventFactory.dispatchOptionalReturn(listeners, listener -> listener.getNameTag(entity, originalReturn))
	).markLoud();

	@FunctionalInterface
	public interface RenderNameTag {
		@NotNull OptionalEventReturn<Component> getNameTag(Entity entity, Component originalReturn);
	}
}
