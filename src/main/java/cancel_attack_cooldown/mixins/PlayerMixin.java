package cancel_attack_cooldown.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerMixin {
    @Inject(method = "attack", at = @At(value = "INVOKE", 
    target = "Lnet/minecraft/entity/player/PlayerEntity;spawnSweepAttackParticles()V"), 
    cancellable = true)
    private void cancelSweepAttack(Entity target, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "getAttackCooldownProgress", at = @At("HEAD"), cancellable = true)
    private void getAttackStrengthScale(float baseTime, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(1.0F);
    }

    @Inject(method = "getAttackCooldownProgressPerTick", at = @At("HEAD"), cancellable = true)
    private void hideAttackIndicator(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(0.0F);
    }
}