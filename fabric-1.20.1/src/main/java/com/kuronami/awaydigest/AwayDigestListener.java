package com.kuronami.awaydigest;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

/**
 * Records logout times and visible advancements, then greets a
 * returning player with what they missed. All facts are certain
 * (timestamps, advancement titles) — no speculation.
 *
 * <p>Fabric: invoked as static helpers from {@code AwayDigestFabric}
 * (connection events) and {@code PlayerAdvancementsMixin} (advancement
 * grant, which Fabric exposes no first-class event for).
 */
public final class AwayDigestListener {

    private static final int MAX_SHOWN = 5;

    private AwayDigestListener() {}

    public static void onLogin(ServerPlayer player) {
        if (player == null || player.getServer() == null) {
            return;
        }
        AwayDigestData data = AwayDigestData.get(player.getServer());
        Long last = data.lastLogout(player.getUUID());
        if (last == null) {
            return; // first time we've seen them — nothing to recap yet.
        }
        long hours = Math.round((System.currentTimeMillis() - last) / 3_600_000.0);
        String me = player.getGameProfile().getName();
        List<AwayDigestData.Adv> since = data.advancementsSince(last, me);

        player.sendSystemMessage(Component.translatable("awaydigest.welcome", hours)
            .withStyle(ChatFormatting.GOLD));
        if (since.isEmpty()) {
            player.sendSystemMessage(Component.translatable("awaydigest.quiet")
                .withStyle(ChatFormatting.GRAY));
            return;
        }
        player.sendSystemMessage(Component.translatable(
            "awaydigest.adv.head", since.size()).withStyle(ChatFormatting.YELLOW));
        int shown = Math.min(since.size(), MAX_SHOWN);
        for (int i = 0; i < shown; i++) {
            AwayDigestData.Adv a = since.get(i);
            player.sendSystemMessage(Component.translatable(
                "awaydigest.adv.entry", a.player(), a.title())
                .withStyle(ChatFormatting.GRAY));
        }
        if (since.size() > shown) {
            int more = since.size() - shown;
            player.sendSystemMessage(Component.translatable(
                "awaydigest.adv.more", more).withStyle(ChatFormatting.GRAY));
        }
    }

    public static void onLogout(ServerPlayer player) {
        if (player == null || player.getServer() == null) {
            return;
        }
        AwayDigestData.get(player.getServer())
            .setLogout(player.getUUID(), System.currentTimeMillis());
    }

    /** Called by {@code PlayerAdvancementsMixin} when an advancement is newly granted. */
    public static void onAdvancement(ServerPlayer player, String title) {
        if (player == null || player.getServer() == null) {
            return;
        }
        MinecraftServer server = player.getServer();
        AwayDigestData.get(server).addAdvancement(
            System.currentTimeMillis(),
            player.getGameProfile().getName(),
            title);
    }
}
