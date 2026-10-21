package net.mat0u5.matlib.services;

import net.mat0u5.matlib.MatLib;

import java.util.ServiceLoader;
import java.util.function.Consumer;

public class ServiceProvider {
	public static <T> void callListeners(Class<T> type, Consumer<T> call) {
		ServiceLoader<T> providers = ServiceLoader.load(type);
		for (T provider : providers) {
			MatLib.DEBUG.services.providerCalls.log("Calling {} provider {}", type.getSimpleName(), provider.getClass().getName());
			call.accept(provider);
		}
	}

	public static <T> ServiceLoader<T> getListeners(Class<T> type) {
		return ServiceLoader.load(type);
	}
}
