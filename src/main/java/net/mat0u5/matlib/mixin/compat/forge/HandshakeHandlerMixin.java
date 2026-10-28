package net.mat0u5.matlib.mixin.compat.forge;

//? if forge && <= 1.20 {
/*import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import io.netty.buffer.Unpooled;
import net.mat0u5.matlib.mixin.ServerLoginPacketListenerImplAccessor;
import net.mat0u5.matlib.utils.other.OtherUtils;
import net.mat0u5.matlib.utils.other.RegistrySyncUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import net.minecraftforge.network.HandshakeHandler;
import net.minecraftforge.network.HandshakeMessages;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.registries.ForgeRegistry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(value = HandshakeHandler.class, priority = 1, remap = false)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public class HandshakeHandlerMixin {
    /^
        Since Forge 1.20 creates the registry packets before the player is known, we must then decode them, edit them and re-encode them.
     ^/
    @Unique
    private static final String ml$registryChannelId = "fml:handshake";
    @Unique
    private static final int ml$messageId = 3; //3 -> S2CRegistry

    @Shadow
    @Final
    private Connection manager;

    @Shadow
    private List<NetworkRegistry.LoginPayload> messageList;

    @Unique
    private boolean ml$registriesEdited = false;

    @Inject(method = "tickServer", at = @At("HEAD"))
    private void ml$editRegistries(CallbackInfoReturnable<Boolean> cir) {
        if (ml$registriesEdited) return;
        ml$registriesEdited = true;
        if (!(manager.getPacketListener() instanceof ServerLoginPacketListenerImpl listener)) return;

        Map<Integer, HandshakeMessages.S2CRegistry> registryPackets = new LinkedHashMap<>();
        Map<Identifier, Collection<Identifier>> registries = new LinkedHashMap<>();
        for (int i = 0; i < messageList.size(); i++) {
            HandshakeMessages.S2CRegistry registryPacket = ml$decodeRegistry(messageList.get(i));
            if (registryPacket == null || registryPacket.getSnapshot() == null) continue;
            registryPackets.put(i, registryPacket);
            registries.put(registryPacket.getRegistryName(), ml$snapshotIds(registryPacket.getSnapshot()).keySet());
        }
        if (registries.isEmpty()) return;

        RegistrySyncUtils.editRegistries(registries, OtherUtils.profileId(((ServerLoginPacketListenerImplAccessor) listener).getGameProfile()));

        registryPackets.forEach((index, registryPacket) -> {
            NetworkRegistry.LoginPayload payload = messageList.get(index);
            FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
            data.writeByte(ml$messageId);
            data.writeIdentifier(registryPacket.getRegistryName());
            data.writeBoolean(true);
            data.writeBytes(registryPacket.getSnapshot().getPacketData());
            messageList.set(index, new NetworkRegistry.LoginPayload(data, payload.getChannelName(), payload.getMessageContext(), payload.needsResponse()));
        });
    }

    @Unique
    @Nullable
    private static HandshakeMessages.S2CRegistry ml$decodeRegistry(NetworkRegistry.LoginPayload payload) {
        if (!ml$registryChannelId.equals(payload.getChannelName().toString())) return null;
        FriendlyByteBuf data = new FriendlyByteBuf(payload.getData().duplicate());
        if (!data.isReadable() || data.readUnsignedByte() != ml$messageId) return null;
        return HandshakeMessages.S2CRegistry.decode(data);
    }

    @Unique
    @SuppressWarnings("unchecked")
    private static Map<Identifier, ?> ml$snapshotIds(ForgeRegistry.Snapshot snapshot) {
        try {
            return (Map<Identifier, ?>) ForgeRegistry.Snapshot.class.getField("ids").get(snapshot);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Couldn't access the registry snapshot ids", e);
        }
    }
}
*///?} else {
import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = MinecraftServer.class)
@MixinEnvironment(type = MixinEnvironment.Env.MAIN)
public interface HandshakeHandlerMixin {
}
//?}
