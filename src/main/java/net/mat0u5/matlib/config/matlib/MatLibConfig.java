package net.mat0u5.matlib.config.matlib;

import com.google.auto.service.AutoService;
import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.config.GuiConfig;
import net.mat0u5.matlib.config.ConfigManager;
import net.mat0u5.matlib.config.entry.ConfigurableValue;
import net.mat0u5.matlib.services.RegistrableServer;
import net.mat0u5.matlib.utils.other.DebugOptions;
import net.mat0u5.matlib.utils.player.PermissionManager;
import net.minecraft.server.level.ServerPlayer;

public class MatLibConfig extends GuiConfig {

	public ConfigurableValue<DebugOptions.Prebuilt> LOGGING_LEVEL = ConfigurableValue.register(this, "logging_level", DebugOptions.Prebuilt.STABLE)
			.name("Logging Level")
			.description("Controls how many events will be logged in the console.")
			.onChange((oldValue, newValue) -> MatLib.DEBUG = DebugOptions.of(newValue));


	private MatLibConfig() {
		super("matlib:main", "MatLib Config", "matlib", MatLib.MOD_ID+".properties");
	}

	private static MatLibConfig instance;
	public static MatLibConfig get() {
		if (instance == null) instance = new MatLibConfig();
		return instance;
	}

	@AutoService(RegistrableServer.class)
	public static class Registrar implements RegistrableServer {
		@Override
		public void onRegister() {
			ConfigManager.registerConfig(get());
		}
	}

	/*@Override
	public String getClientCounterpartId() {
		return null;
	}*/

	@Override
	public boolean hasPermissionsToOpen(ServerPlayer player) {
		return PermissionManager.isAdmin(player);
	}
}
