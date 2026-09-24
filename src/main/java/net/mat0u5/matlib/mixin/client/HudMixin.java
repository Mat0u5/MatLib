package net.mat0u5.matlib.mixin.client;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.mat0u5.matlib.client.events.ClientRenderEvents;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >= 1.21
import net.minecraft.client.DeltaTracker;

//? if <= 26.1 {
/*import net.minecraft.client.gui.Gui;
@Mixin(value = Gui.class)
*///?} else {
import net.minecraft.client.gui.Hud;
@Mixin(value = Hud.class, priority = 1)
//?}
@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
public class HudMixin {
	//? if <= 1.20.5 {
    /*@Inject(method = "render", at = @At(value = "HEAD"))
    public void render(GuiGraphicsExtractor guiGraphics, float deltaTracker, CallbackInfo ci) {
    *///?} else if <= 1.21.11 {
    /*@Inject(method = "render", at = @At(value = "HEAD"))
    public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
    *///?} else {
	@Inject(method = "extractRenderState", at = @At(value = "HEAD"))
	public void render(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
	//?}
		//? if !(neoforge && <= 1.20.3) && !(forge && <= 1.20) {
		ClientRenderEvents.RENDER_GUI.invoker().onRenderGui(guiGraphics, deltaTracker);
		//?}
	}

//? if forge {
    /*//? if <= 1.20.5 {
    @Inject(method = "render", at = @At(value = "TAIL"))
    public void renderPost(GuiGraphicsExtractor guiGraphics, float deltaTracker, CallbackInfo ci) {
    //?} else if <= 1.21.6 {
    /^@Inject(method = "render", at = @At(value = "TAIL"))
    public void renderPost(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
    ^///?} else if <= 1.21.11 {
    /^@Inject(method = "render", at = @At(value = "RETURN"))
    public void renderPost(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
    ^///?} else {
    /^@Inject(method = "extractRenderState", at = @At(value = "RETURN"))
    public void renderPost(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
    ^///?}
*///?} else {
	//? if <= 1.20.5 {
    /*@Inject(method = "render", at = @At(value = "TAIL"))
    public void renderPost(GuiGraphicsExtractor guiGraphics, float deltaTracker, CallbackInfo ci) {
    *///?} else if <= 1.21.11 {
    /*@Inject(method = "render", at = @At(value = "TAIL"))
    public void renderPost(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
    *///?} else {
	@Inject(method = "extractRenderState", at = @At(value = "TAIL"))
	public void renderPost(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
	//?}
//?}
		//? if !(neoforge && <= 1.20.3) && !(forge && <= 1.20) {
		ClientRenderEvents.RENDER_GUI_POST.invoker().onPostRenderGui(guiGraphics, deltaTracker);
		//?}
	}
}
