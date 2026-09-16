package net.mat0u5.matlib.util.other;

import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.util.player.PlayerUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class OtherUtils {

	public static void log(Component message) {
		for (ServerPlayer player : PlayerUtils.getAllPlayers()) {
			PlayerUtils.messagePlayer(player, message);
		}
		MatLib.LOGGER.info(message.getString());
	}

	public static void log(String string) {
		Component message = Component.nullToEmpty(string);
		for (ServerPlayer player : PlayerUtils.getAllPlayers()) {
			PlayerUtils.messagePlayer(player, message);
		}
		MatLib.LOGGER.info(string);
	}

	public static void logConsole(String string) {
		MatLib.LOGGER.info(string);
	}

	public static void logIfClient(String string) {
		//TODO
		/*if (x.hasClient()) {
			MatLib.LOGGER.info(string);
		}*/
	}
}
