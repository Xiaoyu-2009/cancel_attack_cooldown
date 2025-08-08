package cancel_attack_cooldown.mixins;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "sweepAttack", at = @At("HEAD"), cancellable = true)
    private void sweepAttack(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "getAttackStrengthScale", at = @At("HEAD"), cancellable = true)
    private void getAttackStrengthScale(float partialTicks, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(1.0F);
    }
    
    @Inject(method = "getCurrentItemAttackStrengthDelay", at = @At("HEAD"), cancellable = true)
    private void hideAttackIndicator(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(0.0F);
    }
}