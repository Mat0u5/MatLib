package net.mat0u5.matlib.util.player;

import net.mat0u5.matlib.MatLib;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtils {

	/**
	 * Sends a chat message to a player.
	 */
	public static void messagePlayer(ServerPlayer player, Component component) {
		if (component.getString().isEmpty()) return;
		player.sendSystemMessage(component, false);
	}

	/**
	 * Sends a message to a player.
	 * @param aboveHotbar Controls whether the message is in chat or above the hotbar.
	 */
	public static void messagePlayer(ServerPlayer player, Component component, boolean aboveHotbar) {
		if (component.getString().isEmpty()) return;
		player.sendSystemMessage(component, aboveHotbar);
	}

	/**
	 * @return List of all online players.
	 */
	public static List<ServerPlayer> getAllPlayers() {
		List<ServerPlayer> result = new ArrayList<>();
		MinecraftServer server = MatLib.server();
		if (server == null) return result;

		result.addAll(server.getPlayerList().getPlayers());
		return result;
	}
}
