package net.mat0u5.matlib.client.platform.forge;

//? forge {
/*import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.client.MatLibClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
//? if <= 1.21.5 {
/^import net.minecraftforge.eventbus.api.SubscribeEvent;
^///?} else {
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
 //?}

@Mod.EventBusSubscriber(modid = MatLib.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeClientEventSubscriber {

	@SubscribeEvent
	public static void onClientSetup(final FMLClientSetupEvent event) {
		MatLibClient.onInitializeClient();
	}
}
*///?}
