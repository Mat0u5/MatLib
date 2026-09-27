package net.mat0u5.matlib.services;

public interface MultiplatformModInitializer {
	default void onRegister() {}
	void onInitialize();
}
