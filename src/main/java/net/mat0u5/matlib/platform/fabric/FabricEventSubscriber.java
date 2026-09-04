package net.mat0u5.matlib.platform.fabric;

//? fabric {

import net.mat0u5.matlib.event.ExampleEventHandler;
import net.minecraft.server.level.ServerPlayer;
//? if <= 1.16 {
/*import net.mat0u5.matlib.Main;
import net.fabricmc.fabric.api.event.world.WorldTickCallback;
*///?} else {
import net.fabricmc.fabric.api.entity.event.v1.*;
//?}

public class FabricEventSubscriber {

	public static void registerEvents() {
		//? if <= 1.16 {
		/*WorldTickCallback.EVENT.register((level) -> {
			Main.LOGGER.info("TICK");
		});
		*///?} else if <= 1.19.4 {
		/*ServerPlayerEvents.ALLOW_DEATH.register((entity, source, damageTaken) -> {
			if (entity instanceof ServerPlayer && damageTaken > 0) {
				ExampleEventHandler.onPlayerHurt((ServerPlayer) entity);
			}
			return true;
		});
		*///?} else if <= 1.21 {
		/*ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, damageTaken) -> {
			if (entity instanceof ServerPlayer && damageTaken > 0) {
				ExampleEventHandler.onPlayerHurt((ServerPlayer) entity);
			}
			return true;
		});
		*///?} else {
		ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamage, damageTaken, blocked) -> {
			if (entity instanceof ServerPlayer && damageTaken > 0) {
				ExampleEventHandler.onPlayerHurt((ServerPlayer) entity);
			}
		});
		//?}
	}
}
//?}
