package com.kuronami.awaydigest;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Away Digest — entry point.
 *
 * <p>Greets a returning player with how long they were gone and which
 * advancements others earned meanwhile, so a long-running SMP feels
 * alive. Three listeners + persistent {@link SavedData}. No mixin, no
 * config, no command, no game object.
 */
@Mod(AwayDigest.MOD_ID)
public class AwayDigest {

    public static final String MOD_ID = "awaydigest";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public AwayDigest(IEventBus modBus, ModContainer container) {
        LOGGER.info("Away Digest ready — recap on login.");
        NeoForge.EVENT_BUS.register(new AwayDigestListener());
    }
}
