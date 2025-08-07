package net.bluelotuscoding.pmegundamuniverse.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * Base class for Gundam mechs added by the addon.
 * TODO: extend the core mod's PmgBaseEntity when available.
 */
public class GundamMechEntity extends Entity implements GeoAnimatable {
    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);

    public GundamMechEntity(EntityType<? extends Entity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        // TODO: sync Gundam data
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        // TODO: read custom data
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        // TODO: write custom data
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // TODO: add animation controllers
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animationCache;
    }

    @Override
    public double getTick(Object animatable) {
        return tickCount;
    }
}
