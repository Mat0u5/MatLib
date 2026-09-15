package net.mat0u5.matlib.api;

public interface MatLibClientInitializer {
	default void onRegister() {}
	void onInitializeClient();
}
