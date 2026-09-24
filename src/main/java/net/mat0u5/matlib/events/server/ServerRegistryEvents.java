package net.mat0u5.matlib.events.server;

import com.mojang.brigadier.CommandDispatcher;
import net.mat0u5.matlib.command.Command;
import net.mat0u5.matlib.events.Event;
import net.mat0u5.matlib.events.EventFactory;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.List;

public class ServerRegistryEvents {
	/**
	 * Fires upon command registration.
	 * <p>Return a List of {@link Command} to register them.
	 */
	public static final Event<CustomRegister> COMMAND_CUSTOM = EventFactory.createServer(CustomRegister.class,
			listeners -> () -> EventFactory.dispatchCollect(listeners, listener -> listener.getCommands())
	);

	/**
	 * Fires upon command registration.
	 * <p>You must use the arguments to register your commands as you wish.
	 */
	public static final Event<VanillaRegister> COMMAND_VANILLA = EventFactory.createServer(VanillaRegister.class,
			listeners -> (dispatcher, commandRegistryAccess, registrationEnvironment) -> EventFactory.dispatch(listeners, listener -> listener.onRegister(dispatcher, commandRegistryAccess, registrationEnvironment))
	);

	@FunctionalInterface
	public interface CustomRegister {
		List<Command> getCommands();
	}

	@FunctionalInterface
	public interface VanillaRegister {
		void onRegister(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandRegistryAccess, Commands.CommandSelection registrationEnvironment);
	}
}
