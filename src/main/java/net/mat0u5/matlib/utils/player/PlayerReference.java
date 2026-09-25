package net.mat0u5.matlib.utils.player;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * PlayerReference should be used when a {@link ServerPlayer} reference can go stale.
 */
public class PlayerReference {
	private UUID uuid;

	private PlayerReference(UUID uuid) {
		this.uuid = uuid;
	}

	public static PlayerReference of(UUID uuid) {
		return new PlayerReference(uuid);
	}

	public static PlayerReference of(ServerPlayer player) {
		return new PlayerReference(player == null ? null : player.getUUID());
	}

	public @Nullable ServerPlayer get() {
		return PlayerUtils.getPlayer(uuid);
	}
}
