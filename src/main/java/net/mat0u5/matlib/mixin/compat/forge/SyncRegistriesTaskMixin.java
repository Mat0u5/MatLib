package net.mat0u5.matlib.mixin.compat.forge;

//? if forge && > 1.20 {
/*import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.utils.other.OtherUtils;
import net.mat0u5.matlib.utils.other.RegistrySyncUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import net.minecraftforge.network.config.ConfigurationTaskContext;
import net.minecraftforge.network.tasks.SyncRegistriesTask;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Mixin(value = SyncRegistriesTask.class, priority = 1, remap = false)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public class SyncRegistriesTaskMixin {
    @WrapOperation(method = "start(Lnet/minecraftforge/network/config/ConfigurationTaskContext;)V", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/registries/RegistryManager;takeSnapshot(Z)Ljava/util/Map;"))
    private Map<Identifier, ForgeRegistry.Snapshot> ml$editRegistries(RegistryManager instance, boolean savingToDisc, Operation<Map<Identifier, ForgeRegistry.Snapshot>> original, ConfigurationTaskContext ctx) {
        Map<Identifier, ForgeRegistry.Snapshot> snapshots = original.call(instance, savingToDisc);
        if (snapshots == null || !(ctx.getConnection().getPacketListener() instanceof ServerConfigurationPacketListenerImpl listener)) return snapshots;

        Map<Identifier, Collection<Identifier>> registries = new LinkedHashMap<>();
        snapshots.forEach((registry, snapshot) -> registries.put(registry, snapshot.ids.keySet()));
        RegistrySyncUtils.editRegistries(registries, OtherUtils.profileId(listener.getOwner()));
        return snapshots;
    }
}
*///?} else {
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = MinecraftServer.class)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public interface SyncRegistriesTaskMixin {
}
//?}
