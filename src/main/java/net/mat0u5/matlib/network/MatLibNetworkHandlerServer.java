package net.mat0u5.matlib.network;

import com.google.auto.service.AutoService;
import net.mat0u5.matlib.events.common.CommonRegistryEvents;
import net.mat0u5.matlib.events.server.ServerNetworkEvents;
import net.mat0u5.matlib.network.packets.*;
import net.mat0u5.matlib.services.RegistrableServer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

//? if <= 1.20.3 {
/*import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import java.util.function.Function;
*///?} else {
import net.minecraft.network.RegistryFriendlyByteBuf;
//?}

@AutoService(RegistrableServer.class)
public class MatLibNetworkHandlerServer implements RegistrableServer {

	//? if <= 1.20.3 {
    /*private static final Map<Identifier, Function<FriendlyByteBuf, CustomPacketPayload>> SIMPLE_PACKET_PAYLOADS = new HashMap<>();
    static {
        SIMPLE_PACKET_PAYLOADS.put(DoublePayload.ID, DoublePayload::read);
        SIMPLE_PACKET_PAYLOADS.put(StringPayload.ID, StringPayload::read);
        SIMPLE_PACKET_PAYLOADS.put(StringListPayload.ID, StringListPayload::read);
        SIMPLE_PACKET_PAYLOADS.put(LongPayload.ID, LongPayload::read);
        SIMPLE_PACKET_PAYLOADS.put(EmptyPayload.ID, EmptyPayload::read);
        SIMPLE_PACKET_PAYLOADS.put(BooleanPayload.ID, BooleanPayload::read);
        SIMPLE_PACKET_PAYLOADS.put(IntPayload.ID, IntPayload::read);
    }
    *///?} else {
	private static final List<CustomPacketPayload.TypeAndCodec<? super RegistryFriendlyByteBuf, ? extends CustomPacketPayload>> SIMPLE_PACKET_PAYLOADS = List.of(
			new CustomPacketPayload.TypeAndCodec<>(DoublePayload.ID, DoublePayload.CODEC)
			, new CustomPacketPayload.TypeAndCodec<>(StringPayload.ID, StringPayload.CODEC)
			, new CustomPacketPayload.TypeAndCodec<>(StringListPayload.ID, StringListPayload.CODEC)
			, new CustomPacketPayload.TypeAndCodec<>(LongPayload.ID, LongPayload.CODEC)
			, new CustomPacketPayload.TypeAndCodec<>(EmptyPayload.ID, EmptyPayload.CODEC)
			, new CustomPacketPayload.TypeAndCodec<>(BooleanPayload.ID, BooleanPayload.CODEC)
			, new CustomPacketPayload.TypeAndCodec<>(IntPayload.ID, IntPayload.CODEC)
	);
	//?}

	@Override
	public void onRegister() {
		CommonRegistryEvents.PACKET_PAYLOADS.register(() -> SIMPLE_PACKET_PAYLOADS);
		ServerNetworkEvents.RECEIVE_CUSTOM_PACKET.register(MatLibNetworkHandlerServer::onCustomPayload);
	}

	public static boolean onCustomPayload(CustomPacketPayload customPacketPayload, ServerPlayer player) {
		return false;
	}
}
