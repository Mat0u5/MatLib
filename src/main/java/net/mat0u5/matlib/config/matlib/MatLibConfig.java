package net.mat0u5.matlib.config.matlib;

import com.google.auto.service.AutoService;
import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.config.GuiConfig;
import net.mat0u5.matlib.config.ConfigManager;
import net.mat0u5.matlib.config.entry.ConfigurableValue;
import net.mat0u5.matlib.services.RegistrableServer;
import net.mat0u5.matlib.utils.player.PermissionManager;
import net.minecraft.server.level.ServerPlayer;

@AutoService(RegistrableServer.class)
public class MatLibConfig extends GuiConfig implements RegistrableServer {

	public ConfigurableValue<Integer> TEST = ConfigurableValue.register(this, "test", 1)
			.name("Test value")
			.description("Test value description")
			.serverShared();


	public MatLibConfig() {
		super("matlib:main", "MatLib Config", "matlib", MatLib.MOD_ID+".properties");
		instance = this;
	}

	private static MatLibConfig instance;
	public static MatLibConfig get() {
		return instance;
	}

	@Override
	public void onRegister() {
		if (MatLib.DEBUG) ConfigManager.registerConfig(this);
	}

	@Override
	public boolean hasPermissionsToOpen(ServerPlayer player) {
		return PermissionManager.isAdmin(player);
	}
}
