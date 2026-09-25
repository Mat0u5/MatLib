package net.mat0u5.matlib.services;

public interface MatLibInitializer {
	default void onRegister() {}
	void onInitialize();
}
