package net.mat0u5.matlib.network.packets;
//? if <= 1.20.3 {
/*import net.mat0u5.matlib.utils.other.IdentifierHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record NumberPayload(String name, double number) implements CustomPacketPayload {

    public static final Identifier ID = IdentifierHelper.matlib("number");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(name);
        buf.writeDouble(number);
    }

    public static NumberPayload read(FriendlyByteBuf buf) {
        String name = buf.readUtf();
        double number = buf.readDouble();
        return new NumberPayload(name, number);
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

public record NumberPayload(String name, double number) implements CustomPacketPayload {

    public static final Type<NumberPayload> ID = new Type<>(IdentifierHelper.matlib("number"));
    public static final StreamCodec<RegistryFriendlyByteBuf, NumberPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, NumberPayload::name,
            ByteBufCodecs.DOUBLE, NumberPayload::number,
            NumberPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
//?}