package net.mat0u5.matlib.mixin.client;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.client.events.ClientEntityRenderEvents;
import net.mat0u5.matlib.events.EventResult;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityRenderDispatcher.class, priority = 1)
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
public class EntityRenderDispatcherMixin {
	@Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
	//? if <= 26.2 {
	/*public <E extends Entity> void render(E entity, Frustum culler, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir) {
	*///?} else {
	public void render(Entity entity, Frustum culler, double camX, double camY, double camZ, float partialTicks, CallbackInfoReturnable<Boolean> cir) {
	 //?}
	 	if (ClientEntityRenderEvents.SHOULD_RENDER.listenerCount() == 0) return;
		EventResult result = ClientEntityRenderEvents.SHOULD_RENDER.invoker().shouldRender(entity, culler, camX, camY, camZ);
		if (result.isAllow()) cir.setReturnValue(true);
		if (result.isDeny()) cir.setReturnValue(false);
		// Else pass -> vanilla
	}
}
