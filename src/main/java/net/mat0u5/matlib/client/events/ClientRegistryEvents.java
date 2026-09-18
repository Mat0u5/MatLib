package net.mat0u5.matlib.client.events;

import net.mat0u5.matlib.client.registries.util.ProvidedParticle;
import net.mat0u5.matlib.client.registries.util.RenderableEntity;
import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;

import java.util.List;

public class ClientRegistryEvents {

	/**
	 * Fires when entity renderers are being registered.
	 */
	public static final Event<EntityRenderer> ENTITY_RENDERER = EventFactory.create(EntityRenderer.class,
			listeners -> () -> EventFactory.dispatchCollect(listeners, listener -> listener.getEntityRenderers())
	);

	@FunctionalInterface
	public interface EntityRenderer {
		List<RenderableEntity> getEntityRenderers();
	}

	/**
	 * Fires when particle providers are being registered.
	 */
	public static final Event<ParticleProvider> PARTICLE_PROVIDER = EventFactory.create(ParticleProvider.class,
			listeners -> () -> EventFactory.dispatchCollect(listeners, listener -> listener.getProvidedParticles())
	);

	@FunctionalInterface
	public interface ParticleProvider {
		List<ProvidedParticle> getProvidedParticles();
	}

	/**
	 * Fires when keybinds are being registered.
	 */
	public static final Event<KeyMapping> KEYBIND = EventFactory.create(KeyMapping.class,
			listeners -> mappings -> EventFactory.dispatch(listeners, listener -> listener.modifyKeymappings(mappings))
	);

	@FunctionalInterface
	public interface KeyMapping {
		void modifyKeymappings(List<net.minecraft.client.KeyMapping> mappings);
	}
}
