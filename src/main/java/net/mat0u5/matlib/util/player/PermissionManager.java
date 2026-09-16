package net.mat0u5.matlib.util.player;

import net.mat0u5.matlib.MatLib;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import static net.mat0u5.matlib.MatLib.server;

public class PermissionManager {

    /**
     * Checks if a player has operator status or is in singleplayer.
     */
    public static boolean isAdmin(ServerPlayer player) {
        if (player == null) return false;
        //if (MatLib.isClientPlayer(player.getUUID())) return true;//TODO
        if (server == null) return false;
        //? if < 1.21.9 {
        /*return server.getPlayerList().isOp(player.getGameProfile());
        *///?} else {
        return server.getPlayerList().isOp(player.nameAndId());
        //?}
    }

     /**
     * Checks if a command source is an admin or a player with operator status or in singleplayer.
     */
    public static boolean isAdmin(CommandSourceStack source) {
        if (source.getEntity() == null) return true;
        return isAdmin(source.getPlayer());
    }
}
