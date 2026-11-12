# Cancel Attack Cooldown

移除攻击间隔、攻击蓄力指示器与横扫攻击的 Minecraft mod。

原版为 **1.20.1 Forge**（见仓库根目录的 Forge 工程）。本分支在 `1.21.11/` 下新增 **Minecraft 1.21.11** 的移植，提供 **Fabric** 与 **NeoForge** 两个构建，原 1.20.1 Forge 工程保持不变。

- 原作：Xiaoyu_2009（GPL-3.0）
- 移植：Thatgfsj（[Thatgfsj-minecraft](https://github.com/Thatgfsj-minecraft)），沿用 GPL-3.0

## 功能

| Mixin | 注入点 | 效果 |
|---|---|---|
| `EntityMixin` | `Entity.tick` HEAD | 每 tick 清零 `invulnerableTime`，移除受击无敌帧 |
| `LivingEntityMixin` | `LivingEntity.tick` HEAD | 每 tick 将 `attackStrengthTicker` 锁在 20，攻击永不衰减 |
| `PlayerMixin` | `getAttackStrengthScale` HEAD | 恒返回 1.0，攻击伤害/击退不随冷却衰减 |
| `PlayerMixin` | `getCurrentItemAttackStrengthDelay` HEAD | 恒返回 0.0，隐藏攻击蓄力指示器 |
| `PlayerMixin` | `isSweepAttack` HEAD | 恒返回 false，移除横扫攻击（伤害+音效+粒子） |

## 1.20.1 → 1.21.11 移植说明

1. **加载器**：1.21.11 没有 MinecraftForge，Forge 侧改为其后继 **NeoForge 21.11.45**，并附带 Fabric 版。
2. **`sweepAttack()` 已不存在**：1.21.2 战斗系统重做后拆成 `isSweepAttack(...)`（判定，驱动音效/粒子）+ `doSweepAttack(...)`（AOE）。在 `isSweepAttack` 返回 false 即可同时移除横扫伤害与音效粒子，等价于旧版取消 `sweepAttack()`。
3. **冷却字段位置变动**：`invulnerableTime` 递减移到 `LivingEntity`，`attackStrengthTicker` 递增移到 `Player.tick`，原注入点（两者 `tick` HEAD）依然有效。
4. **mixin 配置声明**：NeoForge 21.11 不会自动扫描 `*.mixins.json`，需在 `neoforge.mods.toml` 用 `[[mixins]]` 声明；Fabric 走 `fabric.mod.json` 的 `mixins` 字段，Loom 1.17 直接把注解重映射为 intermediary，无需 refmap。

## 构建

```bash
cd 1.21.11/fabric   # 或 1.21.11/neoforge
./gradlew build     # 需要 JDK 21
# 产物 build/libs/*.jar
```

## 验证

- Fabric 生产 jar 在 1.21.11 专用服务器（loader 0.19.5）实机 E2E：14 连击全额 112 伤害（原版冷却约 30~55）、紧接第二击全额、横扫相邻实体零伤害。
- NeoForge dev 服务器启动至 `Done`，`defaultRequire=1` 下三个 Mixin 全部成功应用。

## License

GPL-3.0（与原仓库一致），原作 © Xiaoyu_2009。

## 1.21.11 说明

- `cancel_attack_cooldown` 只取消**攻击间隔**：玩家/生物攻击后清除被攻击目标的受伤无敌帧，让下一次攻击立即命中。
- 不再全局每 tick 清空 `invulnerableTime`，因此岩浆、火焰、虚空等持续环境伤害保持原版节奏。
