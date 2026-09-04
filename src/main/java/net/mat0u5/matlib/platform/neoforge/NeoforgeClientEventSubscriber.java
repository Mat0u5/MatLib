package net.mat0u5.matlib.platform.neoforge;

//? neoforge {

/*import net.mat0u5.matlib.Main;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
//? if <= 1.20.3 {
/^import net.neoforged.fml.common.Mod;
^///?} else {
import net.neoforged.fml.common.EventBusSubscriber;
//?}

//? if <= 1.20.3 {
/^@Mod.EventBusSubscriber(modid = Main.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
 ^///?} else if <= 1.21.2 {
/^@EventBusSubscriber(modid = Main.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
^///?} else {
@EventBusSubscriber(modid = Main.MOD_ID, value = Dist.CLIENT)
//?}
public class NeoforgeClientEventSubscriber {
	@SubscribeEvent
	public static void onClientSetup(final FMLClientSetupEvent event) {
		Main.onInitializeClient();
	}
}
*///?}
