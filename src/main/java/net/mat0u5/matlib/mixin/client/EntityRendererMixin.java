package net.mat0u5.matlib.mixin.client;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import net.mat0u5.matlib.client.events.ClientEntityEvents;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.network.chat.Component;
//? if <= 1.21 {
/*import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
*///?} else {
import net.mat0u5.matlib.client.utils.interfaces.IEntityRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//?}

@Mixin(value = EntityRenderer.class, priority = 1)
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
//? if <= 1.21 {
/*public class EntityRendererMixin<T extends Entity> {
*///?} else {
public class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {
//?}

	//? if <= 1.21 {
	/*@WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getDisplayName()Lnet/minecraft/network/chat/Component;"))
	public Component render(Entity entity, Operation<Component> operation) {
		Component original = operation.call(entity);
		return ClientEntityEvents.RENDER_ENTITY_NAMETAG.invoker().getNameTag(entity, original).getOrDefault(original);
	}
	*///?} else {
	@Inject(method = "extractRenderState", at = @At("HEAD"))
	public void injectEntity(T entity, S state, float tickProgress, CallbackInfo ci) {
		if (state instanceof IEntityRenderState accessor) {
			accessor.ml$update(entity, tickProgress);
		}
	}

	@ModifyReturnValue(method = "getNameTag", at = @At("RETURN"))
	private @Nullable Component modifyName(@Nullable Component original, Entity entity) {
		return ClientEntityEvents.RENDER_ENTITY_NAMETAG.invoker().getNameTag(entity, original).getOrDefault(original);
	}
	//?}
}
