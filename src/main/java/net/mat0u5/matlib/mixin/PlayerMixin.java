package net.mat0u5.matlib.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.events.EventResult;
import net.mat0u5.matlib.events.common.CommonPlayerEvents;
import net.mat0u5.matlib.events.server.ServerEntityEvents;
import net.mat0u5.matlib.events.server.ServerPlayerEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Player.class, priority = 1)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public class PlayerMixin {

	@Inject(method = "attack", at = @At("HEAD"), cancellable = true)
	public void onPlayerInteractEntity(Entity target, CallbackInfo ci) {
		InteractionResult result = CommonPlayerEvents.ATTACK_ENTITY.invoker().onAttackEntity((Player) (Object) this, target);

		if (result != InteractionResult.PASS) {
			ci.cancel();
		}
	}

	@Inject(method = "attack", at = @At("TAIL"))
	private void onAttackEntity(Entity target, CallbackInfo ci) {
		Player player = (Player) (Object) this;
		CommonPlayerEvents.UPDATE_INVENTORY.invoker().onUpdateInventory(player, player.getInventory());
	}

	//? if <= 1.21 {
    /*@Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void onPreDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
    *///?} else {
	@Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
	private void onPreDamage(ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
	//?}
		EventResult result = ServerPlayerEvents.PRE_DAMAGE.invoker().onPreDamage((Player) (Object) this, source, amount);
		if (result.isAllow()) cir.setReturnValue(true);
		if (result.isDeny()) cir.setReturnValue(false);
	}

	@Inject(method = "actuallyHurt", at = @At("HEAD"))
	//? if <= 1.21 {
	/*private void onApplyDamage(DamageSource source, float amount, CallbackInfo ci) {
	*///?} else {
	private void onDamage(ServerLevel level, DamageSource source, float amount, CallbackInfo ci) {
	//?}
		ServerPlayerEvents.DAMAGE.invoker().onDamage((Player) (Object) this, source, amount);
		ServerEntityEvents.DAMAGE.invoker().onDamage((Player) (Object) this, source, amount);
	}
}
