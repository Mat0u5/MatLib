package net.mat0u5.matlib.utils.other;

import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.config.matlib.MatLibConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Two ways of using the DebugOptions:
 * <pre>{@code
 * MatLib.DEBUG.network.packetsSent.log("Sending {} to {}", id, player);
 * if (MatLib.DEBUG.network.packetsSent.enabled()) { ... }
 * }</pre>
 */
public final class DebugOptions {
	public enum Prebuilt {
		STABLE,
		DEV,
		ALL
	}

	public static DebugOptions getInstanceBasedOnConfig() {
		return of(MatLibConfig.get().LOGGING_LEVEL.get());
	}

	public static DebugOptions of(Prebuilt prebuilt) {
		return switch (prebuilt) {
			case ALL -> DebugOptions.ALL;
			case DEV -> DebugOptions.DEV;
			default -> DebugOptions.STABLE;
		};
	}

	public static final DebugOptions STABLE = builder()
			.enable(o -> o.events.listenerErrors)
			.enable(o -> o.events.environmentMismatch)
			.build();

	public static final DebugOptions DEV = builder(STABLE)
			//.enableAll(o -> o.features)
			.enable(o -> o.events.invocations)
			.enableAll(o -> o.network)
			.enableAll(o -> o.config)
			.enable(o -> o.services.providerCalls)
			.build();

	public static final DebugOptions ALL = builder().enableAll().build();

	public final FeatureOptions features = new FeatureOptions();
	public final EventOptions events = new EventOptions();
	public final NetworkOptions network = new NetworkOptions();
	public final ConfigOptions config = new ConfigOptions();
	public final SchedulerOptions scheduler = new SchedulerOptions();
	public final CommandOptions commands = new CommandOptions();
	public final ServiceOptions services = new ServiceOptions();

	private DebugOptions() {
	}

	public static Builder builder() {
		return new Builder(null);
	}

	public static Builder builder(DebugOptions base) {
		return new Builder(base);
	}

	public List<Category> categories() {
		return List.of(features, events, network, config, scheduler, commands, services);
	}

	public List<Flag> flags() {
		List<Flag> result = new ArrayList<>();
		for (Category category : categories()) {
			result.addAll(category.flags());
		}
		return result;
	}

	public List<String> enabledFlagNames() {
		List<String> result = new ArrayList<>();
		for (Flag flag : flags()) {
			if (flag.enabled()) result.add(flag.name());
		}
		return result;
	}

	public boolean anyEnabled() {
		for (Flag flag : flags()) {
			if (flag.enabled()) return true;
		}
		return false;
	}

	/**
	 * Feature enable/disables, not logging.
	 */
	public static final class FeatureOptions extends Category {
		/** Adds the Testing category to config screens. */
		public final Flag configTestingCategory = flag("configTestingCategory");

		private FeatureOptions() {
			super("features");
		}
	}

	public static final class EventOptions extends Category {
		/** Logs every invoker call of events not marked loud (ticks, rendering...). */
		public final Flag invocations = flag("invocations");
		/** Logs every invoker call of events marked loud (ticks, rendering...). Not advised to turn on. */
		public final Flag loudInvocations = flag("loudInvocations");
		/** Logs every listener registration. */
		public final Flag registrations = flag("registrations");
		/** Logs exceptions thrown by event listeners. */
		public final Flag listenerErrors = flag("listenerErrors");
		/** Logs client events triggered on the server and vice versa. */
		public final Flag environmentMismatch = flag("environmentMismatch");

		private EventOptions() {
			super("events");
		}
	}

	public static final class NetworkOptions extends Category {
		/** Logs every custom packet sent. */
		public final Flag packetsSent = flag("packetsSent");
		/** Logs every custom packet received. */
		public final Flag packetsReceived = flag("packetsReceived");
		/** Logs handshakes being sent. */
		public final Flag handshakes = flag("handshakes");
		/** Logs received simple packets which are unregistered or have no receiver. */
		public final Flag unhandledPackets = flag("unhandledPackets");
		/** Logs packets which were not sent, because the receiver can't accept them. */
		public final Flag droppedPackets = flag("droppedPackets");
		/** Logs config gui updates received from clients. */
		public final Flag configUpdates = flag("configUpdates");
		/** Logs synced textures being sent and received. */
		public final Flag syncedTextures = flag("syncedTextures");

