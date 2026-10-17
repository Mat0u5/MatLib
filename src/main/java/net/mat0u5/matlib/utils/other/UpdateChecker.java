package net.mat0u5.matlib.utils.other;

import com.google.auto.service.AutoService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.events.server.ServerLifecycleEvents;
import net.mat0u5.matlib.events.server.ServerPlayerEvents;
import net.mat0u5.matlib.services.RegistrableServer;
import net.mat0u5.matlib.services.ServiceProvider;
import net.mat0u5.matlib.services.VersionTrackedMod;
import net.mat0u5.matlib.utils.player.PermissionManager;
import net.mat0u5.matlib.utils.player.PlayerUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@AutoService(RegistrableServer.class)
public class UpdateChecker implements RegistrableServer {
	public static final boolean TEST_UPDATE_FAKE = false;
	public static final boolean TEST_UPDATE_LAST = false;

	private static final ExecutorService executor = Executors.newSingleThreadExecutor(runnable -> {
		Thread thread = new Thread(runnable, "MatLib Update Checker");
		thread.setDaemon(true);
		return thread;
	});
	private static final Map<String, UpdateInfo> updates = new ConcurrentHashMap<>();
	private static boolean checked = false;

	public static class UpdateInfo {
		public final VersionTrackedMod mod;
		public volatile boolean majorUpdateAvailable = false;
		public volatile boolean updateAvailable = false;
		public volatile String majorVersionName;
		public volatile String versionName;
		public volatile String versionDescription;
		public volatile String changelogLink = null;
		public volatile int version;

		public UpdateInfo(VersionTrackedMod mod) {
			this.mod = mod;
		}

		@Nullable
		public String getChangelogLink() {
			if (changelogLink != null) return changelogLink;
			return mod.changelogUrl(mod.modVersion(), versionName);
		}

		public boolean hasUpdate() {
			return updateAvailable && versionName != null && versionDescription != null;
		}
	}

