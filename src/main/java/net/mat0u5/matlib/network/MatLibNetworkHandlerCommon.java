package net.mat0u5.matlib.network;

import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.network.packets.*;
import net.mat0u5.matlib.services.NetworkCommon;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

public class MatLibNetworkHandlerCommon implements NetworkCommon {

	//? if <= 1.20.3 {
    /*public static final Map<Identifier, Function<FriendlyByteBuf, CustomPacketPayload>> PAYLOAD_READERS = new HashMap<>();
    static {
        PAYLOAD_READERS.put(NumberPayload.ID, NumberPayload::read);
        PAYLOAD_READERS.put(StringPayload.ID, StringPayload::read);
        PAYLOAD_READERS.put(StringListPayload.ID, StringListPayload::read);
        PAYLOAD_READERS.put(LongPayload.ID, LongPayload::read);
        PAYLOAD_READERS.put(EmptyPayload.ID, EmptyPayload::read);
        PAYLOAD_READERS.put(BooleanPayload.ID, BooleanPayload::read);
        PAYLOAD_READERS.put(IntPayload.ID, IntPayload::read);
    }
    *///?} else {
	public static final List<CustomPacketPayload.TypeAndCodec<? super RegistryFriendlyByteBuf, ? extends CustomPacketPayload>> PAYLOADS = List.of(
			new CustomPacketPayload.TypeAndCodec<>(NumberPayload.ID, NumberPayload.CODEC)
			, new CustomPacketPayload.TypeAndCodec<>(StringPayload.ID, StringPayload.CODEC)
			, new CustomPacketPayload.TypeAndCodec<>(StringListPayload.ID, StringListPayload.CODEC)
			, new CustomPacketPayload.TypeAndCodec<>(LongPayload.ID, LongPayload.CODEC)
			, new CustomPacketPayload.TypeAndCodec<>(EmptyPayload.ID, EmptyPayload.CODEC)
			, new CustomPacketPayload.TypeAndCodec<>(BooleanPayload.ID, BooleanPayload.CODEC)
			, new CustomPacketPayload.TypeAndCodec<>(IntPayload.ID, IntPayload.CODEC)
	);
	//?}

	@Override
	public String modId() {
		return MatLib.MOD_ID;
	}
}
