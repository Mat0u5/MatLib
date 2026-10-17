package net.mat0u5.matlib.entity.fakeplayer;

/*
 * This file includes code from the Fabric Carpet project: https://github.com/gnembon/fabric-carpet
 *
 * Used and modified under the MIT License.
 */

import com.mojang.authlib.GameProfile;
import net.mat0u5.matlib.utils.interfaces.FakePlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

//? if > 1.20 {
import net.minecraft.server.level.ClientInformation;
//?}

//? if <= 1.21.6 {

/*//? if <= 1.20 {
/^import java.util.concurrent.atomic.AtomicReference;
^///?}
//? if > 1.20.5 {
import net.minecraft.network.DisconnectionDetails;
//?}
//? if >= 1.20.2 {
import net.minecraft.server.network.CommonListenerCookie;
//?}
//? if > 1.20 && <= 1.20.3 {
/^import net.mat0u5.matlib.mixin.SkullBlockEntityAccessor;
^///?}

import net.mat0u5.matlib.utils.world.LevelUtils;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
*///?}

@SuppressWarnings("EntityConstructor")
public class FakeServerPlayer extends ServerPlayer implements FakePlayer {
    //? if <= 1.20 {
    /*protected FakeServerPlayer(MinecraftServer server, ServerLevel levelIn, GameProfile profile) {
        super(server, levelIn, profile);
    }
    *///?} else {
    protected FakeServerPlayer(MinecraftServer server, ServerLevel levelIn, GameProfile profile, ClientInformation cli) {
        super(server, levelIn, profile, cli);
    }
    //?}

    @FunctionalInterface
    public interface Factory<T extends FakeServerPlayer> {
        //? if <= 1.20 {
        /*T create(MinecraftServer server, ServerLevel level, GameProfile profile);
        *///?} else {
        T create(MinecraftServer server, ServerLevel level, GameProfile profile, ClientInformation clientInformation);
        //?}
    }

