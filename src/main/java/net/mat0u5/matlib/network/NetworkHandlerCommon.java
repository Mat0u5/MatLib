package net.mat0u5.matlib.network;

import net.mat0u5.matlib.events.common.CommonRegistryEvents;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.*;

//? if <= 1.20.3 {
/*import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import java.util.function.Function;
*///?} else {
import net.minecraft.network.RegistryFriendlyByteBuf;
//?}

public class NetworkHandlerCommon {

	//? if <= 1.20.3 {
    /*private static Map<Identifier, Function<FriendlyByteBuf, CustomPacketPayload>> PAYLOAD_READERS = null;
	public static Map<Identifier, Function<FriendlyByteBuf, CustomPacketPayload>> getPayloadReaders() {
		if (PAYLOAD_READERS == null) {
			PAYLOAD_READERS = CommonRegistryEvents.PACKET_PAYLOADS.invoker().getPacketPayloads();
		}
		return PAYLOAD_READERS;
	}
    *///?} else {
	private static List<CustomPacketPayload.TypeAndCodec<? super RegistryFriendlyByteBuf, ? extends CustomPacketPayload>> PAYLOADS = null;
	public static List<CustomPacketPayload.TypeAndCodec<? super RegistryFriendlyByteBuf, ? extends CustomPacketPayload>> getPayloads() {
		if (PAYLOADS == null) {
			PAYLOADS = CommonRegistryEvents.PACKET_PAYLOADS.invoker().getPacketPayloads();
		}
		return PAYLOADS;
	}
	//?}


}
