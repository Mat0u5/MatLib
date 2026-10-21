package net.mat0u5.matlib.client.utils.interfaces;

import net.minecraft.world.entity.Entity;

/**
 * Stores the rendered entity and tick progress in {@code EntityRenderState}.
 */
public interface IEntityRenderState {
    Entity ml$getEntity();
    float ml$getTickProgress();
    void ml$update(Entity entity, float tickProgress);
}
