package net.mat0u5.matlib.registries.util;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public record AttributeEntity(EntityType<? extends LivingEntity> type, AttributeSupplier container) { }
