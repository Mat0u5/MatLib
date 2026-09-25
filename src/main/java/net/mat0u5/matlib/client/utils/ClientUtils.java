package net.mat0u5.matlib.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

//? if > 1.20.5
import net.minecraft.network.DisconnectionDetails;

//? if >= 26.2
import net.minecraft.world.scores.TeamColor;

//? if <= 26.2 {
/*import net.minecraft.util.Util;
*///?} else {
import com.mojang.blaze3d.Blaze3D;
import java.net.URI;
//?}

public class ClientUtils {

    @Nullable
    public static Player getPlayer(UUID uuid) {
        Minecraft client = Minecraft.getInstance();
        if (client == null) return null;
        if (client.level == null) return null;
        return client.level.getPlayerByUUID(uuid);
    }

    @Nullable
    public static String getPlayerTeamColor() {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) return null;
        Team team = client.player.getTeam();
        //~ if >= 26.2 'team.getColor().getName()' -> 'team.getColor().orElse(TeamColor.WHITE).getSerializedName()' {
        if (team != null) return team.getColor().orElse(TeamColor.WHITE).getSerializedName();
        //~}
        return null;
    }
    @Nullable
    public static String getPlayerTeamName() {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) return null;
        Team team = client.player.getTeam();
        if (team != null) return team.getName();
        return null;
    }

    public static void runCommand(String command) {
        ClientPacketListener handler = Minecraft.getInstance().getConnection();
        if (handler == null) return;

        if (command.startsWith("/")) {
            command = command.substring(1);
        }
        handler.sendCommand(command);
    }

    public static void disconnect(Component reason) {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null) return;
        ClientPacketListener handler = client.getConnection();
        if (handler == null) return;
        //? if < 1.21.6 {
        /*client.level.disconnect();
        *///?} else {
        client.level.disconnect(reason);
        //?}
        //? if <= 1.20.5 {
        /*handler.onDisconnect(reason);
        *///?} else {
        handler.onDisconnect(new DisconnectionDetails(reason));
        //?}
    }

    public static void openExternalLink(String str) {
        //? if <= 26.2 {
        /*Util.getPlatform().openUri(str);
        *///?} else {
        Blaze3D.openUri(URI.create(str));
        //?}
    }
}
