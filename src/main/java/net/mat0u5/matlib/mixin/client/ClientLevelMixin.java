package net.mat0u5.matlib.mixin.client;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.client.events.ClientEntityEvents;
import net.mat0u5.matlib.client.events.ClientLevelEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientLevel.class, priority = 1)
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
public class ClientLevelMixin {
	@Inject(method = "tickEntities", at = @At("TAIL"))
	private void onTickEntities(CallbackInfo ci) {
		ClientLevelEvents.TICK_ENTITIES.invoker().onTickEntities((ClientLevel) (Object) this);
	}

	@Inject(method = "tickNonPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V", shift = At.Shift.AFTER))
	private void tickEntity(Entity entity, CallbackInfo ci) {
		ClientEntityEvents.TICK_ENTITY.invoker().onTick(entity);
	}

	@Inject(method = "tickPassenger", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;rideTick()V", shift = At.Shift.AFTER))
	private void tickPassengerEntity(Entity vehicle, Entity passenger, CallbackInfo ci) {
		ClientEntityEvents.TICK_ENTITY.invoker().onTick(vehicle);
	}
}
