package net.mat0u5.matlib.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.events.server.ServerPlayerEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if <= 1.20.5 {
/*import net.minecraft.network.chat.Component;
 *///?} else {
import net.minecraft.network.DisconnectionDetails;
//?}

@Mixin(value = ServerGamePacketListenerImpl.class, priority = 1)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public class ServerGamePacketListenerImplMixin {
	@Shadow
	public ServerPlayer player;

	@Inject(method = "onDisconnect", at = @At("HEAD"))
	//? if <= 1.20.5 {
	/*private void onDisconnect(Component details, CallbackInfo ci) {
	*///?} else {
	private void onDisconnect(DisconnectionDetails details, CallbackInfo ci) {
	//?}
		ServerPlayerEvents.DISCONNECT.invoker().onDisconnect(details, this.player);
	}
}
