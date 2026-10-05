package org.mixin;

import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import net.minecraft.network.protocol.login.ServerboundCustomQueryAnswerPacket;
import org.HelloQuery;
import org.HelloReport;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientHandshakePacketListenerImpl.class)
public abstract class HandshakeListenerMixin {
    @Shadow
    @Final
    private Connection connection;

    @Inject(method = "handleCustomQuery", at = @At("HEAD"), cancellable = true)
    private void mnfcheck$answerHello(ClientboundCustomQueryPacket packet, CallbackInfo ci) {
        if (!(packet.payload() instanceof HelloQuery query)) {
            return;
        }
        byte[] answer = HelloReport.build(query.nonce());
        connection.send(new ServerboundCustomQueryAnswerPacket(packet.transactionId(),
                buf -> buf.writeBytes(answer)));
        ci.cancel();
    }
}
