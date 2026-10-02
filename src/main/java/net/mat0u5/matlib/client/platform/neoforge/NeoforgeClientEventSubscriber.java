package net.mat0u5.matlib.client.platform.neoforge;

//? if neoforge {

/*//? if <= 1.20.3 {
/^import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.client.events.ClientRenderEvents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderGuiOverlayEvent;
import net.neoforged.neoforge.client.gui.overlay.VanillaGuiOverlay;
^///?}

//? if <= 1.20.3 {
/^@net.neoforged.fml.common.Mod.EventBusSubscriber(modid = MatLib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
^///?}
public class NeoforgeClientEventSubscriber {
    //? if <= 1.20.3 {
    /^@SubscribeEvent
	public static void onRenderGui(RenderGuiOverlayEvent.Pre event) {
		if (event.getOverlay() == VanillaGuiOverlay.HOTBAR.type()) {
			ClientRenderEvents.RENDER_GUI.invoker().onRenderGui(event.getGuiGraphicsExtractor(), event.getPartialTick());
		}
	}

	@SubscribeEvent
	public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
		if (event.getOverlay() == VanillaGuiOverlay.HOTBAR.type()) {
			ClientRenderEvents.RENDER_GUI_POST.invoker().onPostRenderGui(event.getGuiGraphicsExtractor(), event.getPartialTick());
		}
	}
    ^///?}
}

*///?}