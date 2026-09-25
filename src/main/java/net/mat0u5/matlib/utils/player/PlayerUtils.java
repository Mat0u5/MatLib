package net.mat0u5.matlib.utils.player;

import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.utils.interfaces.FakePlayer;
import net.mat0u5.matlib.utils.other.DefaultTaskScheduler;
import net.mat0u5.matlib.utils.other.TextUtils;
import net.mat0u5.matlib.utils.world.LevelUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static net.mat0u5.matlib.MatLib.server;

//? if > 1.20.2 {
import net.minecraft.network.protocol.common.ClientboundResourcePackPopPacket;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
//?}
//? if >= 1.21.9 {
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.network.chat.contents.objects.PlayerSprite;
//?}

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

	public static boolean isFakePlayer(Entity player) {
		return player instanceof FakePlayer;
	}

	/**
	 * @return List of all online players.
	 */
	public static List<ServerPlayer> getAllPlayers() {
		List<ServerPlayer> result = new ArrayList<>();
		MinecraftServer server = MatLib.server();
		if (server == null) return result;

		for (ServerPlayer player : server.getPlayerList().getPlayers()) {
			if (isFakePlayer(player)) continue;
			result.add(player);
		}
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

	public static void sendTitleWithSubtitle(ServerPlayer player, Component title, Component subtitle, int fadeIn, int stay, int fadeOut) {
		if (server == null) return;
		if (player == null) return;
		if (!player.isAlive()) {
			DefaultTaskScheduler.scheduleTask(5, () -> sendTitleWithSubtitle(getPlayer(player.getUUID()), title, subtitle, fadeIn, stay, fadeOut));
			return;
		}
		ClientboundSetTitlesAnimationPacket fadePacket = new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut);
		player.connection.send(fadePacket);
		ClientboundSetTitleTextPacket titlePacket = new ClientboundSetTitleTextPacket(title);
		player.connection.send(titlePacket);
		ClientboundSetSubtitleTextPacket subtitlePacket = new ClientboundSetSubtitleTextPacket(subtitle);
		player.connection.send(subtitlePacket);
	}

	public static void sendTitle(ServerPlayer player, Component title, int fadeIn, int stay, int fadeOut) {
		if (server == null) return;
		if (player == null) return;
		if (!player.isAlive()) {
			DefaultTaskScheduler.scheduleTask(5, () -> sendTitle(getPlayer(player.getUUID()), title, fadeIn, stay, fadeOut));
			return;
		}
		ClientboundSetTitlesAnimationPacket fadePacket = new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut);
		player.connection.send(fadePacket);
		ClientboundSetTitleTextPacket titlePacket = new ClientboundSetTitleTextPacket(title);
		player.connection.send(titlePacket);
	}

	public static void sendTitleToPlayers(Collection<ServerPlayer> players, Component title, int fadeIn, int stay, int fadeOut) {
		for (ServerPlayer player : players) {
			sendTitle(player, title, fadeIn, stay, fadeOut);
		}
	}

	public static void sendTitleWithSubtitleToPlayers(Collection<ServerPlayer> players, Component title, Component subtitle, int fadeIn, int stay, int fadeOut) {
		for (ServerPlayer player : players) {
			sendTitleWithSubtitle(player, title, subtitle, fadeIn, stay, fadeOut);
		}
	}

	public static void playSoundToPlayers(Collection<ServerPlayer> players, SoundEvent sound) {
		playSoundToPlayers(players,sound,SoundSource.MASTER,1,1);
	}
	public static void playSoundToPlayers(Collection<ServerPlayer> players, SoundEvent sound, float volume, float pitch) {
		playSoundToPlayers(players,sound, SoundSource.MASTER, volume, pitch);
	}

	public static void playSoundToPlayers(Collection<ServerPlayer> players, SoundEvent sound, SoundSource soundCategory, float volume, float pitch) {
		for (ServerPlayer player : players) {
			if (player == null) continue;
			playNotifySoundToPlayer(player, sound, soundCategory, volume, pitch);
		}
	}

	public static void playSoundToPlayer(ServerPlayer player, SoundEvent sound) {
		playSoundToPlayer(player, sound, 1, 1);
	}

	public static void playSoundToPlayer(ServerPlayer player, SoundEvent sound, float volume, float pitch) {
		if (player == null) return;
		playNotifySoundToPlayer(player, sound, SoundSource.MASTER, volume, pitch);
	}

	private static final Random rnd = new Random();
	public static void playSoundWithSourceToPlayers(Entity source, SoundEvent sound, SoundSource soundCategory, float volume, float pitch) {
		playSoundWithSourceToPlayers(getAllPlayers(), source, sound, soundCategory, volume, pitch);
	}
	public static void playSoundWithSourceToPlayers(Collection<ServerPlayer> players, Entity source, SoundEvent sound, SoundSource soundCategory, float volume, float pitch) {
		ClientboundSoundEntityPacket packet = new ClientboundSoundEntityPacket(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound), soundCategory, source, volume, pitch, rnd.nextLong());
		for (ServerPlayer player : players) {
			player.connection.send(packet);
		}
	}


	//? if > 1.20.2 {
	/**
	 * Applies a resourcepack to a player.
	 * @param player Target player.
	 * @param link Link to the pack download.
	 * @param sha1 Pack SHA-1
	 * @param message Prompt message.
	 */
	public static void applySingleResourcepack(ServerPlayer player, String link, String sha1, String message) {
		UUID id = UUID.nameUUIDFromBytes(link.getBytes(StandardCharsets.UTF_8));
		ClientboundResourcePackPushPacket resourcepackPacket = new ClientboundResourcePackPushPacket(
				id,
				link,
				sha1,
				false,
				//? if <= 1.20.3 {
				/*Component.translatable(message)
				 *///?} else {
				Optional.of(Component.translatable(message))
				//?}
		);
		player.connection.send(resourcepackPacket);
	}

	/**
	 * Removes an applied resourcepack from a player.
	 * @param player Target player.
	 * @param link Link to the pack download.
	 */
	public static void removeSingleResourcepack(ServerPlayer player, String link) {
		UUID id = UUID.nameUUIDFromBytes(link.getBytes(StandardCharsets.UTF_8));
		ClientboundResourcePackPopPacket removePackPacket = new ClientboundResourcePackPopPacket(Optional.of(id));
		player.connection.send(removePackPacket);
	}
	//?}

	public static List<ItemStack> getPlayerInventory(ServerPlayer player) {
		List<ItemStack> list = new ArrayList<>();
		Container inventory = player.getInventory();
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack itemStack = inventory.getItem(i);
			if (!itemStack.isEmpty()) {
				list.add(itemStack);
			}
		}
		return list;
	}

	/**
	 * Removes all identical instances of an {@link ItemStack} from a players inventory.
	 */
	public static void clearItemStack(ServerPlayer player, ItemStack itemStack) {
		if (itemStack == null || itemStack.isEmpty()) return;
		Container inventory = player.getInventory();
		for (int i = 0; i < inventory.getContainerSize(); i++) {
			ItemStack stack = inventory.getItem(i);
			if (stack.equals(itemStack)) {
				inventory.removeItemNoUpdate(i);
			}
		}
	}

	/**
	 * @param maxDistance Maximum distance of the target entity.
	 * @return The entity a player is looking at.
	 */
	@Nullable
	public static Entity getEntityLookingAt(ServerPlayer player, double maxDistance) {
		Vec3 start = player.getEyePosition(1.0F);
		Vec3 direction = player.getViewVector(1.0F).normalize().scale(maxDistance);
		Vec3 end = start.add(direction);

		HitResult entityHit = ProjectileUtil.getEntityHitResult(player, start, end,
				player.getBoundingBox().expandTowards(direction).inflate(1.0),
				entity -> !entity.isSpectator() && entity.isAlive(), maxDistance*maxDistance);

		if (entityHit instanceof EntityHitResult entityHitResult) {
			return entityHitResult.getEntity();
		}

		return null;
	}

	/**
	 * @param maxDistance Maximum distance of the target block.
	 * @return The position of the block a player is looking at.
	 */
	@Nullable
	public static Vec3 getPosLookingAt(ServerPlayer player, double maxDistance) {
		HitResult blockHit = player.pick(maxDistance, 1, false);
		if (Math.sqrt(blockHit.distanceTo(player)) >= (maxDistance*0.99)) {
			return null;
		}
		if (blockHit instanceof BlockHitResult blockHitResult) {
			return blockHitResult.getLocation();
		}
		return null;
	}

	private static List<UUID> updateInventoryQueue = new ArrayList<>();

	/**
	 * Refreshes a players inventory after a server-side change.
	 */
	public static void updatePlayerInventory(ServerPlayer player) {
		if (updateInventoryQueue.contains(player.getUUID())) return;
		updateInventoryQueue.add(player.getUUID());
	}

	/**
	 * Refreshes available commands list of a specific player.
	 */
	public static void resendCommandTree(ServerPlayer player) {
		if (player == null) return;
		if (server == null) return;
		server.getCommands().sendCommands(player);
	}

	/**
	 * Refreshes available commands list of all players.
	 */
	public static void resendCommandTrees() {
		for (ServerPlayer player : getAllPlayers()) {
			resendCommandTree(player);
		}
	}

	/**
	 * @param slot Integer 0-3, with 0 being boots, and 3 being helmet
	 */
	public static ItemStack getEquipmentSlot(Player player, int slot) {
		//? if <= 1.21.4 {
		/*return player.getInventory().getArmor(slot);
		 *///?} else {
		return player.getInventory().getItem(slot + 36);
		//?}
	}

	//? if <= 1.21.4 {
    /*public static Iterable<ItemStack> getArmorItems(ServerPlayer player) {
        return player.getArmorSlots();
    }
    *///?} else {
	public static List<ItemStack> getArmorItems(ServerPlayer player) {
		List<ItemStack> result = new ArrayList<>();
		result.add(getEquipmentSlot(player, 0));
		result.add(getEquipmentSlot(player, 1));
		result.add(getEquipmentSlot(player, 2));
		result.add(getEquipmentSlot(player, 3));
		return result;
	}
	//?}

	public static void onTick() {
		if (!broadcastCooldown.isEmpty()) {
			HashMap<Component, Integer> newCooldowns = new HashMap<>();
			for (Map.Entry<Component, Integer> entry : broadcastCooldown.entrySet()) {
				Component key = entry.getKey();
				Integer value = entry.getValue();
				value--;
				if (value > 0) {
					newCooldowns.put(key, value);
				}
			}
			broadcastCooldown = newCooldowns;
		}

		if (!updateInventoryQueue.isEmpty()) {
			for (UUID uuid : updateInventoryQueue) {
				ServerPlayer player = PlayerUtils.getPlayer(uuid);
				if (player == null) continue;

				player.getInventory().tick();
				player.containerMenu.broadcastChanges();
				if (!player.isCreative()) {
					player.containerMenu.sendAllDataToRemote();
					player.inventoryMenu.sendAllDataToRemote();
				}
			}
			updateInventoryQueue.clear();
		}
	}

	private static HashMap<Component, Integer> broadcastCooldown = new HashMap<>();
	public static void broadcastMessage(Component message) {
		broadcastMessage(message, 0);
	}

	public static void broadcastMessageToAdmins(Component message) {
		broadcastMessageToAdmins(message, 0);
	}

	public static void broadcastMessage(List<ServerPlayer> players, Component message) {
		for (ServerPlayer player : players) {
			messagePlayer(player, message);
		}
	}

	public static void broadcastMessageExcept(Component message, ServerPlayer exceptPlayer) {
		for (ServerPlayer player : PlayerUtils.getAllPlayers()) {
			if (player == exceptPlayer) continue;
			messagePlayer(player, message);
		}
	}

	public static void broadcastMessage(Component message, int cooldownTicks) {
		if (cooldownTicks != 0) {
			if (broadcastCooldown.containsKey(message)) return;
			broadcastCooldown.put(message, cooldownTicks);
		}

		for (ServerPlayer player : PlayerUtils.getAllPlayers()) {
			messagePlayer(player, message);
		}
	}

	public static void broadcastMessageToAdmins(Component message, int cooldownTicks) {
		if (cooldownTicks != 0) {
			if (broadcastCooldown.containsKey(message)) return;
			broadcastCooldown.put(message, cooldownTicks);
		}

		for (ServerPlayer player : PlayerUtils.getAdminPlayers()) {
			messagePlayer(player, message);
		}
		MatLib.LOGGER.info(message.getString());
	}

	public static void teleport(ServerPlayer player, BlockPos pos) {
		LevelUtils.teleport(player, getPlayerServerLevel(player), Vec3.atBottomCenterOf(pos));
	}

	public static void teleport(ServerPlayer player, Vec3 pos) {
		LevelUtils.teleport(player, getPlayerServerLevel(player), pos);
	}

	public static void teleport(ServerPlayer player, double destX, double destY, double destZ) {
		LevelUtils.teleport(player, getPlayerServerLevel(player), destX, destY, destZ);
	}

	/**
	 * Puts a player into survival and teleports them to the ground.
	 */
	public static void safelyPutIntoSurvival(ServerPlayer player) {
		if (player.gameMode.getGameModeForPlayer() == GameType.SURVIVAL) return;

		//Teleport to the highest block in the terrain
		BlockPos.MutableBlockPos playerBlockPos = player.blockPosition().mutable();
		int safeY = LevelUtils.findTopSafeY(getPlayerServerLevel(player), Vec3.atBottomCenterOf(playerBlockPos));
		playerBlockPos.setY(safeY);
		teleport(player, playerBlockPos);

		player.setGameMode(GameType.SURVIVAL);
	}

	/**
	 * Safely kills a player from a damage source.
	 */
	public static void killFromSource(ServerPlayer player, DamageSource source) {
		player.setHealth(0.0001f);
		hurtPlayer(player, source, 10);
		if (player.isAlive()) {
			//? if <= 1.21 {
			/*player.kill();
			 *///?} else {
			player.kill(getPlayerServerLevel(player));
			//?}
		}
	}

	//? if >= 1.21.9 {
	/**
	 * @return The new player sprite icon text component.
	 */
	public static Component getPlayerIcon(ServerPlayer player) {
		return Component.object(new PlayerSprite(ResolvableProfile.createResolved(player.getGameProfile()), true));
	}
	//?}

	public static Component tryGetPlayerNameWithIcon(ServerPlayer player) {
		//? if < 1.21.9 {
		/*return player.getDisplayName();
		 *///?} else {
		return TextUtils.format("{} {}", getPlayerIcon(player), player);
		//?}
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

	/**
	 * Gets a player's UUID from their username, or {@code null} if none was found.
	 */
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

	/**
	 * Gets a player's UUID from a string uuid or a username.
	 */
	public static UUID getUUIDFromNameOrUUID(String nameOrUUID) {
		if (nameOrUUID == null || nameOrUUID.isEmpty()) return null;
		try {
			return UUID.fromString(nameOrUUID);
		}catch(Exception ignored) {}
		return getUUIDFromName(nameOrUUID);
	}

	/**
	 * Gets a player's username from their UUID, or {@code null} if none was found.
	 */
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

	/**
	 * Gets a player's username from their UUID, or the uuid itself if none was found.
	 */
	public static String getNameFromUUIDOrRaw(UUID uuid) {
		if (uuid == null) return "";
		String name = getNameFromUUID(uuid);
		if (name != null && !name.isEmpty()) return name;
		return uuid.toString();
	}

	/**
	 * Gets a player's display name from their UUID.
	 */
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
