package net.mat0u5.matlib.mixin.client;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.client.events.ClientRegistryEvents;
import net.minecraft.client.renderer.entity.EntityRenderers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderers.class)
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
public class EntityRenderersMixin {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void registerRenderers(CallbackInfo ci) {
        ClientRegistryEvents.ENTITY_RENDERER.invoker().getEntityRenderers().forEach(renderableEntity -> {
            EntityRenderers.register(renderableEntity.type(), renderableEntity.renderer());
        });
    }
}