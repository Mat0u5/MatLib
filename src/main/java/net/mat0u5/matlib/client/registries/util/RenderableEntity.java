package net.mat0u5.matlib.client.registries.util;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public record RenderableEntity<T extends Entity> (EntityType<? extends T> type, EntityRendererProvider<T> renderer) {}
