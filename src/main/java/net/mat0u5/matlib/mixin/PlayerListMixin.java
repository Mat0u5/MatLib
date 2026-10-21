package net.mat0u5.matlib.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.events.common.CommonPlayerEvents;
import net.mat0u5.matlib.events.server.ServerChatEvents;
import net.mat0u5.matlib.events.server.ServerPlayerEvents;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;
import java.util.function.Predicate;

//? if >= 1.20.2
import net.minecraft.server.network.CommonListenerCookie;
//? if > 1.20.5
import net.minecraft.world.entity.Entity;
//? if <= 1.21.6 {
/*import net.mat0u5.matlib.entity.fakeplayer.FakeServerPlayer;
import java.util.Optional;
*///?}
//? if <= 1.21.5
//import net.minecraft.nbt.CompoundTag;
//? if = 1.21.6 {
/*import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.ValueInput;
*///?}

@Mixin(value = PlayerList.class, priority = 1)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public class PlayerListMixin {
	@Inject(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundPlayerAbilitiesPacket;<init>(Lnet/minecraft/world/entity/player/Abilities;)V"))
	//? if <= 1.20 {
	//private void handlePlayerConnection(Connection connection, ServerPlayer player, CallbackInfo ci) {
	//?} else {
	private void handlePlayerConnection(Connection connection, ServerPlayer player, CommonListenerCookie arg, CallbackInfo ci) {
	//?}
		ServerPlayerEvents.CONNECT.invoker().onConnect(connection, player);
		CommonPlayerEvents.UPDATE_INVENTORY.invoker().onUpdateInventory(player, player.getInventory());
	}

	@Inject(method = "respawn", at = @At("RETURN"))
	//? if <= 1.20.5 {
	//private void onRespawn(ServerPlayer player, boolean alive, CallbackInfoReturnable<ServerPlayer> cir) {
	//?} else {
	private void onRespawn(ServerPlayer player, boolean alive, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayer> cir) {
	//?}
		//? if <= 1.20.5 {
		//if (alive) return;
		 //?} else {
		if (alive || removalReason != Entity.RemovalReason.KILLED) return;
		//?}
		ServerPlayerEvents.RESPAWN.invoker().onRespawn(cir.getReturnValue());
	}

	@Inject(method = "broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Ljava/util/function/Function;Z)V", at = @At("HEAD"), cancellable = true)
	private void onBroadcastSystem(Component message, Function<ServerPlayer, Component> playerMessageFactory, boolean overlay, CallbackInfo ci) {
		if (ServerChatEvents.BROADCAST_SYSTEM.invoker().onBroadcastSystem(message, overlay).isDeny()) {
			ci.cancel();
		}
	}

	@Inject(method = "broadcastChatMessage(Lnet/minecraft/network/chat/PlayerChatMessage;Ljava/util/function/Predicate;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/network/chat/ChatType$Bound;)V", at = @At("HEAD"), cancellable = true)
	private void onBroadcastChat(PlayerChatMessage message, Predicate<ServerPlayer> shouldSendFiltered, @Nullable ServerPlayer sender, ChatType.Bound params, CallbackInfo ci) {
		if (ServerChatEvents.BROADCAST_CHAT.invoker().onBroadcastChat(message, sender, params).isDeny()) {
			ci.cancel();
		}
	}

	//? if <= 1.21.6 {
	/*@Inject(method = "load", at = @At(value = "RETURN", shift = At.Shift.BEFORE))
	//? if <= 1.21.5 {
	//private void fixFakePlayerPosition(ServerPlayer player, CallbackInfoReturnable<Optional<CompoundTag>> cir) {
	//?} else {
	private void fixFakePlayerPosition(ServerPlayer player, ProblemReporter errorReporter, CallbackInfoReturnable<Optional<ValueInput>> cir) {
	//?}
		if (player instanceof FakeServerPlayer fakePlayer) {
			fakePlayer.fixStartingPosition.run();
		}
	}
	*///?}
}
