package cancel_attack_cooldown.mixins;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
    /**
     * 1.21.2+ 将原 sweepAttack() 拆成了 isSweepAttack（判定，含音效/粒子分支）+ doSweepAttack（AOE）。
     * 在判定处返回 false 可同时去掉横扫伤害与横扫音效/粒子，等价于 1.20.1 中取消 sweepAttack()。
     */
    @Inject(method = "isSweepAttack", at = @At("HEAD"), cancellable = true)
    private void sweepAttack(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "getAttackStrengthScale", at = @At("HEAD"), cancellable = true)
    private void getAttackStrengthScale(float partialTicks, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(1.0F);
    }

    @Inject(method = "getCurrentItemAttackStrengthDelay", at = @At("HEAD"), cancellable = true)
    private void hideAttackIndicator(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(0.0F);
    }

    /**
     * 取消“攻击间隔”：只清除被攻击目标的受伤无敌帧，让下一次攻击能立刻命中。
     * <p>
     * 不要像旧版那样在 Entity.tick 里每 tick 清空 invulnerableTime —— 那会让岩浆、虚空等
     * 持续环境伤害也按每 tick 结算，导致秒杀。
     */
    @Inject(method = "attack", at = @At("RETURN"))
    private void clearTargetInvulnerability(Entity target, CallbackInfo ci) {
        if (target != null) {
            target.invulnerableTime = 0;
        }
    }
}
