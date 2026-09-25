package net.mat0u5.matlib.utils.interfaces;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;

public interface CustomPacketPayload {
    void write(FriendlyByteBuf friendlyByteBuf);

    Identifier id();
}
