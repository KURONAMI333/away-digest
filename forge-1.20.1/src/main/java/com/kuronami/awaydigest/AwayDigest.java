package com.kuronami.awaydigest;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Away Digest — entry point (Forge 1.20.1).
 *
 * <p>Greets a returning player with how long they were gone and which
 * advancements others earned meanwhile, so a long-running SMP feels
 * alive. Three listeners + persistent {@link AwayDigestData}. No mixin,
 * no config, no command, no game object.
 *
 * <p>Forge 47.x (1.20.1) uses a no-arg {@code @Mod} constructor; only
 * the game event bus is needed here.
 */
@Mod(AwayDigest.MOD_ID)
public class AwayDigest {

    public static final String MOD_ID = "awaydigest";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public AwayDigest() {
        LOGGER.info("Away Digest ready — recap on login.");
        MinecraftForge.EVENT_BUS.register(new AwayDigestListener());
    }
}
