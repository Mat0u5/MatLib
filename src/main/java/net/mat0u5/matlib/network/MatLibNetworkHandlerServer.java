package net.mat0u5.matlib.network;

import com.google.auto.service.AutoService;
import com.mojang.authlib.GameProfile;
import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.config.ConfigManager;
import net.mat0u5.matlib.config.GuiConfig;
import net.mat0u5.matlib.config.SimpleConfig;
import net.mat0u5.matlib.config.entry.ConfigGuiEntry;
import net.mat0u5.matlib.config.entry.ConfigurableValue;
import net.mat0u5.matlib.events.common.CommonRegistryEvents;
import net.mat0u5.matlib.events.server.ServerNetworkEvents;
import net.mat0u5.matlib.events.server.ServerPlayerEvents;
import net.mat0u5.matlib.mixin.ServerLoginPacketListenerImplAccessor;
import net.mat0u5.matlib.network.packets.*;
import net.mat0u5.matlib.services.RegistrableServer;
import net.mat0u5.matlib.utils.enums.HandshakeStatus;
import net.mat0u5.matlib.utils.other.DefaultTaskScheduler;
import net.mat0u5.matlib.utils.other.OtherUtils;
import net.mat0u5.matlib.utils.other.TextUtils;
import net.mat0u5.matlib.utils.other.VersionCompatibility;
import net.mat0u5.matlib.utils.player.PermissionManager;
import net.mat0u5.matlib.utils.player.PlayerUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//? if <= 1.20.3 {
/*import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import java.util.function.Function;
*///?} else {
import net.minecraft.network.RegistryFriendlyByteBuf;
//?}
//? if > 1.20.5
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;

@AutoService(RegistrableServer.class)
public class MatLibNetworkHandlerServer implements RegistrableServer {
	public static final int PRELOGIN_TRANSACTION_ID = 10942423;
	public static final String preLoginPacketID = "preloginpacket";
	private static final Map<UUID, HandshakeStatus> handshakes = new ConcurrentHashMap<>();
	private static final Map<UUID, List<String>> preLoginHandshakes = new ConcurrentHashMap<>();

	//? if <= 1.20.3 {
    /*private static final Map<Identifier, Function<FriendlyByteBuf, CustomPacketPayload>> SIMPLE_PACKET_PAYLOADS = new HashMap<>();
    static {
        SIMPLE_PACKET_PAYLOADS.put(DoublePayload.ID, DoublePayload::read);
        SIMPLE_PACKET_PAYLOADS.put(StringPayload.ID, StringPayload::read);
        SIMPLE_PACKET_PAYLOADS.put(StringListPayload.ID, StringListPayload::read);
        SIMPLE_PACKET_PAYLOADS.put(LongPayload.ID, LongPayload::read);
        SIMPLE_PACKET_PAYLOADS.put(EmptyPayload.ID, EmptyPayload::read);
        SIMPLE_PACKET_PAYLOADS.put(BooleanPayload.ID, BooleanPayload::read);
        SIMPLE_PACKET_PAYLOADS.put(IntPayload.ID, IntPayload::read);
        SIMPLE_PACKET_PAYLOADS.put(HandshakePayload.ID, HandshakePayload::read);
        SIMPLE_PACKET_PAYLOADS.put(TexturePayload.ID, TexturePayload::read);
    }
    *///?} else {
	private static final List<CustomPacketPayload.TypeAndCodec<? super RegistryFriendlyByteBuf, ? extends CustomPacketPayload>> SIMPLE_PACKET_PAYLOADS = List.of(
			new CustomPacketPayload.TypeAndCodec<>(DoublePayload.ID, DoublePayload.CODEC)
			, new CustomPacketPayload.TypeAndCodec<>(StringPayload.ID, StringPayload.CODEC)
			, new CustomPacketPayload.TypeAndCodec<>(StringListPayload.ID, StringListPayload.CODEC)
			, new CustomPacketPayload.TypeAndCodec<>(LongPayload.ID, LongPayload.CODEC)
			, new CustomPacketPayload.TypeAndCodec<>(EmptyPayload.ID, EmptyPayload.CODEC)
			, new CustomPacketPayload.TypeAndCodec<>(BooleanPayload.ID, BooleanPayload.CODEC)
			, new CustomPacketPayload.TypeAndCodec<>(IntPayload.ID, IntPayload.CODEC)
			, new CustomPacketPayload.TypeAndCodec<>(HandshakePayload.ID, HandshakePayload.CODEC)
			, new CustomPacketPayload.TypeAndCodec<>(TexturePayload.ID, TexturePayload.CODEC)
	);
	//?}

	@Override
	public void onRegister() {
		CommonRegistryEvents.PACKET_PAYLOADS.register(() -> SIMPLE_PACKET_PAYLOADS);
		ServerNetworkEvents.RECEIVE_CUSTOM_PACKET.register(MatLibNetworkHandlerServer::onCustomPayload);
		ServerPlayerEvents.CONNECT.register(MatLibNetworkHandlerServer::onPlayerJoin);
		ServerPlayerEvents.DISCONNECT.register((details, player) -> MatLibNetworkHandlerServer.onPlayerLeave(player));
		initializeSimplePacketReceivers();
	}
	public void initializeSimplePacketReceivers() {
		MatLibSimplePackets.CONFIG_GUI_UPDATE.setServerReceive((serverPlayer, payload) -> {
			try {
				onConfigPayload(serverPlayer, payload);
			}catch(Exception ignored) {}
		});
		MatLibSimplePackets.CONFIG_GUI_UPDATE_END.setServerReceive((serverPlayer, payload) -> {
			try {
				onConfigPayloadEnd(serverPlayer, payload);
			}catch(Exception ignored) {}
		});
	}

