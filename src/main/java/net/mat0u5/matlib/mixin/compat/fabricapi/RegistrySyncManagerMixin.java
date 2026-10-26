package net.mat0u5.matlib.mixin.compat.fabricapi;

//? if fabric {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.fabric.impl.registry.sync.RegistrySyncManager;
import net.mat0u5.matlib.utils.other.RegistrySyncUtils;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

//? if <= 1.20 {
//import net.minecraft.server.level.ServerPlayer;
//?} else {
import net.mat0u5.matlib.utils.other.OtherUtils;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
//?}
//? if > 1.21.2 {
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//?}

@Mixin(value = RegistrySyncManager.class, priority = 1, remap = false)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public class RegistrySyncManagerMixin {
    @Unique
    private static final ThreadLocal<Boolean> ml$hasTrackedEntries = ThreadLocal.withInitial(() -> false);

    //? if <= 1.20 {
    /*@WrapOperation(method = "sendPacket(Lnet/minecraft/server/level/ServerPlayer;Lnet/fabricmc/fabric/impl/registry/sync/packet/RegistryPacketHandler;)V", at = @At(value = "INVOKE", target = "Lnet/fabricmc/fabric/impl/registry/sync/RegistrySyncManager;createAndPopulateRegistryMap(ZLjava/util/Map;)Ljava/util/Map;"))
    private static @Nullable Map<Identifier, Object2IntMap<Identifier>> ml$editRegistries(boolean b, Map map, Operation<Map<Identifier, Object2IntMap<Identifier>>> original, ServerPlayer player) {
        Map<Identifier, Object2IntMap<Identifier>> registryMap = original.call(b, map);
        ml$hasTrackedEntries.set(RegistrySyncUtils.editRegistryMap(registryMap, player.getUUID()));
        return registryMap;
    }
    *///?} else {
    @WrapOperation(method = "configureClient", at = @At(value = "INVOKE", target = "Lnet/fabricmc/fabric/impl/registry/sync/RegistrySyncManager;createAndPopulateRegistryMap()Ljava/util/Map;"))
    private static @Nullable Map<Identifier, Object2IntMap<Identifier>> ml$editRegistries(Operation<Map<Identifier, Object2IntMap<Identifier>>> original, ServerConfigurationPacketListenerImpl handler) {
        Map<Identifier, Object2IntMap<Identifier>> registryMap = original.call();
        ml$hasTrackedEntries.set(RegistrySyncUtils.editRegistryMap(registryMap, OtherUtils.profileId(handler.getOwner())));
        return registryMap;
    }
    //?}

    //? if > 1.21.2 {
    @Inject(method = "areAllRegistriesOptional", at = @At(value = "HEAD"), cancellable = true)
    private static void ml$allowVanillaClients(Map<Identifier, Object2IntMap<Identifier>> map, CallbackInfoReturnable<Boolean> cir) {
        if (ml$hasTrackedEntries.get()) {
            ml$hasTrackedEntries.set(false);
            cir.setReturnValue(true);
        }
    }
    //?}
}
//?} else {
/*import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = MinecraftServer.class)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public interface RegistrySyncManagerMixin {
}
*///?}
