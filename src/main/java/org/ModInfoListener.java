package org;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.PlayerClientBrandEvent;
import com.velocitypowered.api.event.player.PlayerModInfoEvent;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.util.ModInfo;
import org.slf4j.Logger;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// Events of different players are handled in parallel, so every message is
// logged as a single record to keep lines of different players from mixing
public class ModInfoListener {

    // Client-controlled strings are cut to this length before logging
    private static final int MAX_STRING_LENGTH = 128;

    private final Logger logger;
    // What was already printed in the current session, to skip repeats on server switch
    private final Map<UUID, String> printedBrands = new ConcurrentHashMap<>();
    private final Map<UUID, ModInfo> printedModInfo = new ConcurrentHashMap<>();

    public ModInfoListener(Logger logger) {
        this.logger = logger;
    }

    @Subscribe
    public void onPostLogin(PostLoginEvent event) {
        Player player = event.getPlayer();
        forget(player.getUniqueId());

        ProtocolVersion version = player.getProtocolVersion();
        logger.info("[MNF-check] {} joined the server: version {} (protocol {})",
                player.getUsername(),
                String.join("/", version.getVersionsSupportedBy()),
                version.getProtocol());
    }

    // Client brand ("vanilla", "fabric", "forge", ...) is sent by every client, but it is
    // not verified: any client can send any brand
    @Subscribe
    public void onClientBrand(PlayerClientBrandEvent event) {
        Player player = event.getPlayer();
        String brand = clean(event.getBrand());
        if (brand.equals(printedBrands.put(player.getUniqueId(), brand))) {
            return;
        }
        logger.info("[MNF-check] {} client brand (modloader): {}", player.getUsername(), brand);
    }

    // Mod list is only sent by legacy Forge clients (FML handshake, up to 1.12.2),
    // in reply to the handshake of a Forge backend server
    @Subscribe
    public void onPlayerModInfo(PlayerModInfoEvent event) {
        Player player = event.getPlayer();
        ModInfo info = event.getModInfo();
        if (info.equals(printedModInfo.put(player.getUniqueId(), info))) {
            return;
        }

        StringBuilder message = new StringBuilder()
                .append("[MNF-check] ").append(player.getUsername())
                .append(" mod list (").append(clean(info.getType()))
                .append("), total ").append(info.getMods().size()).append(':');
        for (ModInfo.Mod mod : info.getMods()) {
            message.append("\n   ").append(clean(mod.getId()))
                    .append(" = ").append(clean(mod.getVersion()));
        }
        logger.info(message.toString());
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        forget(event.getPlayer().getUniqueId());
    }

    private void forget(UUID playerId) {
        printedBrands.remove(playerId);
        printedModInfo.remove(playerId);
    }

    // Brand and mod list come from the client as is: remove line breaks and escape codes
    // and limit the length so they can't break or flood the log
    static String clean(String value) {
        if (value == null) {
            return "<no>";
        }
        String cleaned = value.replaceAll("\\p{Cc}", "?");
        return cleaned.length() > MAX_STRING_LENGTH
                ? cleaned.substring(0, MAX_STRING_LENGTH) + "..."
                : cleaned;
    }
}
