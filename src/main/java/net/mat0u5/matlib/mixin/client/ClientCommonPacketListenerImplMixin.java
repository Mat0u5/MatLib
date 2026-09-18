package net.mat0u5.matlib.mixin.client;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.events.EventResult;
import net.mat0u5.matlib.client.events.ClientPackSourceEvents;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if <= 1.20 {
/*import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundResourcePackPacket;
import net.minecraft.network.protocol.game.ServerboundResourcePackPacket;
*///?} else {
import java.util.UUID;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
//? if <= 1.20.2 {
/*import net.minecraft.network.protocol.common.ClientboundResourcePackPacket;
*///?} else {
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
//?}
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
//?}

//? if <= 1.20 {
/*@Mixin(ClientPacketListener.class)
*///?} else {
@Mixin(ClientCommonPacketListenerImpl.class)
//?}
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
public class ClientCommonPacketListenerImplMixin {

    @Shadow
    @Final
    protected Connection connection;

    //? if <= 1.20 {
    /*@Inject(method = "handleResourcePack",at = @At(target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;parseResourcePackUrl(Ljava/lang/String;)Ljava/net/URL;", shift = At.Shift.AFTER, value = "INVOKE" ), cancellable = true)
    public void onResourcePackSend(ClientboundResourcePackPacket packet, CallbackInfo ci) {
    *///?} else if <= 1.20.2 {
    /*@Inject(method = "handleResourcePack",at = @At(target = "Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl;parseResourcePackUrl(Ljava/lang/String;)Ljava/net/URL;", shift = At.Shift.AFTER, value = "INVOKE" ), cancellable = true)
    public void onResourcePackSend(ClientboundResourcePackPacket packet, CallbackInfo ci) {
    *///?} else {
    @Inject(method = "handleResourcePackPush",at = @At(target = "Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl;parseResourcePackUrl(Ljava/lang/String;)Ljava/net/URL;", shift = At.Shift.AFTER, value = "INVOKE" ), cancellable = true)
    public void onResourcePackSend(ClientboundResourcePackPushPacket packet, CallbackInfo ci) {
    //?}
        //? if <= 1.20.2 {
        /*String url = packet.getUrl();
        *///?} else {
        String url = packet.url();
        UUID uuid = packet.id();
        //?}

		EventResult result = ClientPackSourceEvents.SERVER_PACK_DOWNLOAD.invoker().onPackDownload(url);

        if (!result.isDeny()) return;
        MatLib.LOGGER.info("Skipping resourcepack download ({})", url);
        //? if <= 1.20.2 {
        /*this.connection.send(new ServerboundResourcePackPacket(ServerboundResourcePackPacket.Action.ACCEPTED));
        this.connection.send(new ServerboundResourcePackPacket(ServerboundResourcePackPacket.Action.SUCCESSFULLY_LOADED));
        *///?} else {
        this.connection.send(new ServerboundResourcePackPacket(uuid, ServerboundResourcePackPacket.Action.ACCEPTED));
        this.connection.send(new ServerboundResourcePackPacket(uuid, ServerboundResourcePackPacket.Action.DOWNLOADED));
        this.connection.send(new ServerboundResourcePackPacket(uuid, ServerboundResourcePackPacket.Action.SUCCESSFULLY_LOADED));
        //?}
        ci.cancel();
    }
}
