package com.kuronami.awaydigest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

/**
 * Persists each player's last-logout time and a bounded ring of
 * recently-earned advancements (who + what + when), so a returning
 * player can be told what they missed. Survives restarts via vanilla
 * {@link SavedData} ({@code world/data/awaydigest.dat}).
 */
public class AwayDigestData extends SavedData {

    private static final String NAME = "awaydigest";
    private static final int MAX_ADV = 100;

    private static final Factory<AwayDigestData> FACTORY =
        new Factory<>(AwayDigestData::new, AwayDigestData::load, null);

    public record Adv(long ms, String player, String title) {}

    private final Map<UUID, Long> lastLogout = new HashMap<>();
    private final List<Adv> adv = new ArrayList<>(); // oldest first

    public static AwayDigestData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(FACTORY, NAME);
    }

    public Long lastLogout(UUID uuid) {
        return lastLogout.get(uuid);
    }

    public void setLogout(UUID uuid, long ms) {
        lastLogout.put(uuid, ms);
        setDirty();
    }

    public void addAdvancement(long ms, String player, String title) {
        adv.add(new Adv(ms, player, title));
        while (adv.size() > MAX_ADV) {
            adv.remove(0);
        }
        setDirty();
    }

    /** Advancements after {@code sinceMs} earned by someone other than {@code exceptPlayer}. */
    public List<Adv> advancementsSince(long sinceMs, String exceptPlayer) {
        List<Adv> out = new ArrayList<>();
        for (Adv a : adv) {
            if (a.ms() > sinceMs && !a.player().equals(exceptPlayer)) {
                out.add(a);
            }
        }
        return out;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        CompoundTag logouts = new CompoundTag();
        lastLogout.forEach((u, ms) -> logouts.putLong(u.toString(), ms));
        tag.put("logouts", logouts);

        ListTag lt = new ListTag();
        for (Adv a : adv) {
            CompoundTag c = new CompoundTag();
            c.putLong("ms", a.ms());
            c.putString("p", a.player());
            c.putString("t", a.title());
            lt.add(c);
        }
        tag.put("adv", lt);
        return tag;
    }

    private static AwayDigestData load(CompoundTag tag, HolderLookup.Provider registries) {
        AwayDigestData d = new AwayDigestData();
        CompoundTag logouts = tag.getCompound("logouts");
        for (String k : logouts.getAllKeys()) {
            try {
                d.lastLogout.put(UUID.fromString(k), logouts.getLong(k));
            } catch (IllegalArgumentException ignored) {
                // skip non-UUID key rather than fail world load
            }
        }
        ListTag lt = tag.getList("adv", Tag.TAG_COMPOUND);
        for (int i = 0; i < lt.size(); i++) {
            CompoundTag c = lt.getCompound(i);
            d.adv.add(new Adv(c.getLong("ms"), c.getString("p"), c.getString("t")));
        }
        return d;
    }
}
