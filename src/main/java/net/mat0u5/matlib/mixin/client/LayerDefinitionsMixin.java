package net.mat0u5.matlib.mixin.client;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.client.events.ClientRegistryEvents;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(LayerDefinitions.class)
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
public class LayerDefinitionsMixin {
    @Inject(method = "createRoots", at = @At("RETURN"), cancellable = true)
    private static void injectLayerDefinitions(CallbackInfoReturnable<Map<ModelLayerLocation, LayerDefinition>> cir) {
        Map<ModelLayerLocation, LayerDefinition> map = new HashMap<>(cir.getReturnValue());

        ClientRegistryEvents.ENTITY_MODEL_LAYER_DEFINITION.invoker().getLayerDefinitionModels().forEach(layerDefinitionModel -> {
            map.put(layerDefinitionModel.modelLayerLocation(), layerDefinitionModel.layerDefinition());
        });

        cir.setReturnValue(map);
    }
}