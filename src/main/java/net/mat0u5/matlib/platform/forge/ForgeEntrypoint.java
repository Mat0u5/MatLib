package net.mat0u5.matlib.platform.forge;

//? if forge {
/*import net.mat0u5.matlib.MatLib;
import net.minecraftforge.fml.common.Mod;
//? if <= 1.21 {
/^import net.mat0u5.matlib.registries.MobRegistry;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
^///?}

@Mod(MatLib.MOD_ID)
public class ForgeEntrypoint {
	//? if <= 1.21 {
	/^public ForgeEntrypoint() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		MatLib.onInitialize();
		modBus.addListener(this::registerAttributes);
	}

	private void registerAttributes(EntityAttributeCreationEvent event) {
		MobRegistry.registerAttributes(event);
	}
	^///?} else {
	public ForgeEntrypoint() {
		MatLib.onInitialize();
	}
	//?}
}
*///?}
