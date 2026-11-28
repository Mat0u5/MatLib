package net.mat0u5.matlib.client.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.client.events.ClientRegistryEvents;
import net.minecraft.client.renderer.entity.EntityRenderers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderers.class)
@MixinEnvironment(value = "client", type = MixinEnvironment.Env.CLIENT)
public class EntityRenderersMixin {
    @Unique
    private static boolean ml$registered = false;

    @Inject(method = {"createEntityRenderers", "validateRegistrations"}, at = @At("HEAD"))
    private static void registerRenderers(CallbackInfoReturnable<?> cir) {
        if (ml$registered) return;
        ml$registered = true;
        ClientRegistryEvents.ENTITY_RENDERER.invoker().getEntityRenderers().forEach(renderableEntity -> {
            EntityRenderers.register(renderableEntity.type(), renderableEntity.renderer());
        });
    }
}