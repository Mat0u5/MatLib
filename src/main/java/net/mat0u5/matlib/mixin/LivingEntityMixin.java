package net.mat0u5.matlib.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.events.EventResult;
import net.mat0u5.matlib.events.common.CommonEntityEvents;
import net.mat0u5.matlib.events.server.ServerEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class, priority = 1)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public class LivingEntityMixin {
	@Inject(method = "die", at = @At("HEAD"), cancellable = true)
	private void preDeath(DamageSource source, CallbackInfo ci) {
		EventResult result = ServerEntityEvents.PRE_DEATH.invoker().onPreDeath((LivingEntity) (Object) this, source);
		if (result.isDeny()) {
			ci.cancel();
		}
	}

	@Inject(method = "die", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;broadcastEntityEvent(Lnet/minecraft/world/entity/Entity;B)V"))
	private void notifyDeath(DamageSource source, CallbackInfo ci) {
		ServerEntityEvents.DEATH.invoker().onDeath((LivingEntity) (Object) this, source);
	}

	@Inject(method = "dropAllDeathLoot", at = @At("HEAD"), cancellable = true)
	//? if <= 1.20.5 {
	/*private void onDrop(DamageSource damageSource, CallbackInfo ci) {
	*///?} else {
	private void onDrop(ServerLevel level, DamageSource damageSource, CallbackInfo ci) {
	//?}
		EventResult result = ServerEntityEvents.DROP_LOOT.invoker().onDropLoot((LivingEntity) (Object) this, damageSource);
		if (result.isDeny()) {
			ci.cancel();
		}
	}

	@Inject(method = "jumpFromGround", at = @At("TAIL"))
	private void onJump(CallbackInfo ci) {
		CommonEntityEvents.JUMP.invoker().onJump((LivingEntity) (Object) this);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void onTick(CallbackInfo ci) {
		CommonEntityEvents.TICK.invoker().onTickStart((LivingEntity) (Object) this);
	}

	//? if <= 1.21 {
    /*@Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void onPreDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
    *///?} else {
	@Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
	private void onPreDamage(ServerLevel level, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		//?}
		EventResult result = ServerEntityEvents.PRE_DAMAGE.invoker().onPreDamage((LivingEntity) (Object) this, source, amount);
		if (result.isAllow()) cir.setReturnValue(true);
		if (result.isDeny()) cir.setReturnValue(false);
	}

	@Inject(method = "actuallyHurt", at = @At("HEAD"))
	//? if <= 1.21 {
	/*private void onApplyDamage(DamageSource source, float amount, CallbackInfo ci) {
	*///?} else {
	private void onDamage(ServerLevel level, DamageSource source, float amount, CallbackInfo ci) {
	//?}
		ServerEntityEvents.DAMAGE.invoker().onDamage((LivingEntity) (Object) this, source, amount);
	}


	@Inject(method = "heal", at = @At("HEAD"), cancellable = true)
	private void onPreHeal(float amount, CallbackInfo ci) {
		EventResult result = ServerEntityEvents.PRE_HEAL.invoker().onPreHeal((LivingEntity) (Object) this, amount);
		if (result.isDeny()) {
			ci.cancel();
		}
	}

	@Inject(method = "heal", at = @At("TAIL"))
	private void onHeal(float amount, CallbackInfo ci) {
		ServerEntityEvents.HEAL.invoker().onHeal((LivingEntity) (Object) this, amount);
	}
}
