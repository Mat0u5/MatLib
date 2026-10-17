package net.mat0u5.matlib.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.events.common.CommonPlayerEvents;
import net.mat0u5.matlib.events.server.ServerPlayerEvents;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >= 1.20.2
import net.minecraft.server.network.CommonListenerCookie;
//? if = 1.21.6 {
/*import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.ValueInput;
*///?}

@Mixin(value = PlayerList.class, priority = 1)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public class PlayerListMixin {
	@Inject(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundPlayerAbilitiesPacket;<init>(Lnet/minecraft/world/entity/player/Abilities;)V"))
	//? if <= 1.20 {
	/*private void handlePlayerConnection(Connection connection, ServerPlayer player, CallbackInfo ci) {
	*///?} else {
	private void handlePlayerConnection(Connection connection, ServerPlayer player, CommonListenerCookie arg, CallbackInfo ci) {
	//?}
		ServerPlayerEvents.CONNECT.invoker().onConnect(connection, player);
		CommonPlayerEvents.UPDATE_INVENTORY.invoker().onUpdateInventory(player, player.getInventory());
	}
}

	//? if <= 1.21.6 {
	/*@Inject(method = "load", at = @At(value = "RETURN", shift = At.Shift.BEFORE))
	//? if <= 1.21.5 {
	/^private void fixFakePlayerPosition(ServerPlayer player, CallbackInfoReturnable<Optional<CompoundTag>> cir) {
	^///?} else {
	private void fixFakePlayerPosition(ServerPlayer player, ProblemReporter errorReporter, CallbackInfoReturnable<Optional<ValueInput>> cir) {
	//?}
		if (player instanceof FakeServerPlayer fakePlayer) {
			fakePlayer.fixStartingPosition.run();
		}
	}
	*///?}
