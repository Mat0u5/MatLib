package net.mat0u5.matlib.api;

public interface MatLibInitializer {
	default void onRegister() {}
	void onInitialize();
}
