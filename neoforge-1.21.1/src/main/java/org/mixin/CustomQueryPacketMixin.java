package org.mixin;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import net.minecraft.network.protocol.login.custom.CustomQueryPayload;
import net.minecraft.resources.ResourceLocation;
import org.HelloQuery;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientboundCustomQueryPacket.class)
public class CustomQueryPacketMixin {
    @Inject(method = "readPayload", at = @At("HEAD"), cancellable = true)
    private static void mnfcheck$readHello(ResourceLocation id, FriendlyByteBuf buf, CallbackInfoReturnable<CustomQueryPayload> cir) {
        if (!HelloQuery.ID.equals(id)) {
            return;
        }
        int protocol = buf.readInt();
        long nonce = buf.readLong();
        buf.skipBytes(buf.readableBytes());
        cir.setReturnValue(new HelloQuery(protocol, nonce));
    }
}
