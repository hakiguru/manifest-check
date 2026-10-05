package org;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.login.custom.CustomQueryPayload;
import net.minecraft.resources.ResourceLocation;

public record HelloQuery(int protocol, long nonce) implements CustomQueryPayload {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("mnf-check", "hello");

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
    }
}
