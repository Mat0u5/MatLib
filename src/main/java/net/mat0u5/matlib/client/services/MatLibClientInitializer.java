package net.mat0u5.matlib.client.services;

import net.mat0u5.matlib.services.Registrable;

public interface MatLibClientInitializer extends Registrable {
	void onInitializeClient();
}
