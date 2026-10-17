package net.mat0u5.matlib.client.config;

import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.config.SimpleConfig;

public class MatLibClientData extends SimpleConfig {
	private static MatLibClientData instance;

	private MatLibClientData() {
		super(MatLib.MOD_ID + ":client_data", "matlib", MatLib.MOD_ID + "_client_data.properties");
	}

	public static MatLibClientData get() {
		if (instance == null) instance = new MatLibClientData();
		return instance;
	}

	public boolean isUpdateDismissed(String modId, int version) {
		return getOrCreateInt("ignore_update." + modId, 0) == version;
	}

	public void dismissUpdate(String modId, int version) {
		setProperty("ignore_update." + modId, String.valueOf(version));
	}
}
