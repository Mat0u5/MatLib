package net.mat0u5.matlib.client.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import io.netty.channel.ChannelHandlerContext;
import net.mat0u5.matlib.client.events.ClientLocalPlayerEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.status.ClientStatusPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(Connection.class)
@MixinEnvironment(value = "client", type = MixinEnvironment.Env.CLIENT)
public class ConnectionMixin {
    @Unique
    private final AtomicBoolean ml$leaveFired = new AtomicBoolean(false);

    @Inject(method = "channelInactive", at = @At("HEAD"))
    private void disconnectAddon(ChannelHandlerContext channelHandlerContext, CallbackInfo ci) {
        ml$fireLeave();
    }

    //? if <= 1.20.5 {
    //@Inject(method = "handleDisconnection", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketListener;onDisconnect(Lnet/minecraft/network/chat/Component;)V"))
    //?} else {
    @Inject(method = "handleDisconnection", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketListener;onDisconnect(Lnet/minecraft/network/DisconnectionDetails;)V"))
    //?}
    private void disconnectAddon(CallbackInfo ci) {
        ml$fireLeave();
    }

    @Unique
    private void ml$fireLeave() {
        Connection connection = (Connection) (Object) this;
        if (connection.getReceiving() != PacketFlow.CLIENTBOUND) return;
        if (connection.getPacketListener() instanceof ClientStatusPacketListener) return;
        if (!ml$leaveFired.compareAndSet(false, true)) return;

        Minecraft client = Minecraft.getInstance();
        if (client.isSameThread()) {
            ClientLocalPlayerEvents.LEAVE.invoker().onLeave();
        }
        else {
            client.execute(() -> ClientLocalPlayerEvents.LEAVE.invoker().onLeave());
        }
    }
}
