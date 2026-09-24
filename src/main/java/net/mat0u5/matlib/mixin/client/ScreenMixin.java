package net.mat0u5.matlib.mixin.client;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.client.events.ClientScreenEvents;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if <= 1.21.9 {
/*import net.minecraft.client.Minecraft;
*///?}
@Mixin(Screen.class)
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
public class ScreenMixin {
    //? if <= 1.21.9 {
    /*@Inject(method = "init(Lnet/minecraft/client/Minecraft;II)V", at = @At("TAIL"))
    private void afterInitScreen(Minecraft minecraft, int width, int height, CallbackInfo ci) {
        ClientScreenEvents.OPEN_SCREEN.invoker().onOpen((Screen) (Object) this, width, height);
    }
    @Inject(method = "resize", at = @At("TAIL"))
    private void afterResizeScreen(Minecraft minecraft, int width, int height, CallbackInfo ci) {
        ClientScreenEvents.RESIZE_SCREEN.invoker().onResize((Screen) (Object) this, width, height);
    }
    *///?} else {
    @Inject(method = "init(II)V", at = @At("TAIL"))
    private void afterInitScreen(int width, int height, CallbackInfo ci) {
        ClientScreenEvents.OPEN_SCREEN.invoker().onOpen((Screen) (Object) this, width, height);
    }
    @Inject(method = "resize", at = @At("TAIL"))
    private void afterResizeScreen(int width, int height, CallbackInfo ci) {
        ClientScreenEvents.RESIZE_SCREEN.invoker().onResize((Screen) (Object) this, width, height);
    }
    //?}

    @Inject(method = "onClose", at = @At("TAIL"))
    private void afterResizeScreen(CallbackInfo ci) {
        ClientScreenEvents.CLOSE_SCREEN.invoker().onClose((Screen) (Object) this);
    }
}
