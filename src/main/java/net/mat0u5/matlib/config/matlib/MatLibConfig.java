package net.mat0u5.matlib.config.matlib;

import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.config.Config;
import net.mat0u5.matlib.config.entry.ConfigurableValue;

public class MatLibConfig extends Config {

	private static final MatLibConfig INSTANCE = new MatLibConfig();
	public static ConfigurableValue<Integer> LIVES = ConfigurableValue.register(INSTANCE, "default_lives", 6)
			.name("Default Lives")
			.description("Lives each player starts with");

	protected MatLibConfig() {
		super("./config/matlib/", MatLib.MOD_ID+".properties");
	}
}
