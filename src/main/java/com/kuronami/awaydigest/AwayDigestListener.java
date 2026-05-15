package com.kuronami.awaydigest;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * Records logout times and visible advancements, then greets a
 * returning player with what they missed. All facts are certain
 * (timestamps, advancement titles) — no speculation.
 */
public class AwayDigestListener {

    private static final int MAX_SHOWN = 5;

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || player.getServer() == null) {
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

    @SubscribeEvent
    public void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || player.getServer() == null) {
            return;
        }
        AwayDigestData.get(player.getServer())
            .setLogout(player.getUUID(), System.currentTimeMillis());
    }

    @SubscribeEvent
    public void onAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || player.getServer() == null) {
            return;
        }
        var holder = event.getAdvancement();
        if (holder.value().display().isEmpty()) {
            return; // recipe / hidden advancement — not digest-worthy.
        }
        MinecraftServer server = player.getServer();
        AwayDigestData.get(server).addAdvancement(
            System.currentTimeMillis(),
            player.getGameProfile().getName(),
            holder.value().display().get().getTitle().getString());
    }
}
