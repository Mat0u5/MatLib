package net.mat0u5.matlib.command;

import com.mojang.brigadier.CommandDispatcher;
import net.mat0u5.matlib.events.server.ServerRegistryEvents;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    public static List<Command> registeredCommands = new ArrayList<>();

    /**
     * Triggered by {@link net.mat0u5.matlib.mixin.CommandsMixin}
     */
    public static void registerCustomCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandRegistryAccess, Commands.CommandSelection registrationEnvironment) {
        if (registeredCommands.isEmpty()) {
            registeredCommands = ServerRegistryEvents.COMMAND_CUSTOM.invoker().getCommands();
        }
        for (Command command : registeredCommands) {
            command.register(dispatcher, commandRegistryAccess);
        }
    }
}
