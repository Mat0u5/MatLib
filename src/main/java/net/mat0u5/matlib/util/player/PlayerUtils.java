package net.mat0u5.matlib.util.player;

import net.mat0u5.matlib.MatLib;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.mat0u5.matlib.MatLib.server;

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
	 * Damages a player.
	 */
	public static void hurtPlayer(ServerPlayer player, DamageSource source, float amount) {
		//? if <= 1.21 {
		/*player.hurt(source, amount);
		 *///?} else {
		player.hurtServer(getPlayerServerLevel(player), source, amount);
		//?}
	}

	/**
	 * Damages a player.
	 */
	public static void hurtPlayer(ServerPlayer player, ServerLevel level, DamageSource source, float amount) {
		//? if <= 1.21 {
		/*player.hurt(source, amount);
		 *///?} else {
		player.hurtServer(level, source, amount);
		//?}
	}

	/**
	 * Returns {@code player}'s {@code ServerLevel}.
	 */
	public static ServerLevel getPlayerServerLevel(ServerPlayer player) {
		//? if <= 1.21.5 {
		/*return player.serverLevel();
		 *///?} else {
		return player.level();
		//?}
	}

	/**
	 * Plays a sound to a player with no origin.
	 */
	public static void playNotifySoundToPlayer(ServerPlayer player, SoundEvent sound, SoundSource soundSource, float volume, float pitch) {
		//? if <= 1.21.9 {
		/*player.playNotifySound(sound, soundSource, volume, pitch);
		 *///?} else {
		player.connection
				.send(
						new ClientboundSoundPacket(
								BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound), soundSource, player.getX(), player.getY(), player.getZ(), volume, pitch, player.getRandom().nextLong()
						)
				);
		//?}
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

	/**
	 * @return List of admin players.
	 */
	public static List<ServerPlayer> getAdminPlayers() {
		List<ServerPlayer> result = getAllPlayers();
		result.removeIf(player -> !PermissionManager.isAdmin(player));
		return result;
	}

	/**
	 * @return {@link ServerPlayer} with the specified username.
	 */
	public static ServerPlayer getPlayer(String name) {
		if (server == null || name == null) return null;
		return server.getPlayerList().getPlayerByName(name);
	}

	/**
	 * @return {@link ServerPlayer} with the specified {@link UUID}.
	 */
	public static ServerPlayer getPlayer(UUID uuid) {
		if (server == null || uuid == null) return null;
		return server.getPlayerList().getPlayer(uuid);
	}


	/**
	 * Checks if a username is a valid Minecraft username.
	 */
	public static final int MAX_USERNAME_LENGTH = 16;
	public static boolean isValidUsername(String name) {
		if (name == null || name.isEmpty()) return false;
		if (name.length() > MAX_USERNAME_LENGTH) return false;
		for (int i = 0; i < name.length(); i++) {
			char chr = name.charAt(i);
			if (chr >= 'a' && chr <= 'z') continue;
			if (chr >= 'A' && chr <= 'Z') continue;
			if (chr >= '0' && chr <= '9') continue;
			if (chr == '_') continue;
			return false;
		}
		return true;
	}

	public static UUID getUUIDFromName(String name) {
		if (name == null || name.isEmpty()) return null;
		ServerPlayer onlinePlayer = getPlayer(name);
		if (onlinePlayer != null) return onlinePlayer.getUUID();
		if (server == null) return null;
		try {
			//? if <= 1.21.6 {
				/*if (server.getProfileCache() != null) {
					var profile = server.getProfileCache().get(name);
					if (profile.isPresent()) return profile.get().getId();
				}
				*///?} else {
			if (server.services().nameToIdCache() != null) {
				var profile = server.services().nameToIdCache().get(name);
				if (profile.isPresent()) return profile.get().id();
			}
			//?}
		}catch(Exception ignored) {}
		return null;
	}

	public static UUID getUUIDFromNameOrUUID(String nameOrUUID) {
		if (nameOrUUID == null || nameOrUUID.isEmpty()) return null;
		try {
			return UUID.fromString(nameOrUUID);
		}catch(Exception ignored) {}
		return getUUIDFromName(nameOrUUID);
	}

	public static String getNameFromUUID(UUID uuid) {
		if (uuid == null) return null;
		ServerPlayer onlinePlayer = getPlayer(uuid);
		if (onlinePlayer != null) return onlinePlayer.getScoreboardName();
		if (server == null) return null;
		try {
			//? if <= 1.21.6 {
				/*if (server.getProfileCache() != null) {
					var profile = server.getProfileCache().get(uuid);
					if (profile.isPresent()) return profile.get().getName();
				}
				*///?} else {
			if (server.services().nameToIdCache() != null) {
				var profile = server.services().nameToIdCache().get(uuid);
				if (profile.isPresent()) return profile.get().name();
			}
			//?}
		}catch(Exception ignored) {}
		return null;
	}

	public static String getNameFromUUIDOrRaw(UUID uuid) {
		if (uuid == null) return "";
		String name = getNameFromUUID(uuid);
		if (name != null && !name.isEmpty()) return name;
		return uuid.toString();
	}

	public static Component getDisplayNameFromUUID(UUID uuid) {
		if (uuid == null) return Component.empty();
		ServerPlayer onlinePlayer = getPlayer(uuid);
		if (onlinePlayer != null) {
			Component displayName = onlinePlayer.getDisplayName();
			if (displayName != null) return displayName;
		}
		return Component.literal(getNameFromUUIDOrRaw(uuid));
	}
}
