package net.bluelotuscoding.pmegundamuniverse.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Base class for Gundam mechs added by the addon.
 * TODO: extend the core mod's PmgBaseEntity when available.
 */
public class GundamMechEntity extends PomkotsVehicleBase {
    private static final float DASH_COST = 10.0F;
    private static final float DASH_SIDE_COST = 8.0F;
    private static final float JUMP_COST = 12.0F;
    private static final float COCKPIT_COST = 2.0F;

    private boolean cockpitOpen;

    public GundamMechEntity(EntityType<? extends PomkotsVehicleBase> type, Level level) {
        super(type, level);
    }

    /**
     * Forward dash ability.
     */
    public void dash() {
        if (consumeEnergy(DASH_COST)) {
            Vec3 dir = getLookAngle().scale(1.5F);
            setDeltaMovement(getDeltaMovement().add(dir));
            level().playSound(null, blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    /**
     * Side dash ability.
     *
     * @param left true for left, false for right
     */
    public void dashSide(boolean left) {
        if (consumeEnergy(DASH_SIDE_COST)) {
            Vec3 side = getLookAngle().yRot(left ? (float) Math.PI / 2F : (float) -Math.PI / 2F).scale(1.2F);
            setDeltaMovement(getDeltaMovement().add(side));
            level().playSound(null, blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    public void evasionLeft() {
        dashSide(true);
    }

    public void evasionRight() {
        dashSide(false);
    }

    /**
     * Jump ability consuming energy.
     */
    public void jump() {
        if (onGround() && consumeEnergy(JUMP_COST)) {
            setDeltaMovement(getDeltaMovement().add(0.0D, 1.0D, 0.0D));
            level().addParticle(ParticleTypes.CLOUD, getX(), getY(), getZ(), 0.0D, 0.0D, 0.0D);
        }
    }

    /**
     * Opens the cockpit if closed.
     */
    public void cockpitOpen() {
        if (!cockpitOpen && consumeEnergy(COCKPIT_COST)) {
            cockpitOpen = true;
            level().playSound(null, blockPosition(), SoundEvents.IRON_TRAPDOOR_OPEN, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    /**
     * Closes the cockpit if open.
     */
    public void cockpitClose() {
        if (cockpitOpen && consumeEnergy(COCKPIT_COST)) {
            cockpitOpen = false;
            level().playSound(null, blockPosition(), SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
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
}
