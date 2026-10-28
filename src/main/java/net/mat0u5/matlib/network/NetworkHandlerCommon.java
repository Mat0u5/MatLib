package net.mat0u5.matlib.network;

import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.events.common.CommonRegistryEvents;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

//? if <= 1.20.3 {
/*import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import java.util.function.Function;
*///?} else {
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
//?}

public class NetworkHandlerCommon {

	//? if <= 1.20.3 {
    /*private static Map<Identifier, Function<FriendlyByteBuf, CustomPacketPayload>> PAYLOAD_READERS = null;
	public static Map<Identifier, Function<FriendlyByteBuf, CustomPacketPayload>> getPayloadReaders() {
		if (PAYLOAD_READERS != null) return PAYLOAD_READERS;
		Map<Identifier, Function<FriendlyByteBuf, CustomPacketPayload>> payloadReaders = CommonRegistryEvents.PACKET_PAYLOADS.invoker().getPacketPayloads();
		if (MatLib.isRegistered()) PAYLOAD_READERS = payloadReaders;
		return payloadReaders;
	}
    *///?} else {
	private static List<CustomPacketPayload.TypeAndCodec<? super RegistryFriendlyByteBuf, ? extends CustomPacketPayload>> PAYLOADS = null;
	private static Map<Identifier, CustomPacketPayload.TypeAndCodec<? super RegistryFriendlyByteBuf, ? extends CustomPacketPayload>> PAYLOADS_BY_ID = null;

	public static List<CustomPacketPayload.TypeAndCodec<? super RegistryFriendlyByteBuf, ? extends CustomPacketPayload>> getPayloads() {
		if (PAYLOADS != null) return PAYLOADS;
		List<CustomPacketPayload.TypeAndCodec<? super RegistryFriendlyByteBuf, ? extends CustomPacketPayload>> payloads = CommonRegistryEvents.PACKET_PAYLOADS.invoker().getPacketPayloads();
		if (MatLib.isRegistered()) PAYLOADS = payloads;
		return payloads;
	}

	public static CustomPacketPayload.TypeAndCodec<? super RegistryFriendlyByteBuf, ? extends CustomPacketPayload> getPayload(Identifier id) {
		if (PAYLOADS_BY_ID != null) return PAYLOADS_BY_ID.get(id);
		Map<Identifier, CustomPacketPayload.TypeAndCodec<? super RegistryFriendlyByteBuf, ? extends CustomPacketPayload>> payloadsById = new HashMap<>();
		for (var payload : getPayloads()) {
			payloadsById.putIfAbsent(payload.type().id(), payload);
		}
		if (MatLib.isRegistered()) PAYLOADS_BY_ID = payloadsById;
		return payloadsById.get(id);
	}

	public static <B extends FriendlyByteBuf> CustomPacketPayload.FallbackProvider<B> wrapFallbackProvider(CustomPacketPayload.FallbackProvider<B> original) {
		return id -> {
			var payload = getPayload(id);
			if (payload != null) {
				return (StreamCodec) payload.codec();
			}
			return original.create(id);
		};
	}
	//?}
}
