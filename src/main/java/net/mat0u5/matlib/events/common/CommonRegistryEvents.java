package net.mat0u5.matlib.events.common;

import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.mat0u5.matlib.registries.util.AttributeEntity;
import net.mat0u5.matlib.registries.util.IdentifiedParticle;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.*;

//? if <= 1.20.3 {
/*import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import java.util.function.Function;
*///?} else {
import net.minecraft.network.RegistryFriendlyByteBuf;
//?}

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

	/**
	 * Fires when payload readers are being registered.
	 */
	public static final Event<PacketPayloads> PACKET_PAYLOADS = EventFactory.createCommon(PacketPayloads.class,
			//~ if >  1.20.3 '.dispatchCollectMap' -> '.dispatchCollect' {
			listeners -> () -> EventFactory.dispatchCollect(listeners, listener -> listener.getPacketPayloads())
			//~}
	);

	@FunctionalInterface
	public interface PacketPayloads {
		//? if <= 1.20.3 {
		/*Map<Identifier, Function<FriendlyByteBuf, CustomPacketPayload>> getPacketPayloads();
		*///?} else {
		List<CustomPacketPayload.TypeAndCodec<? super RegistryFriendlyByteBuf, ? extends CustomPacketPayload>> getPacketPayloads();
		//?}
	}
}
