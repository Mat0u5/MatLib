package net.mat0u5.matlib.client.services;

public interface MatLibClientInitializer {
	default void onRegister() {}
	void onInitializeClient();
}
