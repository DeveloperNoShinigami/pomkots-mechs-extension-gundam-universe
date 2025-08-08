package net.bluelotuscoding.pmegundamuniverse.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

/**
 * Simplified vehicle base providing an energy gauge.
 * This placeholder is used until the core Pomkots Mechs API is available.
 */
public class PomkotsVehicleBase extends Entity {
    private float energy = 100.0F;
    private final float maxEnergy = 100.0F;

    public PomkotsVehicleBase(EntityType<? extends Entity> type, Level level) {
        super(type, level);
    }

    /**
     * Attempts to consume energy from the gauge.
     *
     * @param amount energy to consume
     * @return {@code true} if enough energy was available
     */
    public boolean consumeEnergy(float amount) {
        if (energy >= amount) {
            energy -= amount;
            return true;
        }
        return false;
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = Math.min(energy, maxEnergy);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        energy = tag.getFloat("Energy");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("Energy", energy);
    }
}
