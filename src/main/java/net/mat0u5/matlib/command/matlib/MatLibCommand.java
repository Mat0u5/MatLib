package net.mat0u5.matlib.command.matlib;

import com.mojang.brigadier.CommandDispatcher;
import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.command.Command;
import net.mat0u5.matlib.config.matlib.MatLibConfig;
import net.mat0u5.matlib.network.MatLibNetworkHandlerServer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class MatLibCommand extends Command {
	@Override
	public boolean isAllowed() {
		return true;
	}

	@Override
	public Component getBannedText() {
		return Component.empty();
	}

	@Override
	public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		if (!MatLib.DEBUG) return;
		dispatcher.register(
				literal("matlib")
						.then(literal("config")
								.executes(context -> openConfig(context.getSource()))
						)
		);
	}

	private int openConfig(CommandSourceStack source) {
		if (checkBanned(source)) return -1;
		ServerPlayer self = source.getPlayer();
		if (self == null) return -1;
		if (!MatLibNetworkHandlerServer.wasHandshakeSuccessful(self, MatLib.MOD_ID)) {
			sendCommandFailure(source, Component.nullToEmpty("You must have the MatLib mod installed §nclient-side§c to open the config GUI."));
			sendCommandFailure(source, Component.nullToEmpty("Either install the mod on the client or modify the config folder."));
			return -1;
		}
		MatLibConfig.get().openGuiFor(self);
		sendCommandFeedback(source, Component.literal("§7Opening Config GUI..."));
		return 1;
	}
}
