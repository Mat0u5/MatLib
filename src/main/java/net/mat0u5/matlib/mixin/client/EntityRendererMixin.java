package net.mat0u5.matlib.mixin.client;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
//? if >= 1.21.2 {
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.mat0u5.matlib.client.util.interfaces.IEntityRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
//?}

@Mixin(value = EntityRenderer.class, priority = 1)
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
//? if <= 1.21 {
/*public class EntityRendererMixin<T extends Entity> {
*///?} else {
public class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {
//?}

	//? if >= 1.21.2 {
	@Inject(method = "extractRenderState", at = @At("HEAD"))
	public void injectEntity(T entity, S state, float tickProgress, CallbackInfo ci) {
		if (state instanceof IEntityRenderState accessor) {
			accessor.ml$update(entity, tickProgress);
		}
	}
	//?}
}
