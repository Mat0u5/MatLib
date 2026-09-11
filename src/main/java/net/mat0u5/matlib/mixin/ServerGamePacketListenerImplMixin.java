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

//? if >= 26.1 {
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
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

	//? if >= 26.1 {
	@Inject(method = "handleInteract", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"), cancellable = true)
	public void handleInteract(ServerboundInteractPacket packet, CallbackInfo info, @Local(name = "target") Entity target) {
		Level level = player.level();

		//~ !renames_1_21_11
		EntityHitResult hitResult = new EntityHitResult(target, packet.location().add(target.getX(), target.getY(), target.getZ()));
		//~ renames_1_21_11
		InteractionResult result = ServerPlayerEvents.CLICK_ENTITY.invoker().onClickEntity(player, level, packet.hand(), target, hitResult);

		if (result != InteractionResult.PASS) {
			info.cancel();
		}
	}
	//?}
}
