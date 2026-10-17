package net.mat0u5.matlib.config.entry;

import java.util.function.Supplier;

public final class ConfigRef<T> implements Supplier<T> {
    private final Supplier<ConfigurableValue<T>> valueSupplier;

    private ConfigRef(Supplier<ConfigurableValue<T>> valueSupplier) {
        this.valueSupplier = valueSupplier;
    }

    public static <T> ConfigRef<T> of(Supplier<ConfigurableValue<T>> valueSupplier) {
        return new ConfigRef<>(valueSupplier);
    }

    public ConfigurableValue<T> value() {
        return valueSupplier.get();
    }

    @Override
    public T get() {
        return value().get();
    }
}