	@Override
	public void onRegister() {
		ServerPlayerEvents.FINISH_JOINING.register(UpdateChecker::onPlayerFinishJoining);
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> shutdownExecutor());
	}

	public static synchronized void checkAll() {
		if (checked) return;
		checked = true;
		for (VersionTrackedMod mod : ServiceProvider.getListeners(VersionTrackedMod.class)) {
			try {
				if (mod.checkForUpdates()) {
					checkForUpdates(mod);
				}
			} catch (Exception e) {
				MatLib.LOGGER.error("Error while starting the update check for {}", mod.modId(), e);
			}
		}
	}

	public static void checkForUpdates(VersionTrackedMod mod) {
		UpdateInfo info = new UpdateInfo(mod);
		updates.put(mod.modId(), info);
		if (TEST_UPDATE_FAKE) {
			info.updateAvailable = true;
			info.versionName = "versionName";
			info.majorVersionName = "majorVersionName";
			info.versionDescription = "versionDescription";
			info.version = 2_000_000_000;
			return;
		}
		if (executor.isShutdown()) return;
		executor.submit(() -> {
			try {
				checkForMajorUpdates(info);
			} finally {
				checkForMinorUpdates(info);
			}
		});
	}

	@Nullable
	public static UpdateInfo getUpdateInfo(String modId) {
		return updates.get(modId);
	}

	public static List<UpdateInfo> getAvailableUpdates() {
		List<UpdateInfo> result = new ArrayList<>();
		for (UpdateInfo info : updates.values()) {
			if (info.hasUpdate()) result.add(info);
		}
		return result;
	}

	private static String releasesUrl(VersionTrackedMod mod) {
		return "https://api.github.com/repos/" + mod.githubRepo() + "/releases";
	}

	private static void checkForMajorUpdates(UpdateInfo info) {
		VersionTrackedMod mod = info.mod;
		HttpURLConnection connection = null;
		try {
			// Connect to the GitHub API
			connection = (HttpURLConnection) new URI(releasesUrl(mod) + "/latest").toURL().openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);

			if (connection.getResponseCode() == 200) {
				// Parse the JSON response
				InputStreamReader reader = new InputStreamReader(connection.getInputStream());
				JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

				String name = json.get("tag_name").getAsString();

				int currentVersionNumber = VersionCompatibility.getModVersionInt(mod.modVersion());
				int updateVersionNumber = VersionCompatibility.getModVersionInt(name);

				if (currentVersionNumber < updateVersionNumber || TEST_UPDATE_LAST) {
					MatLib.LOGGER.info("New major version of {} found: {}", mod.modId(), name);
					info.updateAvailable = true;
					info.majorUpdateAvailable = true;
					info.versionName = name;
					info.majorVersionName = name;
					info.version = updateVersionNumber;
					info.versionDescription = formatDescription(info, json.get("body").getAsString());
				}
			}
			else {
				MatLib.LOGGER.error("Failed to fetch update info for {}: {}", mod.modId(), connection.getResponseCode());
			}
		} catch (Exception e) {
			MatLib.LOGGER.error("Error while checking for updates of {}: {}", mod.modId(), e.getMessage());
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	private static void checkForMinorUpdates(UpdateInfo info) {
		VersionTrackedMod mod = info.mod;
		HttpURLConnection connection = null;
		try {
			// Connect to the GitHub API
			connection = (HttpURLConnection) new URI(releasesUrl(mod)).toURL().openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);

			if (connection.getResponseCode() == 200) {
				// Parse the JSON response
				InputStreamReader reader = new InputStreamReader(connection.getInputStream());
				JsonArray jsonList = JsonParser.parseReader(reader).getAsJsonArray();

				for (JsonElement jsonElement : jsonList.asList()) {
					JsonObject json = jsonElement.getAsJsonObject();

					String name = json.get("tag_name").getAsString();
					boolean draft = json.get("draft").getAsBoolean();
					boolean prerelease = json.get("prerelease").getAsBoolean();

					if (draft || prerelease) continue;

					try {
						int currentVersionNumber = VersionCompatibility.getModVersionInt(mod.modVersion());
						int updateVersionNumber = VersionCompatibility.getModVersionInt(name);

						if (info.version < updateVersionNumber && currentVersionNumber < updateVersionNumber) {
							MatLib.LOGGER.info("New minor version of {} found: {}", mod.modId(), name);
							info.updateAvailable = true;
							info.versionName = name;
							info.version = updateVersionNumber;
							if (!info.majorUpdateAvailable) info.majorVersionName = info.versionName;
							if (!info.majorUpdateAvailable) info.versionDescription = formatDescription(info, json.get("body").getAsString());
						}
					} catch (Exception e) {
						MatLib.LOGGER.error(TextUtils.formatString("Error while parsing version number for update of {}: {} - {}", mod.modId(), name, e.getMessage()));
					}
				}
			}
			else {
				MatLib.LOGGER.error("Failed to fetch update info for {}: {}", mod.modId(), connection.getResponseCode());
			}
		} catch (Exception e) {
			MatLib.LOGGER.error("Error while checking for updates of {}: {}", mod.modId(), e.getMessage());
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public static String formatDescription(UpdateInfo info, String rawDesctiption) {
		rawDesctiption = rawDesctiption.replace("<br>","\n");
		if (rawDesctiption.contains("~~")) {
			String[] split = rawDesctiption.split("~~");
			String newDesctiption = split[0];
			if (!newDesctiption.isEmpty()) {
				rawDesctiption = newDesctiption;
			}
			if (split.length >= 2) {
				for (int i = 1; i < split.length; i++) {
					try {
						String line = split[i].trim();
						if (line.startsWith("changelogLink=")) {
							info.changelogLink = line.replace("changelogLink=", "");
						}
						if (line.startsWith("versionName=")) {
							info.versionName = line.replace("versionName=", "");
						}
						if (line.startsWith("majorVersionName=")) {
							info.majorVersionName = line.replace("majorVersionName=", "");
						}
						if (line.startsWith("versionDescription=")) {
							rawDesctiption = line.replace("versionDescription=", "");
						}
						if (line.startsWith("version=")) {
							info.version = Integer.parseInt(line.replace("version=", ""));
						}
					} catch (Exception ignored) {}
				}
			}
		}
		return rawDesctiption;
	}

	private static void onPlayerFinishJoining(ServerPlayer player) {
		for (UpdateInfo info : updates.values()) {
			if (!info.hasUpdate()) continue;
			VersionTrackedMod mod = info.mod;
			if (!mod.isUnstableVersion()) {
				if (!PermissionManager.isAdmin(player)) continue;

				MutableComponent updateText = TextUtils.formatLoosely("A new version of the {} mod is available ({}) §nserver-side§f. \n", mod.modReadableName(), info.versionName)
						.withStyle(style -> style
								.withHoverEvent(
										TextUtils.showTextHoverEvent(TextUtils.formatLoosely(
												"§7§nUpdate Description:§r\n\n{}", info.versionDescription.replace("\r", "")
										))
								)
						);
				if (mod.modrinthUrl() != null) {
					updateText.append(new ActionText("Click to download on Modrinth").styledBlueUnderline().defaultOpenURL(mod.modrinthUrl()).get());
				}
				PlayerUtils.messagePlayer(player, updateText);
				if (mod.discordUrl() != null) {
					PlayerUtils.messagePlayer(player, TextUtils.format("§7Click {}§7 to join the mod development discord if you have any questions, issues, requests, or if you just want to hang out :)", ActionText.hereTextOpenURL(mod.discordUrl())));
				}
			}
			else {
				MutableComponent updateText = Component.literal(TextUtils.formatString("§c[{}] You are playing on a developer version, there are probably some bugs, and it's possible that some features don't work.\n", mod.modReadableName()));
				if (mod.modrinthUrl() != null) {
					updateText.append(new ActionText("Download full releases on Modrinth").styledBlueUnderline().defaultOpenURL(mod.modrinthUrl()).get());
				}
				PlayerUtils.messagePlayer(player, updateText);
			}
		}
	}

	public static void shutdownExecutor() {
		executor.shutdown();
		try {
			if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
				executor.shutdownNow();
			}
		} catch (InterruptedException e) {
			executor.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}
}
