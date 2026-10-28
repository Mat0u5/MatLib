package net.mat0u5.matlib.mixin.compat.neoforge;

//? if neoforge {
/*import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.utils.other.OtherUtils;
import net.mat0u5.matlib.utils.other.RegistrySyncUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ConfigurationTask;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import net.neoforged.neoforge.network.configuration.SyncRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Consumer;

@Mixin(value = ServerConfigurationPacketListenerImpl.class, priority = 1)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public class ServerConfigurationPacketListenerImplMixin {
    @WrapOperation(method = "startNextTask", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ConfigurationTask;start(Ljava/util/function/Consumer;)V"))
    private void ml$trackSyncingPlayer(ConfigurationTask task, Consumer<Packet<?>> sender, Operation<Void> original) {
        ServerConfigurationPacketListenerImpl self = (ServerConfigurationPacketListenerImpl) (Object) this;
        if (!(task instanceof SyncRegistries) || self.getConnection().isMemoryConnection()) {
            original.call(task, sender);
            return;
        }
        // This stores the owner uuid, runs the task, then clears the uuid.
        RegistrySyncUtils.runConfigurationTask(OtherUtils.profileId(self.getOwner()), () -> original.call(task, sender));
    }
}
*///?} else {
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = MinecraftServer.class)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public interface ServerConfigurationPacketListenerImplMixin {
}
//?}
