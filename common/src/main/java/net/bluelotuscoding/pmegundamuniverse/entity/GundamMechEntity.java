package net.bluelotuscoding.pmegundamuniverse.entity;

import grcmcs.minecraft.mods.pomkotsmechs.entity.vehicle.PomkotsVehicleBase;
import grcmcs.minecraft.mods.pomkotsmechs.entity.vehicle.equipment.action.Action;
import grcmcs.minecraft.mods.pomkotsmechs.entity.vehicle.equipment.action.ActionController;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

/**
 * Base class for Gundam mechs added by the addon. This now extends the
 * Pomkots vehicle base so actions and GeckoLib animations can be used.
 */
public class GundamMechEntity extends PomkotsVehicleBase {
    protected static final int ACT_SHOOT = 7;
    protected static final int ACT_SABER = 8;
    protected static final int ACT_BAZOOKA = 9;
    protected static final int ACT_GATLING = 10;

    public GundamMechEntity(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected String getMechName() {
        // All sample animations use the my_mech namespace
        return "my_mech";
    }

    @Override
    protected void registerActions() {
        super.registerActions();
        registerCombatActions();
    }

    /**
     * Registers weapon actions for this mech. Each action contains
     * a cooldown, charge duration and active fire window and is
     * mapped to the limb that performs it.
     */
    protected void registerCombatActions() {
        actionController.registerAction(ACT_SHOOT, new Action(20, 10, 10),
            ActionController.ActionType.R_ARM_MAIN);
        actionController.registerAction(ACT_SABER, new Action(60, 11, 9),
            ActionController.ActionType.L_ARM_MAIN);
        actionController.registerAction(ACT_BAZOOKA, new Action(60, 15, 15),
            ActionController.ActionType.R_SHL_MAIN);
        actionController.registerAction(ACT_GATLING, new Action(40, 10, 10),
            ActionController.ActionType.L_SHL_MAIN);
    }

    @Override
    protected void addExtraAnimationController(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "attack", 2, this::attackAnimation));
    }

    private <E extends GundamMechEntity> PlayState attackAnimation(AnimationState<E> event) {
        if (actionController.getAction(ACT_SHOOT).isInAction()) {
            return event.setAndContinue(RawAnimation.begin()
                .thenPlay("animation." + getMechName() + ".shoot"));
        }
        if (actionController.getAction(ACT_SABER).isInAction()) {
            return event.setAndContinue(RawAnimation.begin()
                .thenPlay("animation." + getMechName() + ".saber"));
        }
        if (actionController.getAction(ACT_BAZOOKA).isInAction()) {
            return event.setAndContinue(RawAnimation.begin()
                .thenPlay("animation." + getMechName() + ".bazooka"));
        }
        if (actionController.getAction(ACT_GATLING).isInAction()) {
            return event.setAndContinue(RawAnimation.begin()
                .thenPlay("animation." + getMechName() + ".gatling1"));
        }
        return PlayState.STOP;
    }
}
