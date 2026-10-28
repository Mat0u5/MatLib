package net.mat0u5.matlib.client.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.client.events.ClientNetworkEvents;
import net.mat0u5.matlib.events.OptionalEventReturn;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientHandshakePacketListenerImpl.class, priority = 1)
@MixinEnvironment(value = "client", type = MixinEnvironment.Env.CLIENT)
public class ClientHandshakePacketListenerImplMixin {
    @Inject(method = "handleCustomQuery", at = @At("HEAD"), cancellable = true)
    private void handleCustomQuery(ClientboundCustomQueryPacket packet, CallbackInfo ci) {
        ClientHandshakePacketListenerImpl handler = (ClientHandshakePacketListenerImpl) (Object) this;

        OptionalEventReturn<Packet<?>> response = ClientNetworkEvents.RECEIVE_PRELOGIN_PACKET.invoker().onReceivePreLogin(handler.connection, packet);
        if (!response.isPass()) {
            handler.connection.send(response.get());
            ci.cancel();
        }
    }
}