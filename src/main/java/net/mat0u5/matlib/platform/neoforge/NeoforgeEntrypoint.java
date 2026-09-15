package net.mat0u5.matlib.platform.neoforge;

//? if neoforge {

/*import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.registries.MobRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@Mod(MatLib.MOD_ID)
public class NeoforgeEntrypoint {

	public NeoforgeEntrypoint(IEventBus modBus) {
		MatLib.onInitialize();
		modBus.addListener(this::registerAttributes);
	}

	private void registerAttributes(EntityAttributeCreationEvent event) {
		MobRegistry.registerAttributes(event);
	}
}
*///?}
