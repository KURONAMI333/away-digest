package com.kuronami.awaydigest.mixin;

import com.kuronami.awaydigest.AwayDigestListener;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Fabric 1.20.1: hook into {@link PlayerAdvancements#award} to detect when an
 * advancement is newly granted (Fabric has no first-class advancement event).
 *
 * <p>1.20.1 {@code award} takes a raw {@link Advancement} whose
 * {@code getDisplay()} is a nullable {@link DisplayInfo} (no holder wrapper,
 * no {@code Optional}). Recipe / hidden advancements (no display) are skipped,
 * mirroring the NeoForge listener.
 */
@Mixin(PlayerAdvancements.class)
public abstract class PlayerAdvancementsMixin {

    @Shadow private ServerPlayer player;

    @Shadow public abstract AdvancementProgress getOrStartProgress(Advancement advancement);

    @Inject(method = "award", at = @At("RETURN"))
    private void awaydigest$onAward(Advancement advancement, String criterion,
                                    CallbackInfoReturnable<Boolean> cir) {
        // Only fire when this call resulted in newly completing the advancement.
        if (!cir.getReturnValueZ()) return;
        AdvancementProgress prog = getOrStartProgress(advancement);
        if (!prog.isDone()) return;

        DisplayInfo display = advancement.getDisplay();
        if (display == null) return; // recipe / hidden — not digest-worthy.

        ServerPlayer sp = this.player;
        if (sp == null) return;

        AwayDigestListener.onAdvancement(sp, display.getTitle().getString());
    }
}
