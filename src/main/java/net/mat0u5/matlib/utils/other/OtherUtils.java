package net.mat0u5.matlib.utils.other;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.utils.player.PlayerUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static net.mat0u5.matlib.MatLib.server;

//? if <= 1.21.9
//import net.minecraft.world.level.gamerules.GameRules;
//? if > 1.21.9
import net.minecraft.world.level.gamerules.GameRule;

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

	protected static int parseInt(String value) {
		return value == null ? 0 : Integer.parseInt(value);
	}

	public static void executeCommand(String command) {
		try {
			if (server == null) return;
			Commands manager = server.getCommands();
			CommandSourceStack commandSource = server.createCommandSourceStack().withSuppressedOutput();
			manager.performPrefixedCommand(commandSource, command);
		} catch (Exception e) {
			MatLib.LOGGER.error("Error executing command: " + command, e);
		}
	}

	public static String getTimeAndDate() {
		LocalDateTime time = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return time.format(formatter);
	}

	public static UUID profileId(GameProfile profile) {
		if (profile == null) return null;
		//? if <= 1.21.6 {
		/*return profile.getId();
		 *///?} else {
		return profile.id();
		//?}
	}

	public static String profileName(GameProfile profile) {
		if (profile == null) return null;
		//? if <= 1.21.6 {
		/*return profile.getName();
		 *///?} else {
		return profile.name();
		//?}
	}

	public static PropertyMap profileProperties(GameProfile profile) {
		if (profile == null) return null;
		//? if <= 1.21.6 {
		/*return profile.getProperties();
		 *///?} else {
		return profile.properties();
		//?}
	}

	//? if <= 1.21.9 {
    /*public static boolean getBooleanGameRule(ServerLevel level, GameRules.Key<GameRules.BooleanValue> gamerule) {
        return level.getGameRules().getBoolean(gamerule);
    }
    public static <T extends GameRules.Value<T>> void setBooleanGameRule(ServerLevel level, GameRules.Key<GameRules.BooleanValue> gamerule, boolean value) {
        level.getGameRules().getRule(gamerule).set(value, server);
    }
    *///?} else {
	public static boolean getBooleanGameRule(ServerLevel level, GameRule<?> gamerule) {
		if (level.getGameRules().get(gamerule) instanceof Boolean bool) {
			return bool;
		}
		return false;
	}
	public static void setBooleanGameRule(ServerLevel level, GameRule<Boolean> gamerule, Boolean value) {
		level.getGameRules().set(gamerule, value, server);
	}
	//?}

	public static boolean isNumber(String text) {
		try {
			int num = Integer.parseInt(text);
			return true;
		} catch (Exception e) {}
		try {
			double num = Double.parseDouble(text);
			return true;
		} catch (Exception e) {}
		return false;
	}

	public static double clamp(double value, double min, double max) {
		if (!(min < max)) {
			if (Double.isNaN(min)) {
				throw new IllegalArgumentException("min is NaN");
			}
			if (Double.isNaN(max)) {
				throw new IllegalArgumentException("max is NaN");
			}
			if (Double.compare(min, max) > 0) {
				throw new IllegalArgumentException(min + " > " + max);
			}
		}
		return Math.min(max, Math.max(value, min));
	}

	public static float clamp(float value, float min, float max) {
		if (!(min < max)) {
			if (Double.isNaN(min)) {
				throw new IllegalArgumentException("min is NaN");
			}
			if (Double.isNaN(max)) {
				throw new IllegalArgumentException("max is NaN");
			}
			if (Double.compare(min, max) > 0) {
				throw new IllegalArgumentException(min + " > " + max);
			}
		}
		return Math.min(max, Math.max(value, min));
	}

	public static int clamp(int value, int min, int max) {
		if (!(min < max)) {
			if (Double.isNaN(min)) {
				throw new IllegalArgumentException("min is NaN");
			}
			if (Double.isNaN(max)) {
				throw new IllegalArgumentException("max is NaN");
			}
			if (Double.compare(min, max) > 0) {
				throw new IllegalArgumentException(min + " > " + max);
			}
		}
		return Math.min(max, Math.max(value, min));
	}

	public static Vec3 getCenter(BlockPos pos) {
		//? if <= 26.1 {
		/*return pos.getCenter();
		 *///?} else {
		return Vec3.atCenterOf(pos);
		//?}
	}
}
