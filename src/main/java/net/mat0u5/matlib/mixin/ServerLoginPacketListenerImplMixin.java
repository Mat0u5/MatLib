package net.mat0u5.matlib.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.events.server.ServerNetworkEvents;
import net.mat0u5.matlib.network.NetworkHandlerServer;
import net.mat0u5.matlib.network.packets.CustomQueryPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

//? if <= 1.20 {
/*import net.minecraft.network.protocol.login.ServerboundCustomQueryPacket;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.server.MinecraftServer;
*///?} else {
import net.minecraft.network.protocol.login.ServerboundCustomQueryAnswerPacket;
import com.mojang.authlib.GameProfile;
//?}
//? if forge && <= 1.20 {
/*import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
*///?}

@Mixin(value = ServerLoginPacketListenerImpl.class, priority = 1)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public abstract class ServerLoginPacketListenerImplMixin {
    @Unique private boolean ml$queriesSent = false;
    @Unique private boolean ml$queriesAnswered = false;
    @Unique private final Map<Integer, Identifier> ml$pendingQueries = new ConcurrentHashMap<>();

    @Unique
    private void ml$sendQueries() {
        ServerLoginPacketListenerImpl self = (ServerLoginPacketListenerImpl)(Object)this;
        Map<Integer, Identifier> queries = ServerNetworkEvents.SEND_PRELOGIN_PACKETS.invoker().onSendPreLogin(self);

        ml$pendingQueries.putAll(queries);
        new TreeMap<>(queries).forEach((transactionId, id) -> NetworkHandlerServer.sendPreLoginPacket(self, transactionId, id));
    }

    //? if <= 1.20 {
    /*@Shadow @Final
    MinecraftServer server;
    @Shadow  ServerLoginPacketListenerImpl.State state;
    @Shadow public abstract void handleAcceptedLogin();

    @Inject(method = "handleAcceptedLogin", at = @At("HEAD"), cancellable = true)
    private void onHandleAcceptedLogin(CallbackInfo ci) {
        if (ml$queriesAnswered) {
            return;
        }

        if (!ml$queriesSent) {
            ml$queriesSent = true;
            ml$sendQueries();

            if (ml$pendingQueries.isEmpty()) {
                ml$queriesAnswered = true;
                return;
            }

            //? if !(forge && <= 1.20) {
            this.state = ServerLoginPacketListenerImpl.State.NEGOTIATING;
            //?}
        }

        ci.cancel();
    }

    @Inject(method = "handleCustomQueryPacket", at = @At("HEAD"), cancellable = true)
    private void onHandleAnswer(ServerboundCustomQueryPacket packet, CallbackInfo ci) {
        Identifier id = ml$pendingQueries.remove(packet.getTransactionId());
        if (id == null) return;

        FriendlyByteBuf data = packet.getData();
        boolean lastAnswer = ml$pendingQueries.isEmpty();
        //? if !forge
        if (lastAnswer) ml$queriesAnswered = true;

        ServerLoginPacketListenerImpl self = (ServerLoginPacketListenerImpl)(Object)this;

        this.server.execute(() -> {
            //? if !forge {
            NetworkHandlerServer.onPreLoginAnswer(self, id, data);
            if (!lastAnswer) return;

            this.state = ServerLoginPacketListenerImpl.State.READY_TO_ACCEPT;
            this.handleAcceptedLogin();
            //?} else {

            /^if (self.connection.getPacketListener() != self) return;

            NetworkHandlerServer.onPreLoginAnswer(self, id, data);
            if (!lastAnswer) return;

            ml$queriesAnswered = true;
            ^///?}
        });

        ci.cancel();
    }

    //? if forge {
    /^@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/network/NetworkHooks;tickNegotiation(Lnet/minecraft/server/network/ServerLoginPacketListenerImpl;Lnet/minecraft/network/Connection;Lnet/minecraft/server/level/ServerPlayer;)Z", remap = false))
    private boolean ml$queryBeforeNegotiation(ServerLoginPacketListenerImpl handler, Connection connection, ServerPlayer player, Operation<Boolean> original) {
        if (!ml$queriesSent) {
            ml$queriesSent = true;
            ml$sendQueries();

            if (ml$pendingQueries.isEmpty()) {
                ml$queriesAnswered = true;
            }
        }

        if (!ml$queriesAnswered) return false;
        return original.call(handler, connection, player);
    }
    ^///?}
    *///?} else {
    @Shadow public abstract void handleCustomQueryPacket(ServerboundCustomQueryAnswerPacket packet);

    @Unique private GameProfile ml$pendingProfile = null;

    @Inject(method = "finishLoginAndWaitForClient", at = @At("HEAD"), cancellable = true)
    private void interceptFinish(GameProfile profile, CallbackInfo ci) {
        if (ml$queriesAnswered) return;

        if (!ml$queriesSent) {
            ml$queriesSent = true;
            ml$pendingProfile = profile;
            ml$sendQueries();

            if (ml$pendingQueries.isEmpty()) {
                ml$queriesAnswered = true;
                return;
            }
        }
        ci.cancel();
    }

    @Inject(method = "handleCustomQueryPacket", at = @At("HEAD"), cancellable = true)
    private void onHandleAnswer(ServerboundCustomQueryAnswerPacket packet, CallbackInfo ci) {
        Identifier id = ml$pendingQueries.remove(packet.transactionId());
        if (id == null) return;

        FriendlyByteBuf data = packet.payload() instanceof CustomQueryPacket customPayload ? customPayload.data() : null;

        ServerLoginPacketListenerImpl self = (ServerLoginPacketListenerImpl)(Object)this;
        NetworkHandlerServer.onPreLoginAnswer(self, id, data);
        ci.cancel();

        if (!ml$pendingQueries.isEmpty()) return;
        ml$queriesAnswered = true;
        finishLogin(ml$pendingProfile);
    }

    @Shadow
    private void finishLoginAndWaitForClient(GameProfile profile) {}

    @Unique
    private void finishLogin(GameProfile profile) {
        finishLoginAndWaitForClient(profile);
    }
    //?}
}
