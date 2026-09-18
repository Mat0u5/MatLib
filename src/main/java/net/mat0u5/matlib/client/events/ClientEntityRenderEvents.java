package net.mat0u5.matlib.client.events;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.mat0u5.matlib.events.EventResult;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.Entity;

import java.util.List;

public class ClientEntityRenderEvents {

	/**
	 * Fires when an entity is about to be rendered.
	 */
	public static final Event<ShouldRender> SHOULD_RENDER = EventFactory.create(ShouldRender.class,
			listeners -> (entity, culler, camX, camY, camZ) -> EventFactory.dispatchResult(listeners, listener -> listener.shouldRender(entity, culler, camX, camY, camZ))
	).markLoud();

	/**
	 * Modify the list of entities for rendering.
	 */
	public static final Event<EntitiesForRendering> ENTITIES_FOR_RENDERING = EventFactory.create(EntitiesForRendering.class,
			listeners -> entities -> EventFactory.dispatch(listeners, listener -> listener.modifyList(entities))
	).markLoud();

	@FunctionalInterface
	public interface ShouldRender {
		EventResult shouldRender(Entity entity, Frustum culler, double camX, double camY, double camZ);
	}

	@FunctionalInterface
	public interface EntitiesForRendering {
		void modifyList(List<Entity> entities);
	}
}
