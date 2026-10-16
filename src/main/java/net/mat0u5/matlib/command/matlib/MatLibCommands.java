package net.mat0u5.matlib.command.matlib;

import com.google.auto.service.AutoService;
import net.mat0u5.matlib.command.Command;
import net.mat0u5.matlib.events.server.ServerRegistryEvents;
import net.mat0u5.matlib.services.RegistrableServer;

import java.util.List;

@AutoService(RegistrableServer.class)
public class MatLibCommands implements RegistrableServer {
	private static List<Command> cache = null;
	public static List<Command> getAllCommands() {
		if (cache != null) return cache;

		cache = List.of(
				new MatLibCommand()
		);
		return cache;
	}

	@Override
	public void onRegister() {
		ServerRegistryEvents.COMMAND_CUSTOM.register(MatLibCommands::getAllCommands);
	}
}
