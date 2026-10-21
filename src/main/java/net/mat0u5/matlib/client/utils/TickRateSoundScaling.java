package net.mat0u5.matlib.client.utils;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BooleanSupplier;

public class TickRateSoundScaling {
	private static final List<BooleanSupplier> conditions = new CopyOnWriteArrayList<>();
	private static final Set<String> excludedSounds = ConcurrentHashMap.newKeySet();

	/**
	 * Controls whether should scaling should be enabled at all.
	 */
	public static void addEnableCondition(BooleanSupplier condition) {
		if (condition != null) conditions.add(condition);
	}

	/**
	 * Sounds excluded from scaling.
	 */
	public static void excludeSound(String... sounds) {
		excludedSounds.addAll(List.of(sounds));
	}

	public static boolean isEnabled() {
		for (BooleanSupplier condition : conditions) {
			try {
				if (condition.getAsBoolean()) return true;
			} catch (Exception ignored) {}
		}
		return false;
	}

	public static boolean shouldScaleSound(SoundInstance sound) {
		if (conditions.isEmpty()) return false;
		//? if <= 1.21.9 {
		//Identifier id = sound.getLocation();
		//?} else {
		Identifier id = sound.getIdentifier();
		//?}
		if (id != null && (excludedSounds.contains(id.toString()) || excludedSounds.contains(id.getPath()))) return false;
		return isEnabled();
	}
}
