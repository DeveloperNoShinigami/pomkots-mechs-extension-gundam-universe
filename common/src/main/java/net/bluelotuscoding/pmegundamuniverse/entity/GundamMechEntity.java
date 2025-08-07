package net.bluelotuscoding.pmegundamuniverse.entity;

import grcmcs.minecraft.mods.pomkotsmechs.extension.entity.vehicle.PmgBaseEntity;
import grcmcs.minecraft.mods.pomkotsmechs.client.input.DriverInput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.core.animation.AnimatableManager;

/**
 * Base class for Gundam mechs added by the addon.
 */
public class GundamMechEntity extends PmgBaseEntity {
    private static final EntityDataAccessor<Integer> ENERGY =
            SynchedEntityData.defineId(GundamMechEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HAS_PILOT =
            SynchedEntityData.defineId(GundamMechEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> EQUIPMENT =
            SynchedEntityData.defineId(GundamMechEntity.class, EntityDataSerializers.STRING);

    public GundamMechEntity(EntityType<? extends PmgBaseEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ENERGY, 0);
        this.entityData.define(HAS_PILOT, false);
        this.entityData.define(EQUIPMENT, "");
    }

    public int getEnergy() {
        return this.entityData.get(ENERGY);
    }

    public void setEnergy(int energy) {
        this.entityData.set(ENERGY, energy);
    }

    public boolean hasPilot() {
        return this.entityData.get(HAS_PILOT);
    }

    public void setHasPilot(boolean value) {
        this.entityData.set(HAS_PILOT, value);
    }

    public String getEquipment() {
        return this.entityData.get(EQUIPMENT);
    }

    public void setEquipment(String equipment) {
        this.entityData.set(EQUIPMENT, equipment);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        setEnergy(tag.getInt("Energy"));
        setHasPilot(tag.getBoolean("HasPilot"));
        setEquipment(tag.getString("Equipment"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Energy", getEnergy());
        tag.putBoolean("HasPilot", hasPilot());
        tag.putString("Equipment", getEquipment());
    }

    /**
     * Register animation controllers for this mech.
     */
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        super.registerControllers(controllers);
    }

    /**
     * Attribute setup inherited from {@link PmgBaseEntity}.
     */
    public static AttributeSupplier.Builder createAttributes() {
        return AttributeSupplier.builder()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    protected void registerCombatActions() {
        // No combat actions yet.
    }

    @Override
    protected void applyPlayerInputWeaponsMainMode(DriverInput input) {
        // Mech weapons not implemented yet.
    }

    @Override
    protected String getMechName() {
        return "gundam_mech";
    }

    @Override
    protected boolean useEnergy(int amount) {
        if (getEnergy() >= amount) {
            setEnergy(getEnergy() - amount);
            return true;
        }
        return false;
    }

    @Override
    protected void setupProperties() {
        // Properties can be set here when needed.
    }
}
