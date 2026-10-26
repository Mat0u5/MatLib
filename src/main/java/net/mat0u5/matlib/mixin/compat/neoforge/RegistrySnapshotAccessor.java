package net.mat0u5.matlib.mixin.compat.neoforge;

//? if neoforge {
/*import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.registries.RegistrySnapshot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = RegistrySnapshot.class, remap = false)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public interface RegistrySnapshotAccessor {
    @Accessor("ids")
    Int2ObjectSortedMap<Identifier> getMutableIds();
}
*///?} else {
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = MinecraftServer.class)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public interface RegistrySnapshotAccessor {
}
//?}
