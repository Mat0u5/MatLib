package net.mat0u5.matlib.client.utils;

import com.google.auto.service.AutoService;
import net.mat0u5.matlib.client.config.MatLibClientData;
import net.mat0u5.matlib.client.events.ClientScreenEvents;
import net.mat0u5.matlib.client.gui.UpdateInfoScreen;
import net.mat0u5.matlib.client.render.RenderUtils;
import net.mat0u5.matlib.client.services.RegistrableClient;
import net.mat0u5.matlib.utils.other.UpdateChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;

import java.util.HashSet;
import java.util.Set;

@AutoService(RegistrableClient.class)
public class ClientUpdateNotifier implements RegistrableClient {
	private static final Set<String> shownScreens = new HashSet<>();

	@Override
	public void onRegister() {
		ClientScreenEvents.OPEN_SCREEN.register(ClientUpdateNotifier::onScreenOpen);
	}

	private static void onScreenOpen(Screen screen, int scaledWidth, int scaledHeight) {
		if ((screen instanceof TitleScreen)) {
			for (UpdateChecker.UpdateInfo info : UpdateChecker.getAvailableUpdates()) {
				String modId = info.mod.modId();
				if (shownScreens.contains(modId)) continue;
				if (MatLibClientData.get().isUpdateDismissed(modId, info.version) && !UpdateChecker.TEST_UPDATE_FAKE && !UpdateChecker.TEST_UPDATE_LAST) continue;

				shownScreens.add(modId);
				Minecraft.getInstance().execute(() -> RenderUtils.setScreen(new UpdateInfoScreen(info)));
				return;
			}
		}
	}
}