    //? if <= 1.21.6 {
    /*private static final Set<String> spawning = new HashSet<>();
    public Runnable fixStartingPosition = () -> {};

    public static CompletableFuture<FakeServerPlayer> create(
            String username, MinecraftServer server, Vec3 pos, double yaw, double pitch,
            ResourceKey<Level> dimensionId, GameType gamemode, boolean flying, @Nullable Inventory inv) {
        return create(FakeServerPlayer::new, username, server, pos, yaw, pitch, dimensionId, gamemode, flying, inv);
    }

    public static <T extends FakeServerPlayer> CompletableFuture<T> create(
            Factory<T> factory, String username, MinecraftServer server, Vec3 pos, double yaw, double pitch,
            ResourceKey<Level> dimensionId, GameType gamemode, boolean flying, @Nullable Inventory inv) {
        ServerLevel levelIn = server.getLevel(dimensionId);
        GameProfileCache.setUsesAuthentication(false);
        GameProfile gameprofile = null;
        try {
            if (server.getProfileCache() != null) {
                Optional<GameProfile> opt = server.getProfileCache().get(username);
                if (opt.isPresent()) {
                    gameprofile = opt.get();
                }
            }
        } catch(Exception ignored) {}
        finally {
            GameProfileCache.setUsesAuthentication(server.isDedicatedServer() && server.isPublished());
        }
        if (gameprofile == null) {
            gameprofile = new GameProfile(UUIDUtil.createOfflinePlayerUUID(username), username);
        }
        GameProfile finalGP = gameprofile;

        String name = gameprofile.getName();
        spawning.add(name);

        CompletableFuture<T> future = new CompletableFuture<>();

        fetchGameProfile(server, name).whenCompleteAsync((profile, throwable) -> {
            spawning.remove(name);
            if (throwable != null) {
                future.completeExceptionally(throwable);
                return;
            }
            GameProfile current = finalGP;
            if (profile.isPresent()) current = profile.get();

            //? if <= 1.20 {
            /^T instance = factory.create(server, levelIn, current);
            ^///?} else {
            T instance = factory.create(server, levelIn, current, ClientInformation.createDefault());
            //?}
            //? if <= 1.21.4 {
            /^instance.fixStartingPosition = () -> instance.moveTo(pos.x, pos.y, pos.z, (float) yaw, (float) pitch);
            ^///?} else {
            instance.fixStartingPosition = () -> instance.snapTo(pos.x, pos.y, pos.z, (float) yaw, (float) pitch);
            //?}
            FakeClientConnection connection = new FakeClientConnection(PacketFlow.SERVERBOUND);
            //? if <= 1.20 {
            /^server.getPlayerList().placeNewPlayer(connection, instance);
            ^///?} else if <= 1.20.3 {
            /^CommonListenerCookie data = new CommonListenerCookie(current, 0, instance.clientInformation());
            server.getPlayerList().placeNewPlayer(connection, instance, data);
            ^///?} else {
            CommonListenerCookie data = new CommonListenerCookie(current, 0, instance.clientInformation(), true);
            server.getPlayerList().placeNewPlayer(connection, instance, data);
            //?}
            LevelUtils.teleport(instance, levelIn, pos, (float) yaw, (float) pitch);
            instance.setHealth(20.0F);
            instance.unsetRemoved();
            instance.setGameMode(gamemode);
            server.getPlayerList().broadcastAll(new ClientboundRotateHeadPacket(instance, (byte) (instance.getYRot() * 256 / 360)));
            instance.entityData.set(DATA_PLAYER_MODE_CUSTOMISATION, (byte) 0x7f);
            instance.getAbilities().flying = flying;

            if (inv != null) {
                instance.getInventory().replaceWith(inv);
                instance.getInventory().setChanged();
                instance.getInventory().tick();
                instance.containerMenu.broadcastChanges();
            }

            instance.removeAllEffects();
            instance.setSharedFlagOnFire(false);
            instance.setRemainingFireTicks(0);
            future.complete(instance);
        }, server);

        return future;
    }

    public static boolean isSpawning(String name) {
        return spawning.contains(name);
    }

    private static CompletableFuture<Optional<GameProfile>> fetchGameProfile(MinecraftServer server, final String name) {
        //? if <= 1.20 {
        /^GameProfile gameprofile;
        try {
            gameprofile = server.getProfileCache().get(name).orElse(null);
        }
        finally {
            GameProfileCache.setUsesAuthentication(server.isDedicatedServer() && server.usesAuthentication());
        }
        if (gameprofile == null) {
            gameprofile = new GameProfile(UUIDUtil.createOfflinePlayerUUID(name), name);
        }
        if (gameprofile.getProperties().containsKey("textures")) {
            AtomicReference<GameProfile> result = new AtomicReference<>();
            SkullBlockEntity.updateGameprofile(gameprofile, result::set);
            gameprofile = result.get();
        }
        return CompletableFuture.completedFuture(Optional.ofNullable(gameprofile));
        ^///?} else if <= 1.20.3 {
        /^return SkullBlockEntityAccessor.ml$fetchGameProfile(name);
        ^///?} else {
        return SkullBlockEntity.fetchGameProfile(name);
        //?}
    }

    public boolean shouldStayConnected() {
        return true;
    }

    public void disconnectFake() {
        //? if <= 1.20.5 {
        /^connection.onDisconnect(Component.empty());
        ^///?} else {
        connection.onDisconnect(new DisconnectionDetails(Component.empty()));
        //?}
    }

    @Override
    public String getIpAddress() {
        return "127.0.0.1";
    }

    @Override
    public boolean allowsListing() {
        return false;
    }

    @Override
    public void tick() {
        if (tickCount % 20 == 0 && !shouldStayConnected()) {
            disconnectFake();
        }
        if (tickCount % 10 == 0) {
            this.connection.resetPosition();
        }
        try {
            super.tick();
            doTick();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    *///?}
}
