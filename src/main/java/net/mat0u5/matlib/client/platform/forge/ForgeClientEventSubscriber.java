package net.mat0u5.matlib.client.platform.forge;

//? if forge {

/*//? if <= 1.20 {
/^import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.client.events.ClientRenderEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
@Mod.EventBusSubscriber(modid = MatLib.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
^///?}
public class ForgeClientEventSubscriber {
    //? if <= 1.20 {
    /^@SubscribeEvent
	public static void onRenderGui(RenderGuiEvent.Pre event) {
		ClientRenderEvents.RENDER_GUI.invoker().onRenderGui(event.getGuiGraphicsExtractor(), event.getPartialTick());
	}
	@SubscribeEvent
	public static void onRenderGui(RenderGuiEvent.Post event) {
		ClientRenderEvents.RENDER_GUI_POST.invoker().onPostRenderGui(event.getGuiGraphicsExtractor(), event.getPartialTick());
	}
    ^///?}
}
*///?}