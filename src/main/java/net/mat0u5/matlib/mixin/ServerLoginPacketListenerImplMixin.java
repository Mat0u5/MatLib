package net.mat0u5.matlib.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import io.netty.buffer.Unpooled;
import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.network.MatLibNetworkHandlerServer;
import net.mat0u5.matlib.network.packets.CustomQueryPacket;
import net.mat0u5.matlib.utils.other.IdentifierHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

//? if <= 1.20 {
/*import net.minecraft.network.protocol.login.ServerboundCustomQueryPacket;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.server.MinecraftServer;
*///?} else {
import net.minecraft.network.protocol.login.ServerboundCustomQueryAnswerPacket;
import net.minecraft.network.protocol.login.custom.DiscardedQueryPayload;
import com.mojang.authlib.GameProfile;
//?}


@Mixin(value = ServerLoginPacketListenerImpl.class, priority = 1)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public abstract class ServerLoginPacketListenerImplMixin {
    @Unique private boolean ml$querySent = false;
    @Unique private boolean ml$queryAnswered = false;

    //? if <= 1.20 {
    /*@Shadow @Final
    MinecraftServer server;
    @Shadow  ServerLoginPacketListenerImpl.State state;
    @Shadow public abstract void handleAcceptedLogin();

    @Inject(method = "handleAcceptedLogin", at = @At("HEAD"), cancellable = true)
    private void onHandleAcceptedLogin(CallbackInfo ci) {
        if (ml$queryAnswered) {
            return;
        }

        if (!ml$querySent) {
            ml$querySent = true;

            ServerLoginPacketListenerImpl self = (ServerLoginPacketListenerImpl)(Object)this;
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

            self.connection.send(new ClientboundCustomQueryPacket(
                    MatLibNetworkHandlerServer.PRELOGIN_TRANSACTION_ID,
                    IdentifierHelper.matlib(MatLibNetworkHandlerServer.preLoginPacketID),
                    buf
            ));

            //? if !(forge && <= 1.20) {
            this.state = ServerLoginPacketListenerImpl.State.NEGOTIATING;
            //?}
        }

        ci.cancel();
    }

    @Inject(method = "handleCustomQueryPacket", at = @At("HEAD"), cancellable = true)
    private void onHandleAnswer(ServerboundCustomQueryPacket packet, CallbackInfo ci) {
        if (packet.getTransactionId() != MatLibNetworkHandlerServer.PRELOGIN_TRANSACTION_ID) return;

        boolean understood = false;
        List<String> modIds = new ArrayList<>();

        if (packet.getData() != null) {
            FriendlyByteBuf buf = packet.getData();
            try {
                if (buf.isReadable()) {
                    understood = buf.readBoolean();
                    int modCount = buf.readInt();
                    for (int i = 0; i < modCount; i++) {
                        modIds.add(buf.readUtf());
                    }
                }
            } catch (Exception e) {
                MatLib.LOGGER.error("Failed to read pre-login handshake packet", e);
            }
        }

        ml$queryAnswered = true;

        ServerLoginPacketListenerImpl self = (ServerLoginPacketListenerImpl)(Object)this;

        boolean finalUnderstood = understood;
        this.server.execute(() -> {
            //? if !forge {
            MatLibNetworkHandlerServer.handlePreLogin(finalUnderstood, modIds, self);

            this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
            this.handleAcceptedLogin();
            //?} else {

            /^if (self.connection.getPacketListener() != self) return;

            MatLibNetworkHandlerServer.handlePreLogin(finalUnderstood, modIds, self);

            this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
            ^///?}
        });

        ci.cancel();
    }
    *///?} else {
    @Shadow public abstract void handleCustomQueryPacket(ServerboundCustomQueryAnswerPacket packet);

    @Unique private GameProfile ml$pendingProfile = null;

    @Inject(method = "finishLoginAndWaitForClient", at = @At("HEAD"), cancellable = true)
    private void interceptFinish(GameProfile profile, CallbackInfo ci) {
        if (ml$queryAnswered) return;

        if (!ml$querySent) {
            ml$querySent = true;
            ml$pendingProfile = profile;

            ServerLoginPacketListenerImpl self = (ServerLoginPacketListenerImpl)(Object)this;
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            DiscardedQueryPayload payload = new DiscardedQueryPayload(IdentifierHelper.matlib(MatLibNetworkHandlerServer.preLoginPacketID));
            payload.write(buf);
            self.connection.send(new ClientboundCustomQueryPacket(
                    MatLibNetworkHandlerServer.PRELOGIN_TRANSACTION_ID, payload
            ));
        }
        ci.cancel();
    }

    @Inject(method = "handleCustomQueryPacket", at = @At("HEAD"), cancellable = true)
    private void onHandleAnswer(ServerboundCustomQueryAnswerPacket packet, CallbackInfo ci) {
        if (packet.transactionId() != MatLibNetworkHandlerServer.PRELOGIN_TRANSACTION_ID) return;

        boolean understood = false;
        List<String> modIds = new ArrayList<>();

        if (packet.payload() instanceof CustomQueryPacket customPayload) {
            FriendlyByteBuf buf = customPayload.data();
            try {
                if (buf.isReadable()) {
                    understood = buf.readBoolean();
                    int modCount = buf.readInt();
                    for (int i = 0; i < modCount; i++) {
                        modIds.add(buf.readUtf());
                    }
                }
            } catch (Exception e) {
                MatLib.LOGGER.error("Failed to read pre-login handshake packet", e);
            }
        }
        ml$queryAnswered = true;

        ServerLoginPacketListenerImpl self = (ServerLoginPacketListenerImpl)(Object)this;
        MatLibNetworkHandlerServer.handlePreLogin(understood, modIds, self);
        finishLogin(ml$pendingProfile);
        ci.cancel();
    }

    @Shadow
    private void finishLoginAndWaitForClient(GameProfile profile) {}

    @Unique
    private void finishLogin(GameProfile profile) {
        finishLoginAndWaitForClient(profile);
    }
    //?}
}