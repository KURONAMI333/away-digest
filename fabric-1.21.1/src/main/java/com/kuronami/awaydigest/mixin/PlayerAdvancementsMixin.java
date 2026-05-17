package com.kuronami.awaydigest.mixin;

import com.kuronami.awaydigest.AwayDigestListener;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Fabric 1.21.1: hook into {@link PlayerAdvancements#award} to detect when an
 * advancement is newly granted (Fabric has no first-class advancement event).
 *
 * <p>The {@code award} method is called for every criterion check. We only act
 * when it returns {@code true} (i.e., the advancement was actually granted now,
 * not previously) and the advancement is fully done. Recipe / hidden
 * advancements (no display) are skipped, mirroring the NeoForge listener.
 */
@Mixin(PlayerAdvancements.class)
public abstract class PlayerAdvancementsMixin {

    @Shadow private ServerPlayer player;

    @Shadow public abstract AdvancementProgress getOrStartProgress(AdvancementHolder advancement);

    @Inject(method = "award", at = @At("RETURN"))
    private void awaydigest$onAward(AdvancementHolder advancement, String criterion,
                                    CallbackInfoReturnable<Boolean> cir) {
        // Only fire when this call resulted in newly completing the advancement.
        if (!cir.getReturnValueZ()) return;
        AdvancementProgress prog = getOrStartProgress(advancement);
        if (!prog.isDone()) return;

        var displayOpt = advancement.value().display();
        if (displayOpt.isEmpty()) return; // recipe / hidden — not digest-worthy.

        ServerPlayer sp = this.player;
        if (sp == null) return;

        AwayDigestListener.onAdvancement(sp, displayOpt.get().getTitle().getString());
    }
}
