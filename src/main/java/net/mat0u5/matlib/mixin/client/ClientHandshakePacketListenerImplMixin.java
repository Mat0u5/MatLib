package net.mat0u5.matlib.mixin.client;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import io.netty.buffer.Unpooled;
import net.mat0u5.matlib.MatLibVersionControl;
import net.mat0u5.matlib.network.MatLibNetworkHandlerServer;
import net.mat0u5.matlib.utils.other.IdentifierHelper;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

//? if <= 1.20 {
/*import net.minecraft.network.protocol.login.ServerboundCustomQueryPacket;
*///?} else {
import net.minecraft.network.protocol.login.ServerboundCustomQueryAnswerPacket;
import net.mat0u5.matlib.network.packets.CustomQueryPacket;
//?}

@Mixin(value = ClientHandshakePacketListenerImpl.class, priority = 1)
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
public class ClientHandshakePacketListenerImplMixin {
    @Inject(method = "handleCustomQuery", at = @At("HEAD"), cancellable = true)
    private void handleCustomQuery(ClientboundCustomQueryPacket packet, CallbackInfo ci) {
        //? if <= 1.20 {

        /*if (packet.getIdentifier().equals(IdentifierHelper.matlib(MatLibNetworkHandlerServer.preLoginPacketID))) {
            ClientHandshakePacketListenerImpl handler = (ClientHandshakePacketListenerImpl) (Object) this;

            FriendlyByteBuf responseBuf = new FriendlyByteBuf(Unpooled.buffer());
            responseBuf.writeBoolean(true);
			List<String> modIds = MatLibVersionControl.getTrackedModIds();
            responseBuf.writeInt(modIds.size());
            for (String str : modIds) {
                responseBuf.writeUtf(str);
            }

            handler.connection.send(new ServerboundCustomQueryPacket(packet.getTransactionId(), responseBuf));
            ci.cancel();
        }
        *///?} else {
        if (packet.payload().id().equals(IdentifierHelper.matlib(MatLibNetworkHandlerServer.preLoginPacketID))) {
            ClientHandshakePacketListenerImpl handler = (ClientHandshakePacketListenerImpl) (Object) this;

            FriendlyByteBuf responseBuf = new FriendlyByteBuf(Unpooled.buffer());
            responseBuf.writeBoolean(true);
			List<String> modIds = MatLibVersionControl.getTrackedModIds();
            responseBuf.writeInt(modIds.size());
            for (String str : modIds) {
                responseBuf.writeUtf(str);
            }

            CustomQueryPacket answerPayload = new CustomQueryPacket(responseBuf);

            handler.connection.send(new ServerboundCustomQueryAnswerPacket(packet.transactionId(), answerPayload));
            ci.cancel();
        }
        //?}
    }
}