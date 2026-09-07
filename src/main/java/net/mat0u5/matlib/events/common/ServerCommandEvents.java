package net.mat0u5.matlib.events.common;

import com.mojang.brigadier.CommandDispatcher;
import net.mat0u5.matlib.command.Command;
import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.ArrayList;
import java.util.List;

public class ServerCommandEvents {
	/**
	 * Fires upon command registration.
	 * <p>Return a List of {@link Command} to register them.
	 */
	public static final Event<CustomRegister> CUSTOM_REGISTER = EventFactory.create(CustomRegister.class,
			listeners -> () -> {
				List<Command> allCommands = new ArrayList<>();
				for (CustomRegister listener : listeners) {
					try {
						List<Command> listenerCommands = listener.getCommands();
						if (listenerCommands != null) allCommands.addAll(listenerCommands);
					}
					catch (Exception e) {
						EventFactory.logListenerError(listener, e);
					}
				}
				return allCommands;
			}
	);

	/**
	 * Fires upon command registration.
	 * <p>You must use the arguments to register your commands as you wish.
	 */
	public static final Event<VanillaRegister> VANILLA_REGISTER = EventFactory.create(VanillaRegister.class,
			listeners -> (dispatcher, commandRegistryAccess, registrationEnvironment) -> EventFactory.dispatch(listeners, listener -> listener.register(dispatcher, commandRegistryAccess, registrationEnvironment))
	);

	@FunctionalInterface
	public interface CustomRegister {
		List<Command> getCommands();
	}

	@FunctionalInterface
	public interface VanillaRegister {
		void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandRegistryAccess, Commands.CommandSelection registrationEnvironment);
	}
}