		private NetworkOptions() {
			super("network");
		}
	}

	public static final class ConfigOptions extends Category {
		/** Logs config registrations. */
		public final Flag registrations = flag("registrations");
		/** Logs config reloads. */
		public final Flag reloads = flag("reloads");
		/** Logs configurable value changes. */
		public final Flag valueChanges = flag("valueChanges");
		/** Logs synced config values being sent and received. */
		public final Flag syncs = flag("syncs");

		private ConfigOptions() {
			super("config");
		}
	}

	public static final class SchedulerOptions extends Category {
		/** Logs tasks being scheduled in {@link TaskScheduler}. */
		public final Flag taskScheduled = flag("taskScheduled");
		/** Logs tasks being run in {@link TaskScheduler}. */
		public final Flag taskRuns = flag("taskRuns");
		/** Logs task schedulers being cleared in {@link TaskScheduler}. */
		public final Flag taskClears = flag("taskClears");

		private SchedulerOptions() {
			super("scheduler");
		}
	}

	public static final class CommandOptions extends Category {
		/** Logs custom command registrations. */
		public final Flag registrations = flag("registrations");

		private CommandOptions() {
			super("commands");
		}
	}

	public static final class ServiceOptions extends Category {
		/** Logs all calls from {@link net.mat0u5.matlib.services.ServiceProvider}. */
		public final Flag providerCalls = flag("providerCalls");

		private ServiceOptions() {
			super("services");
		}
	}

	public static abstract class Category {
		private final String name;
		private final List<Flag> flags = new ArrayList<>();

		private Category(String name) {
			this.name = name;
		}

		protected final Flag flag(String flagName) {
			Flag flag = new Flag(name + "." + flagName);
			flags.add(flag);
			return flag;
		}

		public String name() {
			return name;
		}

		public List<Flag> flags() {
			return Collections.unmodifiableList(flags);
		}
	}

	public static final class Flag {
		private final String name;
		private boolean enabled = false;

		private Flag(String name) {
			this.name = name;
		}

		public String name() {
			return name;
		}

		public boolean enabled() {
			return enabled;
		}

		public void log(String template, Object... args) {
			if (enabled) MatLib.LOGGER.info(template, args);
		}

		public void warn(String template, Object... args) {
			if (enabled) MatLib.LOGGER.warn(template, args);
		}

		public void error(String message, Throwable error) {
			if (enabled) MatLib.LOGGER.error(message, error);
		}

		@Override
		public String toString() {
			return name + "=" + enabled;
		}
	}

	public static final class Builder {
		private DebugOptions options = new DebugOptions();

		private Builder(DebugOptions base) {
			if (base == null) return;
			List<Flag> baseFlags = base.flags();
			List<Flag> flags = options.flags();
			for (int i = 0; i < flags.size(); i++) {
				flags.get(i).enabled = baseFlags.get(i).enabled;
			}
		}

		public Builder enable(Function<DebugOptions, Flag> flag) {
			return set(flag, true);
		}

		public Builder disable(Function<DebugOptions, Flag> flag) {
			return set(flag, false);
		}

		public Builder set(Function<DebugOptions, Flag> flag, boolean value) {
			flag.apply(options()).enabled = value;
			return this;
		}

		public Builder enableAll(Function<DebugOptions, Category> category) {
			return setAll(category, true);
		}

		public Builder disableAll(Function<DebugOptions, Category> category) {
			return setAll(category, false);
		}

		public Builder setAll(Function<DebugOptions, Category> category, boolean value) {
			category.apply(options()).flags.forEach(flag -> flag.enabled = value);
			return this;
		}

		public Builder enableAll() {
			options().flags().forEach(flag -> flag.enabled = true);
			return this;
		}

		public DebugOptions build() {
			DebugOptions result = options();
			options = null;
			return result;
		}

		private DebugOptions options() {
			if (options == null) throw new IllegalStateException("DebugOptions builder was already built");
			return options;
		}
	}
}
