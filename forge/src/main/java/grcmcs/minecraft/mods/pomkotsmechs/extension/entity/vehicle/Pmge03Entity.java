package grcmcs.minecraft.mods.pomkotsmechs.extension.entity.vehicle;

import grcmcs.minecraft.mods.pomkotsmechs.client.input.DriverInput;
import grcmcs.minecraft.mods.pomkotsmechs.entity.vehicle.equipment.action.Action;
import grcmcs.minecraft.mods.pomkotsmechs.entity.vehicle.equipment.action.ActionController;
import grcmcs.minecraft.mods.pomkotsmechs.extension.PomkotsMechsExtension;
import grcmcs.minecraft.mods.pomkotsmechs.extension.config.CombatBalance;
import grcmcs.minecraft.mods.pomkotsmechs.extension.entity.projectile.BeamEntity;
import grcmcs.minecraft.mods.pomkotsmechs.extension.entity.projectile.BeamLargeEntity;
import grcmcs.minecraft.mods.pomkotsmechs.extension.entity.projectile.MissileHorizontalEntity;
import grcmcs.minecraft.mods.pomkotsmechs.extension.registry.ModAttributes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class Pmge03Entity extends PmgBaseEntity {
    public static AttributeSupplier.Builder createMobAttributes() {
        return createLivingAttributes()
                .add(Attributes.ATTACK_KNOCKBACK)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
                .add(Attributes.MAX_HEALTH, CombatBalance.BASE_HEALTH * 0.75)
                .add(ModAttributes.MECH_BEAM_DAMAGE.get(), CombatBalance.BASE_DAMAGE_BEAM)
                .add(ModAttributes.MECH_MACHINEGUN_DAMAGE.get(), CombatBalance.BASE_DAMAGE_MACHINEGUN)
                .add(ModAttributes.MECH_MISSILE_DAMAGE.get(), CombatBalance.BASE_DAMAGE_MISSILE)
                .add(ModAttributes.MECH_SABER_DAMAGE.get(), CombatBalance.BASE_DAMAGE_SABER)
                .add(ModAttributes.MECH_ENERGY.get(), CombatBalance.BASE_ENERGY)
                .add(ModAttributes.MECH_DASH_SPEED.get(), 1.7F)
                .add(ModAttributes.MECH_DASH_SIDE_SPEED.get(), 1.7F)
                .add(ModAttributes.MECH_EVASION_LEFT_SPEED.get(), 10F)
                .add(ModAttributes.MECH_EVASION_RIGHT_SPEED.get(), 10F)
                .add(ModAttributes.MECH_JUMP_SUSTAIN.get(), 2F / 3.5F)
                .add(ModAttributes.MECH_JUMP_POWER.get(), 3.5F)
                .add(ModAttributes.MECH_PILOT_ACCURACY.get(), CombatBalance.BASE_PILOT_ACCURACY)
                .add(ModAttributes.MECH_PILOT_REACTION.get(), CombatBalance.BASE_PILOT_REACTION);
    }

    public Pmge03Entity(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    protected static final int ACT_SHOOT = 7;
    protected static final int ACT_SABER = 8;
    protected static final int ACT_BAZOOKA = 9;
    protected static final int ACT_MISSILE = 10;

    @Override
    protected void registerCombatActions() {
        this.actionController.registerAction(ACT_SHOOT, new Action(30, 9, 11), ActionController.ActionType.R_ARM_MAIN);
        this.actionController.registerAction(ACT_SABER, new Action(40, 11, 9), ActionController.ActionType.L_ARM_MAIN);
        this.actionController.registerAction(ACT_BAZOOKA, new Action(60, 11, 11), ActionController.ActionType.R_SHL_MAIN);
        this.actionController.registerAction(ACT_MISSILE, new Action(120, 5, 10), ActionController.ActionType.L_SHL_MAIN);
    }

    @Override
    protected void applyPlayerInputWeaponsMainMode(DriverInput driverInput) {
        if (driverInput.isWeaponRightHandPressed()) {
            this.actionController.getAction(ACT_SHOOT).startAction();
        } else if (driverInput.isWeaponLeftHandPressed()) {
            this.actionController.getAction(ACT_SABER).startAction();
        } else if (driverInput.isWeaponRightShoulderPressed()) {
            this.actionController.getAction(ACT_BAZOOKA).startAction();
        } else if (driverInput.isWeaponLeftShoulderPressed()) {
            this.actionController.getAction(ACT_MISSILE).startAction();
        }
    }

    @Override
    protected void fireWeapons() {
        Level level = level();

        if (this.isMainMode()) {
            if (actionController.getAction(ACT_SHOOT).isOnFire()) {
                this.fireShoot(level);
            } else if (actionController.getAction(ACT_SABER).isOnFire()) {
                this.fireSaber(level);
            } else if (actionController.getAction(ACT_BAZOOKA).isOnFire()) {
                this.fireBazooka(level);
            } else if (actionController.getAction(ACT_MISSILE).isOnFire()) {
                this.fireMissile(level);
            }
        }
    }

    private void fireShoot(Level level) {
        if (!level.isClientSide()) {
            for (int i = 0; i < 2; i++) {
                BeamEntity be = new BeamEntity(PomkotsMechsExtension.BEAM.get(), level, this, (int)getBeamDamage());

                // 原因不明なんだけど、getPosした時の座標と、レンダリングされてる座標で3tick分ぐらい乖離がある気配がする
                // ので、3tick前の座標をオフセットにする
                // なんかaddVelocity周りが悪さしてる…？
                var offset = posHistory.getFirst();

                // オフセット位置から大体の銃口の座標を決める（モデル位置からとるとクラサバ同期がめんどい…）
                var muzzlPos = new Vec3(-5, 13F + i * 2, 6F);
                muzzlPos = muzzlPos.yRot((float) Math.toRadians((-1.0) * this.getYRot()));

                be.setPos(offset.add(muzzlPos));

                float[] angle = getShootingAngle(be, false);
                be.shootFromRotation(be, angle[0], angle[1], this.getFallFlyingTicks(), CombatBalance.BASE_SPEED_BEAM, 0F);

                level.addFreshEntity(be);
            }
        }
    }

    private void fireBazooka(Level level) {
        if (!level.isClientSide()) {
            for (int i = 0; i < 3; i+=2) {
                // innate modifier: large beam doubles the beam attribute
                BeamLargeEntity be = new BeamLargeEntity(PomkotsMechsExtension.BEAMLARGE.get(), level, this, (int)(getBeamDamage() * CombatBalance.LARGE_BEAM_DAMAGE_MULTIPLIER));

                // 原因不明なんだけど、getPosした時の座標と、レンダリングされてる座標で3tick分ぐらい乖離がある気配がする
                // ので、3tick前の座標をオフセットにする
                // なんかaddVelocity周りが悪さしてる…？
                var offset = posHistory.getFirst();

                // オフセット位置から大体の銃口の座標を決める（モデル位置からとるとクラサバ同期がめんどい…）
                var muzzlPos = new Vec3((i-1) * 3, 17.5F, 14F);
                muzzlPos = muzzlPos.yRot((float) Math.toRadians((-1.0) * this.getYRot()));

                be.setPos(offset.add(muzzlPos));

                float[] angle = getShootingAngle(be, false);
                be.shootFromRotation(be, angle[0], angle[1], this.getFallFlyingTicks(), CombatBalance.BASE_SPEED_BEAM_LARGE, 0F);

                level.addFreshEntity(be);

            }

            var knockBackvel = new Vec3(0, 0, -10F).yRot((float) Math.toRadians((-1.0) * this.getYRot()));
            this.addDeltaMovement(knockBackvel);
        }
    }

    private void fireMissile(Level level) {
        if (!level.isClientSide()) {
            // 原因不明なんだけど、getPosした時の座標と、レンダリングされてる座標で3tick分ぐらい乖離がある気配がする
            // ので、3tick前の座標をオフセットにする
            // なんかaddVelocity周りが悪さしてる…？
            var offset = posHistory.getFirst();

            // オフセット位置から大体の銃口の座標を決める（モデル位置からとるとクラサバ同期がめんどい…）
            var muzzlPos = new Vec3(0, 19F, 0F);

            for (int i = 0; i < 6; i++) {
                var worldMuzzlPos = muzzlPos.add(4.5 * (i / 3 - 0.5),1 * (i % 3),0).yRot((float) Math.toRadians((-1.0) * this.getYRot()));

                MissileHorizontalEntity be = new MissileHorizontalEntity(PomkotsMechsExtension.MISSILE.get(), level, this, null, getMissileDamage());

                be.setPos(offset.add(worldMuzzlPos));

                be.shootFromRotation(be, -20, this.getYRot(), this.getFallFlyingTicks(), CombatBalance.BASE_SPEED_MISSILE, 0F);

                level.addFreshEntity(be);
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        super.registerControllers(controllers);

        controllers.add(new AnimationController<>(this, "attack", 2, event -> {
            if (this.actionController.getAction(ACT_SHOOT).isInAction()) {
                if (this.actionController.getAction(ACT_SHOOT).isOnStart()) {
                    event.getController().forceAnimationReset();
                    if (event.isMoving()) {
                        return event.setAndContinue(RawAnimation.begin().thenPlay("animation." + getMechName() + ".shootUpperBody"));
                    } else {
                        return event.setAndContinue(RawAnimation.begin().thenPlay("animation." + getMechName() + ".shoot"));
                    }
                }
                return PlayState.CONTINUE;
            } else if (this.actionController.getAction(ACT_SABER).isInAction()) {
                if (this.actionController.getAction(ACT_SABER).isOnStart()) {
                    event.getController().forceAnimationReset();

                    if (event.isMoving()) {
                        return event.setAndContinue(RawAnimation.begin().thenPlay("animation." + getMechName() + ".saber1_upper"));
                    } else {
                        return event.setAndContinue(RawAnimation.begin().thenPlay("animation." + getMechName() + ".saber1"));
                    }
                }
                return PlayState.CONTINUE;
            } else if (this.actionController.getAction(ACT_BAZOOKA).isInAction()) {
                if (this.actionController.getAction(ACT_BAZOOKA).isOnStart()) {
                    event.getController().forceAnimationReset();
                    if (event.isMoving()) {
                        return event.setAndContinue(RawAnimation.begin().thenPlay("animation." + getMechName() + ".vz_upper"));
                    } else {
                        return event.setAndContinue(RawAnimation.begin().thenPlay("animation." + getMechName() + ".vz"));
                    }
                }
                return PlayState.CONTINUE;
            } else if (this.actionController.getAction(ACT_MISSILE).isInAction()) {
                if (this.actionController.getAction(ACT_MISSILE).isOnStart()) {
                    event.getController().forceAnimationReset();
                    if (event.isMoving()) {
                        return event.setAndContinue(RawAnimation.begin().thenPlay("animation." + getMechName() + ".missileUpperBody"));
                    } else {
                        return event.setAndContinue(RawAnimation.begin().thenPlay("animation." + getMechName() + ".missile"));
                    }
                }
                return PlayState.CONTINUE;
            } else if (event.isMoving() && this.actionController.isBoost() && inAirTicks < 10) {
                return event.setAndContinue(RawAnimation.begin().thenPlay("animation." + getMechName() + ".runUpper"));
            }

            if (event.isMoving()) {
                return PlayState.CONTINUE;
            } else {
                return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation." + getMechName() + ".idle"));
            }
        }).setSoundKeyframeHandler(state -> {
            registerAnimationSoundHandlers(state);
        }));
    }

}
