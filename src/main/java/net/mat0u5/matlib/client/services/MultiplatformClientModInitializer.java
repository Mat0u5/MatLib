package net.mat0u5.matlib.client.services;

public interface MultiplatformClientModInitializer {
	default void onRegister() {}
	void onInitializeClient();
}
