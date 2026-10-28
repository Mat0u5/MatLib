package net.mat0u5.matlib.mixin.compat.neoforge;

//? if neoforge {
/*import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.utils.other.RegistrySyncUtils;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.configuration.SyncRegistries;
import net.neoforged.neoforge.network.payload.FrozenRegistryPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mixin(value = SyncRegistries.class, priority = 1, remap = false)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public class SyncRegistriesMixin {
    @WrapOperation(method = "run", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/registries/RegistryManager;generateRegistryPackets(Z)Ljava/util/List;"))
    private List<FrozenRegistryPayload> ml$editRegistries(boolean isLocal, Operation<List<FrozenRegistryPayload>> original) {
        List<FrozenRegistryPayload> packets = original.call(isLocal);
        UUID playerUUID = RegistrySyncUtils.getConfiguringPlayer();
        if (playerUUID == null) return packets;

        Map<Identifier, Collection<Identifier>> registries = new LinkedHashMap<>();
        for (FrozenRegistryPayload packet : packets) {
            registries.put(packet.registryName(), ((RegistrySnapshotAccessor) packet.snapshot()).getMutableIds().values());
        }
        RegistrySyncUtils.editRegistries(registries, playerUUID);
        return packets;
    }
}
*///?} else {
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = MinecraftServer.class)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public interface SyncRegistriesMixin {
}
//?}
