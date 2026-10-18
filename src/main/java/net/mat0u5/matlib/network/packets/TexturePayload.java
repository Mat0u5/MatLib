package net.mat0u5.matlib.network.packets;
//? if <= 1.20.3 {
/*import net.mat0u5.matlib.utils.other.IdentifierHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record TexturePayload(String folderId, String name, byte[] textureData) implements CustomPacketPayload {

    public static final Identifier ID = IdentifierHelper.matlib("texture");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(folderId);
        buf.writeUtf(name);
        buf.writeByteArray(textureData);
    }

    public static TexturePayload read(FriendlyByteBuf buf) {
        return new TexturePayload(buf.readUtf(), buf.readUtf(), buf.readByteArray());
    }

    @Override
    public Identifier id() {
        return ID;
    }
}
*///?} else {
import net.mat0u5.matlib.utils.other.IdentifierHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record TexturePayload(String folderId, String name, byte[] textureData) implements CustomPacketPayload {
    public static final Type<TexturePayload> ID = new Type<>(IdentifierHelper.matlib("texture"));

    public static final StreamCodec<FriendlyByteBuf, TexturePayload> CODEC = StreamCodec.ofMember(TexturePayload::write, TexturePayload::new);

    public TexturePayload(FriendlyByteBuf buf) {
        this(buf.readUtf(), buf.readUtf(), buf.readByteArray());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(folderId);
        buf.writeUtf(name);
        buf.writeByteArray(textureData);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
//?}
