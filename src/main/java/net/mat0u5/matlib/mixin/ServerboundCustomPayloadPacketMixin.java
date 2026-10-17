package net.mat0u5.matlib.mixin;

//? if <= 1.20 || neoforge {
/*import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;

@Mixin(value = MinecraftServer.class)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public interface ServerboundCustomPayloadPacketMixin {
    //Empty class to avoid mixin errors
}
*///?} else {

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.network.NetworkHandlerCommon;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//? if <= 1.20.3 {
/*import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
*///?} else {
import org.spongepowered.asm.mixin.injection.ModifyArg;
//?}

@Mixin(ServerboundCustomPayloadPacket.class)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public class ServerboundCustomPayloadPacketMixin {
    //? if <= 1.20.3 {
    /*@Inject(method = "readPayload", at = @At("HEAD"), cancellable = true)
    private static void injectPayloads(Identifier id, FriendlyByteBuf buf, CallbackInfoReturnable<CustomPacketPayload> cir) {
        var payloadReaders = NetworkHandlerCommon.getPayloadReaders();
        if (payloadReaders.containsKey(id)) {
            cir.setReturnValue(payloadReaders.get(id).apply(buf));
        }
    }
    *///?} else {
    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload;codec(Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$FallbackProvider;Ljava/util/List;)Lnet/minecraft/network/codec/StreamCodec;"), index = 0)
    private static CustomPacketPayload.FallbackProvider<?> injectPayloads(CustomPacketPayload.FallbackProvider<?> original) {
        return NetworkHandlerCommon.wrapFallbackProvider(original);
    }
    //?}
}

//?}