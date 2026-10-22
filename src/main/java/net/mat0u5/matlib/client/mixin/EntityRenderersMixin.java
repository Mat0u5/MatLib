package net.mat0u5.matlib.client.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.client.events.ClientRegistryEvents;
import net.minecraft.client.renderer.entity.EntityRenderers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

//? if <= 26.3 {
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//?} else {
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Registry;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
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
    private static void registerRenderers(Registry<EntityRendererProvider<?>> registry, CallbackInfoReturnable<EntityRendererProvider<?>> cir) {
        ClientRegistryEvents.ENTITY_RENDERER.invoker().getEntityRenderers().forEach(renderableEntity -> {
            EntityRenderers.register(registry, renderableEntity.type(), renderableEntity.renderer());
        });
    }
//?}
}