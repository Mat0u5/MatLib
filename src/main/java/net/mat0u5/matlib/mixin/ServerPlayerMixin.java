package net.mat0u5.matlib.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.events.EventResult;
import net.mat0u5.matlib.events.server.ServerEntityEvents;
import net.mat0u5.matlib.events.server.ServerPlayerEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;

//? if <= 1.21.6 {
/*import net.mat0u5.matlib.entity.fakeplayer.FakeServerPlayer;
import net.minecraft.network.chat.Component;
*///?}

@Mixin(value = ServerPlayer.class, priority = 1)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public class ServerPlayerMixin {
	@Inject(method = "die", at = @At("HEAD"), cancellable = true)
	private void preDeath(DamageSource source, CallbackInfo ci) {
		EventResult result1 = ServerPlayerEvents.PRE_DEATH.invoker().onPreDeath((ServerPlayer) (Object) this, source);
		if (result1.isDeny()) {
			ci.cancel();
			return;
		}
		EventResult result2 = ServerEntityEvents.PRE_DEATH.invoker().onPreDeath((ServerPlayer) (Object) this, source);
		if (result2.isDeny()) {
			ci.cancel();
		}
	}

	@Inject(method = "die", at = @At("TAIL"))
	private void notifyDeath(DamageSource source, CallbackInfo ci) {
		ServerPlayerEvents.DEATH.invoker().onDeath((ServerPlayer) (Object) this, source);
		ServerEntityEvents.DEATH.invoker().onDeath((ServerPlayer) (Object) this, source);
	}

	@Inject(method = "openMenu", at = @At("TAIL"))
	private void onOpenMenu(@Nullable MenuProvider factory, CallbackInfoReturnable<OptionalInt> cir) {
		ServerPlayer player = (ServerPlayer) (Object) this;
		ServerPlayerEvents.OPEN_MENU.invoker().onOpenMenu(player, player.containerMenu);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void onTick(CallbackInfo ci) {
		ServerPlayerEvents.TICK.invoker().onTickStart((ServerPlayer) (Object) this);
	}

	//? if <= 1.21.6 {
	/*@Inject(method = "sendSystemMessage(Lnet/minecraft/network/chat/Component;Z)V", at = @At("HEAD"), cancellable = true)
	private void ignoreFakePlayerMessages(Component message, boolean overlay, CallbackInfo ci) {
		if ((Object) this instanceof FakeServerPlayer) {
			ci.cancel();
		}
	}

	@Inject(method = "acceptsSystemMessages", at = @At("HEAD"), cancellable = true)
	private void fakePlayerAcceptsSystemMessages(boolean overlay, CallbackInfoReturnable<Boolean> cir) {
		if ((Object) this instanceof FakeServerPlayer) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "acceptsChatMessages", at = @At("HEAD"), cancellable = true)
	private void fakePlayerAcceptsChatMessages(CallbackInfoReturnable<Boolean> cir) {
		if ((Object) this instanceof FakeServerPlayer) {
			cir.setReturnValue(false);
		}
	}
	*///?}
}
