package net.mat0u5.matlib.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

//? if > 1.20 && <= 1.20.3 {
/*import com.mojang.authlib.GameProfile;
import org.spongepowered.asm.mixin.gen.Invoker;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
*///?}

@Mixin(value = SkullBlockEntity.class, priority = 1)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public interface SkullBlockEntityAccessor {
	//? if > 1.20 && <= 1.20.3 {
    /*@Invoker("fetchGameProfile")
    static CompletableFuture<Optional<GameProfile>> ml$fetchGameProfile(String string) {
        throw new AssertionError();
    }
    *///?}
}
