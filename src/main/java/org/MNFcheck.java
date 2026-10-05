package org;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

public class MNFcheck {

    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public MNFcheck(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("MNF-check is started. MODE: diagnostic");
        server.getEventManager().register(this, new ModInfoListener(logger));
        server.getEventManager().register(this, new HelloListener(logger));
    }
}
