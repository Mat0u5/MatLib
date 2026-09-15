package net.mat0u5.matlib.api;


import java.util.ServiceLoader;
import java.util.function.Consumer;

public class ApiProvider {
	public static <T> void callListeners(Class<T> type, Consumer<T> call) {
		ServiceLoader<T> providers = ServiceLoader.load(type);
		for (T provider : providers) {
			call.accept(provider);
		}
	}
}
