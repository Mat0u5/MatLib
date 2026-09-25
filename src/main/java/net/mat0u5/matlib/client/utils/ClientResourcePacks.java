package net.mat0u5.matlib.client.utils;

import net.mat0u5.matlib.MatLib;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.Pack;

public class ClientResourcePacks {
    /**
     * Enable or Disable a specific client resource pack.
     */
    public static void setClientResourcepack(String id, boolean enable) {
        if (enable) {
            enableClientResourcePack(id);
        }
        else {
            disableClientResourcePack(id);
        }
    }

    /**
     * Enable a specific client resource pack.
     */
    public static void enableClientResourcePack(String id) {
        enableClientResourcePack(id, false);
    }

    /**
     * Enable a specific client resource pack.
     * @param forceReload Reload packs even if the given resourcepack was not found.
     */
    public static void enableClientResourcePack(String id, boolean forceReload) {
        Minecraft client = Minecraft.getInstance();
        if (client.getResourcePackRepository() != null && !client.getResourcePackRepository().getSelectedIds().contains(id)) {
            for (Pack profile : client.getResourcePackRepository().getAvailablePacks()) {
                if (profile.getId().equals(id)) {
                    client.getResourcePackRepository().addPack(id);
                    MatLib.LOGGER.info("Enabling resourcepack " + id);
                    client.reloadResourcePacks();
                    return;
                }
            }
        }
        if (forceReload) {
            MatLib.LOGGER.info("Force enabling resourcepack " + id);
            client.reloadResourcePacks();
        }
    }

    /**
     * Disable a specific client resource pack.
     */
    public static void disableClientResourcePack(String id) {
        Minecraft client = Minecraft.getInstance();
        if (client.getResourcePackRepository() == null) return;
        if (!client.getResourcePackRepository().getSelectedIds().contains(id)) return;

        for (Pack profile : client.getResourcePackRepository().getAvailablePacks()) {
            if (profile.getId().equals(id)) {
                client.getResourcePackRepository().removePack(id);
                MatLib.LOGGER.info("Disabling resourcepack " + id);
                client.reloadResourcePacks();
                return;
            }
        }
    }
}
