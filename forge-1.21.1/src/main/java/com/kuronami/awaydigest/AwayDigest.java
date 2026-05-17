package com.kuronami.awaydigest;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Away Digest — entry point (Forge 1.21.1).
 *
 * <p>Greets a returning player with how long they were gone and which
 * advancements others earned meanwhile, so a long-running SMP feels
 * alive. Three listeners + persistent {@link AwayDigestData}. No mixin,
 * no config, no command, no game object.
 */
@Mod(AwayDigest.MOD_ID)
public class AwayDigest {

    public static final String MOD_ID = "awaydigest";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public AwayDigest(FMLJavaModLoadingContext context) {
        LOGGER.info("Away Digest ready — recap on login.");
        MinecraftForge.EVENT_BUS.register(new AwayDigestListener());
    }
}
