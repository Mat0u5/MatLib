package net.mat0u5.matlib.client.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.client.events.ClientLifecycleEvents;
import net.minecraft.client.Minecraft;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >= 1.20.3 {
import net.mat0u5.matlib.client.events.ClientTickRateEvents;
import net.mat0u5.matlib.events.OptionalEventReturn;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//?}

@Mixin(value = Minecraft.class, priority = 1)
@MixinEnvironment(value = "client", type = MixinEnvironment.Env.CLIENT)
public class MinecraftMixin {

	//? if <= 26.1 {
	/*@Inject(at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;)V", shift = At.Shift.AFTER), method = "destroy")
	 *///?} else {
	@Inject(at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;)V", shift = At.Shift.AFTER), method = "exitWorldAndClose")
	//?}
	private void onStopping(CallbackInfo ci) {
		ClientLifecycleEvents.CLIENT_STOPPING.invoker().onStopping((Minecraft) (Object) this);
	}

	@Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;gameThread:Ljava/lang/Thread;", shift = At.Shift.AFTER, ordinal = 0, opcode = Opcodes.PUTFIELD), method = "run")
	private void onStart(CallbackInfo ci) {
		ClientLifecycleEvents.CLIENT_STARTED.invoker().onStarted((Minecraft) (Object) this);
	}

	//? if >= 1.20.3 {
	@Inject(method = "getTickTargetMillis", at = @At("HEAD"), cancellable = true)
	private void modifyTickTargetMillis(float defaultMillis, CallbackInfoReturnable<Float> cir) {
		OptionalEventReturn<Float> result = ClientTickRateEvents.MODIFY_TARGET_MSPT.invoker().onModifyTargetMspt(defaultMillis);
		if (!result.isPass() && result.get() != null) {
			cir.setReturnValue(result.get());
		}
	}
	//?}
}
