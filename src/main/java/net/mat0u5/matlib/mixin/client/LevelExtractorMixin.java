package net.mat0u5.matlib.mixin.client;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.client.events.ClientEntityRenderEvents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;

//? if <= 26.1 {
/*import net.minecraft.client.renderer.LevelRenderer;
@Mixin(value = LevelRenderer.class, priority = 1)
*///?} else {
import net.minecraft.client.renderer.extract.LevelExtractor;
@Mixin(value = LevelExtractor.class, priority = 1)
//?}
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
public class LevelExtractorMixin {
    //? if <= 1.21 {
    /*@Redirect(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;entitiesForRendering()Ljava/lang/Iterable;"))
    *///?} else if <= 1.21.6 {
    /*@Redirect(method = "collectVisibleEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;entitiesForRendering()Ljava/lang/Iterable;"))
	*///?} else {
    @Redirect(method = "extractVisibleEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;entitiesForRendering()Ljava/lang/Iterable;"))
	//?}
	private Iterable<Entity> addMorphedEntities(ClientLevel instance) {
		if (ClientEntityRenderEvents.ENTITIES_FOR_RENDERING.listenerCount() == 0) return instance.entitiesForRendering();
		List<Entity> entities = new ArrayList<>();
		instance.entitiesForRendering().forEach(entities::add);
		ClientEntityRenderEvents.ENTITIES_FOR_RENDERING.invoker().modifyList(entities);
		return entities;
	}
}
