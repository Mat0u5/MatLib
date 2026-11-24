package net.mat0u5.matlib.client.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;

//? if <= 1.20.3 {
/*import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import net.mat0u5.matlib.client.events.ClientRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
*///?}

@Mixin(value = GameRenderer.class)
@MixinEnvironment(value = "client", type = MixinEnvironment.Env.CLIENT)
public class GameRendererMixin {
	//? if <= 1.20.3 {
	/*@Inject(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Options;hideGui:Z", opcode = Opcodes.GETFIELD))
	private void renderHiddenGui(float partialTick, long nanoTime, boolean renderLevel, CallbackInfo ci, @Local GuiGraphicsExtractor guiGraphics) {
		Minecraft client = Minecraft.getInstance();
		if (!client.options.hideGui || client.screen != null) return;
		ClientRenderEvents.RENDER_GUI.invoker().onRenderGui(guiGraphics, partialTick);
		ClientRenderEvents.RENDER_GUI_POST.invoker().onPostRenderGui(guiGraphics, partialTick);
		guiGraphics.flush();
		RenderSystem.clear(256, Minecraft.ON_OSX);
	}
	*///?}
}
