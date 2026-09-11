package net.mat0u5.matlib.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.events.common.CommonPlayerEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Player.class, priority = 1)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public class PlayerMixin {

	@Inject(method = "attack", at = @At("HEAD"), cancellable = true)
	public void onPlayerInteractEntity(Entity target, CallbackInfo info) {
		InteractionResult result = CommonPlayerEvents.ATTACK_ENTITY.invoker().onAttackEntity((Player) (Object) this, target);

		if (result != InteractionResult.PASS) {
			info.cancel();
		}
	}
}
