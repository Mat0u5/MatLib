package net.mat0u5.matlib.client.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.client.events.ClientRegistryEvents;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.WritableRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

//? if <= 26.3 {
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//?} else {
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//?}

@Mixin(EntityRenderers.class)
@MixinEnvironment(value = "client", type = MixinEnvironment.Env.CLIENT)
public class EntityRenderersMixin {
//? if <= 26.3 {
    /*@Inject(method = "<clinit>", at = @At("TAIL"))
    private static void registerRenderers(CallbackInfo ci) {
        ClientRegistryEvents.ENTITY_RENDERER.invoker().getEntityRenderers().forEach(renderableEntity -> {
            EntityRenderers.register(renderableEntity.type(), renderableEntity.renderer());
        });
    }
*///?} else {
    @Inject(method = "bootstrap", at = @At("TAIL"))
    private static void registerRenderers(WritableRegistry<EntityRendererProvider<?>> registry, CallbackInfo ci) {
        ClientRegistryEvents.ENTITY_RENDERER.invoker().getEntityRenderers().forEach(renderableEntity -> {
            EntityRenderers.register(registry, renderableEntity.type(), renderableEntity.renderer());
        });
    }
//?}
}