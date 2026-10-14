package net.mat0u5.matlib.config.matlib;

import com.google.auto.service.AutoService;
import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.config.Config;
import net.mat0u5.matlib.config.ConfigManager;
import net.mat0u5.matlib.config.entry.ConfigurableValue;
import net.mat0u5.matlib.services.RegistrableServer;

@AutoService(RegistrableServer.class)
public class MatLibConfig extends Config implements RegistrableServer {
	private static final MatLibConfig INSTANCE = new MatLibConfig();
	public static ConfigurableValue<Integer> LIVES = ConfigurableValue.register(INSTANCE, "default_lives", 6)
			.name("Default Lives")
			.description("Lives each player starts with");

	@Override
	public String getModId() {
		return MatLib.MOD_ID;
	}

	@Override
	public String getConfigId() {
		return "MatLib Main Config";
	}

	@Override
	public String getFolder() {
		return "matlib";
	}

	@Override
	public String getFile() {
		return MatLib.MOD_ID+".properties";
	}

	@Override
	public void onRegister() {
		ConfigManager.registerConfig(INSTANCE);
	}
}
