package net.mat0u5.matlib.utils.player;

import com.google.auto.service.AutoService;
import net.mat0u5.matlib.events.server.ServerLifecycleEvents;
import net.mat0u5.matlib.events.server.ServerPlayerEvents;
import net.mat0u5.matlib.events.server.ServerTickEvents;
import net.mat0u5.matlib.services.RegistrableServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@AutoService(RegistrableServer.class)
public class PlayerJoinTracker implements RegistrableServer {
	private record JoinState(Vec3 pos, float yaw, float pitch) {}

	private static final Map<UUID, JoinState> joiningPlayers = new ConcurrentHashMap<>();

	@Override
	public void onRegister() {
		ServerPlayerEvents.CONNECT.register((connection, player) -> onStartJoining(player));
		ServerPlayerEvents.DISCONNECT.register((details, player) -> {
			if (player != null) {
				joiningPlayers.remove(player.getUUID());
			}
		});
		ServerTickEvents.END_TICK.register(server -> tick());
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> joiningPlayers.clear());
	}

	private static void onStartJoining(ServerPlayer player) {
		if (player == null || PlayerUtils.isFakePlayer(player)) return;
		joiningPlayers.put(player.getUUID(), new JoinState(player.position(), player.getYRot(), player.getXRot()));
	}

	public static boolean isCurrentlyJoining(UUID uuid) {
		return joiningPlayers.containsKey(uuid);
	}

	private static void tick() {
		if (joiningPlayers.isEmpty()) return;
		List<ServerPlayer> finishedPlayers = new ArrayList<>();
		for (Map.Entry<UUID, JoinState> entry : joiningPlayers.entrySet()) {
			ServerPlayer player = PlayerUtils.getPlayer(entry.getKey());
			if (player == null) continue;
			JoinState state = entry.getValue();
			if (player.position().equals(state.pos()) && player.getYRot() == state.yaw() && player.getXRot() == state.pitch()) continue;
			finishedPlayers.add(player);
		}
		for (ServerPlayer player : finishedPlayers) {
			joiningPlayers.remove(player.getUUID());
			ServerPlayerEvents.FINISH_JOINING.invoker().onFinishJoining(player);
		}
	}
}
