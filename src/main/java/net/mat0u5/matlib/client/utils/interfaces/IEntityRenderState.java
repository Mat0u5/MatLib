package net.mat0u5.matlib.client.utils.interfaces;

import net.minecraft.world.entity.Entity;

/**
 * Stores information in {@link net.minecraft.world.entity.Display.RenderState}
 */
//TODO perhaps change this.
public interface IEntityRenderState {
    Entity ml$getEntity();
    float ml$getTickProgress();
    void ml$update(Entity entity, float tickProgress);
}
