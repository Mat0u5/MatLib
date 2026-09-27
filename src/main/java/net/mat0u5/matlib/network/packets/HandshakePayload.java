package net.mat0u5.matlib.network.packets;
//? if <= 1.20.3 {
/*import net.mat0u5.matlib.utils.other.IdentifierHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public record HandshakePayload(List<String> modIds, List<String> modVersions, List<String> modReadableNames) implements CustomPacketPayload {
    public static final Identifier ID = IdentifierHelper.matlib("handshake");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(modIds.size());
        for (String str : modIds) {
            buf.writeUtf(str);
        }
        buf.writeInt(modVersions.size());
        for (String str : modVersions) {
            buf.writeUtf(str);
        }
        buf.writeInt(modReadableNames.size());
        for (String str : modReadableNames) {
            buf.writeUtf(str);
        }
    }

    public static HandshakePayload read(FriendlyByteBuf buf) {
        int modIdsSize = buf.readInt();
        List<String> modIds = new ArrayList<>();
        for (int i = 0; i < modIdsSize; i++) {
            modIds.add(buf.readUtf());
        }
        int modVersionsSize = buf.readInt();
        List<String> modVersions = new ArrayList<>();
        for (int i = 0; i < modVersionsSize; i++) {
            modVersions.add(buf.readUtf());
        }
        int modReadableNamesSize = buf.readInt();
        List<String> modReadableNames = new ArrayList<>();
        for (int i = 0; i < modReadableNamesSize; i++) {
            modReadableNames.add(buf.readUtf());
        }
        return new HandshakePayload(modIds, modVersions, modReadableNames);
    }

    @Override
    public Identifier id() {
        return ID;
    }
}
*///?} else {
import net.mat0u5.matlib.utils.other.IdentifierHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.List;

public record HandshakePayload(List<String> modIds, List<String> modVersions, List<String> modReadableNames) implements CustomPacketPayload {
    public static final Type<HandshakePayload> ID = new Type<>(IdentifierHelper.matlib( "handshake"));
    public static final StreamCodec<RegistryFriendlyByteBuf, HandshakePayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), HandshakePayload::modIds,
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), HandshakePayload::modVersions,
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), HandshakePayload::modReadableNames,
            HandshakePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
//?}