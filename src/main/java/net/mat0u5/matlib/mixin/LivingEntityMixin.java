package net.mat0u5.matlib.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.events.EventResult;
import net.mat0u5.matlib.events.common.ServerEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LivingEntity.class, priority = 1)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public class LivingEntityMixin {
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
		if (result == EventResult.DENY) {
			ci.cancel();
		}
	}
}
