package net.mat0u5.matlib.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.client.events.ClientPlayerEvents;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//? if <= 1.20 {
/*import net.minecraft.resources.Identifier;
*///?} else {
import net.minecraft.world.entity.player.PlayerSkin;
//?}

@Mixin(value = PlayerInfo.class, priority = 1)
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
public class PlayerInfoMixin {

    //? if <= 1.20 {
    /*@ModifyReturnValue(method = "getSkinLocation", at = @At("RETURN"))
    private Identifier modifySkin(Identifier original) {
    *///?} else {
    @ModifyReturnValue(method = "getSkin", at = @At("RETURN"))
    private PlayerSkin modifySkin(PlayerSkin original) {
    //?}
        PlayerInfo playerInfo = (PlayerInfo) (Object) this;
        return ClientPlayerEvents.GET_SKIN.invoker().getSkin(playerInfo, original).getOrDefault(original);
    }
}
