package net.mat0u5.matlib.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.events.common.ServerLifecycleEvents;
import net.mat0u5.matlib.events.common.ServerResourceEvents;
import net.mat0u5.matlib.events.common.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.BooleanSupplier;

@Mixin(value = MinecraftServer.class, priority = 1)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public abstract class MinecraftServerMixin {
    @Shadow
    private MinecraftServer.ReloadableResources resources;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;initServer()Z"), method = "runServer")
    private void beforeSetupServer(CallbackInfo info) {
        ServerLifecycleEvents.SERVER_STARTING.invoker().onStarting((MinecraftServer) (Object) this);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;buildServerStatus()Lnet/minecraft/network/protocol/status/ServerStatus;", ordinal = 0), method = "runServer")
    private void afterSetupServer(CallbackInfo info) {
        ServerLifecycleEvents.SERVER_STARTED.invoker().onStarted((MinecraftServer) (Object) this);
    }

    @Inject(at = @At("HEAD"), method = "stopServer")
    private void beforeShutdownServer(CallbackInfo info) {
        ServerLifecycleEvents.SERVER_STOPPING.invoker().onStopping((MinecraftServer) (Object) this);
    }

    @Inject(at = @At("TAIL"), method = "stopServer")
    private void afterShutdownServer(CallbackInfo info) {
        ServerLifecycleEvents.SERVER_STOPPED.invoker().onStopped((MinecraftServer) (Object) this);
    }

    @Inject(method = "reloadResources", at = @At("HEAD"))
    private void startResourceReload(Collection<String> collection, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        ServerResourceEvents.RELOAD_START.invoker().onStart((MinecraftServer) (Object) this, this.resources.resourceManager());
    }

    @Inject(method = "reloadResources", at = @At("TAIL"))
    private void endResourceReload(Collection<String> collection, CallbackInfoReturnable<CompletableFuture<Void>> cir) {

        cir.getReturnValue().handleAsync((value, throwable) -> {
            // Hook into fail
            ServerResourceEvents.RELOAD_STOPPING.invoker().onEnd((MinecraftServer) (Object) this, this.resources.resourceManager(), throwable == null);
            return value;
        }, (MinecraftServer) (Object) this);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;tickChildren(Ljava/util/function/BooleanSupplier;)V"), method = "tickServer")
    private void onStartTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        ServerTickEvents.START_TICK.invoker().onTickStart((MinecraftServer) (Object) this);
    }

    @Inject(at = @At("TAIL"), method = "tickServer")
    private void onEndTick(BooleanSupplier shouldKeepTicking, CallbackInfo info) {
        ServerTickEvents.END_TICK.invoker().onTickEnd((MinecraftServer) (Object) this);
    }
}
