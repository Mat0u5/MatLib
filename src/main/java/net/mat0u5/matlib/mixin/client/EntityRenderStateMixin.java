package net.mat0u5.matlib.mixin.client;
//? if <= 1.21 {
/*import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;

@Mixin(value = MinecraftServer.class)
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
public class EntityRenderStateMixin {
    //Empty class to avoid mixin errors
}
*///?} else {
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.client.utils.interfaces.IEntityRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = EntityRenderState.class, priority = 1)
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
public class EntityRenderStateMixin implements IEntityRenderState {
    @Unique
    Entity ml$entity = null;
    @Unique
    float ml$tickProgress = 0;

    @Unique
    @Nullable
    @Override
    public Entity ml$getEntity() {
        return ml$entity;
    }

    @Unique
    @Override
    public float ml$getTickProgress() {
        return ml$tickProgress;
    }

    @Unique
    @Override
    public void ml$update(Entity entity, float tickProgress) {
        ml$entity = entity;
    }
}
//?}