package net.mat0u5.matlib.client.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.client.events.ClientEntityEvents;
import net.mat0u5.matlib.client.events.ClientEntityRenderEvents;
import net.mat0u5.matlib.client.events.ClientLevelEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = ClientLevel.class, priority = 1)
@MixinEnvironment(value = "client", type = MixinEnvironment.Env.CLIENT)
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
		ClientEntityEvents.TICK_ENTITY.invoker().onTick(passenger);
	}

	@Inject(method = "entitiesForRendering", at = @At("RETURN"), cancellable = true)
	private void modifyEntitiesForRendering(CallbackInfoReturnable<Iterable<Entity>> cir) {
		if (ClientEntityRenderEvents.ENTITIES_FOR_RENDERING.listenerCount() == 0) return;
		List<Entity> entities = new ArrayList<>();
		cir.getReturnValue().forEach(entities::add);
		ClientEntityRenderEvents.ENTITIES_FOR_RENDERING.invoker().modifyList(entities);
		cir.setReturnValue(entities);
	}
}
