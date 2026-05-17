package com.kuronami.awaydigest;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Away Digest — entry point (Fabric 1.20.1).
 *
 * <p>Greets a returning player with how long they were gone and which
 * advancements others earned meanwhile, so a long-running SMP feels
 * alive. Login/logout go through {@code ServerPlayConnectionEvents};
 * advancement detection has no first-class Fabric API, so a small
 * {@code PlayerAdvancementsMixin} feeds {@link AwayDigestListener}.
 * Persistent {@link AwayDigestData}. No config, no command, no game object.
 */
public class AwayDigestFabric implements ModInitializer {

    public static final String MOD_ID = "awaydigest";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Away Digest ready — recap on login.");

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) ->
            AwayDigestListener.onLogin(handler.player));

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
            AwayDigestListener.onLogout(handler.player));
    }
}