	public static void onPlayerLeave(ServerPlayer player) {
		if (player != null) {
			handshakes.remove(player.getUUID());
			preLoginHandshakes.remove(player.getUUID());
		}
	}
	public static void onPlayerJoin(Connection connection, ServerPlayer player) {
		sendHandshake(player);
	}

	public static boolean onCustomPayload(CustomPacketPayload customPacketPayload, ServerPlayer player) {
		if (customPacketPayload instanceof HandshakePayload payload) {
			handleHandshakeResponse(player, payload);
		}
		else {
			return false;
		}
		return true;
	}

	public static void sendHandshake(ServerPlayer player) {
		UUID uuid = player.getUUID();
		handshakes.put(uuid, new HandshakeStatus());
		DefaultTaskScheduler.schedulePriorityTask(20, () -> {
			HandshakeStatus status = handshakes.get(uuid);
			if (status != null && status.isWaiting()) {
				status.setReceivedMods(List.of());
			}
		});
		HandshakePayload payload = MatLibNetworkHandlerCommon.getHandshakePayload();
		NetworkHandlerServer.sendPacket(player, payload);
		if (MatLib.DEBUG) MatLib.LOGGER.info(TextUtils.formatString("[PACKET_SERVER] Sending handshake to {}", player));
	}

	public static void handleHandshakeResponse(ServerPlayer player, HandshakePayload payload) {
		VersionCompatibility.Result result = MatLibNetworkHandlerCommon.getCompatibilityResult(payload, true);

		if (!result.passes()) {
			//? if <= 1.20.5 {
			/*player.connection.disconnect(Component.literal(result.getError()));
			 *///?} else {
			player.connection.disconnect(new DisconnectionDetails(Component.literal(result.getError())));
			//?}
			return;
		}

		MatLib.LOGGER.info(TextUtils.formatString("[PACKET_SERVER] Received handshake from {} with {}", player, payload.modIds()));
		handshakes.putIfAbsent(player.getUUID(), new HandshakeStatus());
		handshakes.get(player.getUUID()).setReceivedMods(payload.modIds());
		PlayerUtils.resendCommandTree(player);
	}

	public static void handlePreLogin(boolean understood, List<String> modIds, ServerLoginPacketListenerImpl handler) {
		if (!understood && !modIds.isEmpty()) {
			understood = true;
		}
		GameProfile profile = ((ServerLoginPacketListenerImplAccessor) handler).getGameProfile();
		UUID uuid = OtherUtils.profileId(profile);
		String username = OtherUtils.profileName(profile);
		preLoginHandshakes.put(uuid, modIds);

		if (understood) {
			MatLib.LOGGER.info("Received pre-login packet from " + username + " with: " + modIds);
		}
		else {
			MatLib.LOGGER.info("Did not receive pre-login packet from " + username);
		}
		ServerNetworkEvents.PRE_LOGIN_PACKET.invoker().onPreLoginPacket(handler, uuid, username, understood, modIds);
	}

	public static boolean wasHandshakeSuccessful(ServerPlayer player, String modId) {
		if (player == null) return false;
		return wasHandshakeSuccessful(player.getUUID(), modId);
	}

	public static boolean wasHandshakeSuccessful(UUID uuid, String modId) {
		if (uuid == null) return false;
		HandshakeStatus status = handshakes.get(uuid);
		if (status == null) return false;
		return status.hasReceived(modId);
	}

	public static boolean wasPreLoginHandshakeSuccessful(UUID uuid, String modId) {
		if (uuid == null) return false;
		List<String> loadedIDs = preLoginHandshakes.get(uuid);
		if (loadedIDs == null) return false;
		return loadedIDs.contains(modId);
	}

	public static void onConfigPayload(ServerPlayer player, StringListPayload payload) throws Exception {
		ConfigGuiEntry entry = ConfigGuiEntry.decode(payload.value());
		if (!(ConfigManager.get(entry.configId) instanceof GuiConfig config)) return;
		if (!config.hasPermissionsToOpen(player)) return;
		if (MatLib.DEBUG) MatLib.LOGGER.info(TextUtils.formatString("[PACKET_SERVER] Received config update from {}: {{}, {}, {}}", player, entry.configId, entry.key, entry.args));

		ConfigurableValue<?> value = config.getValue(entry.key);
		if (value == null) {
			config.onDynamicEntryUpdate(player, entry);
			return;
		}
		try {
			value.setFromString(entry.value());
		} catch (Exception e) {
			MatLib.LOGGER.error("Invalid config value '{}' for {}:{} from {}", entry.value(), entry.configId, entry.key, player, e);
		}
	}
	public static void onConfigPayloadEnd(ServerPlayer player, StringListPayload payload) throws Exception {
		List<String> configIds = new ArrayList<>(payload.value());
		boolean reloadServer = Boolean.parseBoolean(configIds.remove(0));
		configIds.removeIf(configId -> !(ConfigManager.get(configId) instanceof GuiConfig config) || !config.hasPermissionsToOpen(player));
		if (configIds.isEmpty()) return;
		if (reloadServer) {
			OtherUtils.reloadServer();
		}
		else {
			ConfigManager.reloadConfigs(configIds);
		}
	}
}
