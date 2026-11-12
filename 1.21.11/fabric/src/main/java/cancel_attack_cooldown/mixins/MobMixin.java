package cancel_attack_cooldown.mixins;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public class MobMixin {
    /**
     * 生物近战攻击同样取消攻击间隔：只清除被攻击目标的受伤无敌帧。
     * 环境伤害（岩浆/火焰/虚空）不受影响。
     */
    @Inject(method = "doHurtTarget", at = @At("RETURN"))
    private void clearTargetInvulnerability(ServerLevel level, Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (target != null) {
            target.invulnerableTime = 0;
        }
    }
}
