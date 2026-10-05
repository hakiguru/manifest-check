package org;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.proxy.LoginPhaseConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.slf4j.Logger;
import java.security.SecureRandom;

public class HelloListener {
    private static final MinecraftChannelIdentifier CHANNEL = MinecraftChannelIdentifier.create("mnf-check", "hello");
    private final SecureRandom random = new SecureRandom();
    private final Logger logger;
    public HelloListener(Logger logger) {
        this.logger = logger;
    }
    @Subscribe(priority = Short.MIN_VALUE)
    public void onPreLogin(PreLoginEvent event) {
        if (!event.getResult().isAllowed()) {
            return;
        }
        if (!(event.getConnection() instanceof LoginPhaseConnection login)){
            return;
        }
        if (login.getProtocolVersion().lessThan(ProtocolVersion.MINECRAFT_1_13)) {
            return;
        }
        long nonce = random.nextLong();
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeInt(1);
        out.writeLong(nonce);
        String name = event.getUsername();
        login.sendLoginPluginMessage(CHANNEL, out.toByteArray(), response -> {
            if (response == null) {
                logger.info("[MNF-check] {}: no mod", name);
                return;
            }
            ClientReport report = ClientReport.parse(response, nonce);
            if(report == null) {
                logger.warn("[MNF-check] {}: broken or fake answer ({} bytes)", name, response.length);
                return;
            }
            logger.info("[MNF-check] {}: pack {} v{}, {} {}, MC {}, mods: {}", name, report.packId(), report.packVersion(), report.loader(), report.loaderVersion(), report.mcVersion(), report.mods().size());
        });
    }
}
