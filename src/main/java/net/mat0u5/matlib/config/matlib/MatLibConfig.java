package net.mat0u5.matlib.config.matlib;

import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.config.GuiConfig;
import net.mat0u5.matlib.config.ConfigManager;
import net.mat0u5.matlib.config.entry.ConfigurableValue;
import net.mat0u5.matlib.utils.player.PermissionManager;
import net.minecraft.server.level.ServerPlayer;

public class MatLibConfig extends GuiConfig {

	public ConfigurableValue<Integer> TEST = ConfigurableValue.register(this, "test", 1)
			.name("Test value")
			.description("Test value description")
			.serverShared();


	private MatLibConfig() {
		super("matlib:main", "MatLib Config", "matlib", MatLib.MOD_ID+".properties");
	}

	private static MatLibConfig instance;
	public static MatLibConfig get() {
		if (instance == null) instance = new MatLibConfig();
		return instance;
	}

	public static void register() {
		if (MatLib.DEBUG) ConfigManager.registerConfig(get());
	}

	@Override
	public boolean hasPermissionsToOpen(ServerPlayer player) {
		return PermissionManager.isAdmin(player);
	}
}
