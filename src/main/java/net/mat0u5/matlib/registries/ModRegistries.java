package net.mat0u5.matlib.registries;

import net.mat0u5.matlib.events.server.ServerTickEvents;
import net.mat0u5.matlib.util.other.DefaultTaskScheduler;
import net.mat0u5.matlib.util.player.PlayerUtils;

public class ModRegistries {
	public static void initialize() {
		ServerTickEvents.END_TICK.register(server -> PlayerUtils.onTick());
		ServerTickEvents.END_TICK.register(server -> {
			//? if < 1.20.3 {
			/*boolean gameFrozen = false;
			 *///?} else {
			boolean gameFrozen = server.tickRateManager().isFrozen();
			//?}
			DefaultTaskScheduler.onTick(gameFrozen);
		});
	}
}
