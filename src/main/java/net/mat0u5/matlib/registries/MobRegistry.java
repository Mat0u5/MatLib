package net.mat0u5.matlib.registries;

import net.mat0u5.matlib.events.common.CommonRegistryEvents;
import net.mat0u5.matlib.util.world.AttributeEntity;
//? fabric || (forge && > 1.21) {
import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.mixin.DefaultAttributesAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
//?} else if forge {
/*import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
*///?} else if neoforge {
/*import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
*///?}

public class MobRegistry {

	//? fabric || (forge && > 1.21) {
	public static void registerAttributes() {
		for (AttributeEntity entity : CommonRegistryEvents.MOB_ATTRIBUTE.invoker().getAttributeEntities()) {
			register(entity.type(), entity.container());
		}
	}

	public static void register(EntityType<? extends LivingEntity> type, AttributeSupplier container) {
		if (DefaultAttributesAccessor.getRegistry().put(type, container) != null) {
			MatLib.LOGGER.debug("Overriding existing registration for entity type {}", BuiltInRegistries.ENTITY_TYPE.getKey(type));
		}
	}
	//?} else {
	/*public static void registerAttributes(EntityAttributeCreationEvent event) {
		for (AttributeEntity entity : CommonRegistryEvents.MOB_ATTRIBUTE.invoker().getAttributeEntities()) {
			event.put(entity.type(), entity.container());
		}
	}
	*///?}
}
