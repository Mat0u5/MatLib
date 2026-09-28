package net.mat0u5.matlib.mixin.client;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.client.events.ClientPlayerEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Player.class, priority = 2)
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
public class PlayerMixin {

	@Inject(method = "die", at = @At("HEAD"))
	private void onDeath(DamageSource source, CallbackInfo ci) {
		Player player = (Player) (Object) this;
		ClientPlayerEvents.DEATH.invoker().onDeath(player, source);
	}
}
