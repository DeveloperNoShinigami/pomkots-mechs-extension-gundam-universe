package net.bluelotuscoding.pmegundamuniverse.entity;

import grcmcs.minecraft.mods.pomkotsmechs.client.input.DriverInput;
import grcmcs.minecraft.mods.pomkotsmechs.extension.entity.vehicle.PmgBaseEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimatableManager;

/**
 * Base class for Gundam mechs added by the addon. The implementation focuses on
 * providing a few energy consuming abilities and basic synced state such as
 * pilot presence and equipment loadout.
 */
public class GundamMechEntity extends PmgBaseEntity {
    private static final EntityDataAccessor<Boolean> HAS_PILOT =
            SynchedEntityData.defineId(GundamMechEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> EQUIPMENT =
            SynchedEntityData.defineId(GundamMechEntity.class, EntityDataSerializers.STRING);

    // Energy costs for the various abilities.
    private static final float DASH_COST = 10.0F;
    private static final float DASH_SIDE_COST = 8.0F;
    private static final float JUMP_COST = 5.0F;
    private static final float COCKPIT_COST = 2.0F;

    private boolean cockpitOpen;
    /**
     * Simple local energy store. The real mod would sync this via attributes,
     * but for compilation purposes we keep a self managed gauge.
     */
    private int energy = 100;

    public GundamMechEntity(EntityType<? extends PmgBaseEntity> type, Level level) {
        super(type, level);
    }

    /**
     * Defines the default attribute set for this mech, combining vanilla
     * health/speed with custom mech attributes.
     */
    public static AttributeSupplier.Builder createAttributes() {
        return AttributeSupplier.builder()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(ModAttributes.MECH_BEAM_DAMAGE.get())
                .add(ModAttributes.MECH_MACHINEGUN_DAMAGE.get())
                .add(ModAttributes.MECH_MISSILE_DAMAGE.get())
                .add(ModAttributes.MECH_SABER_DAMAGE.get())
                .add(ModAttributes.MECH_ENERGY.get(), 100.0D)
                .add(ModAttributes.MECH_DASH_SPEED.get())
                .add(ModAttributes.MECH_DASH_SIDE_SPEED.get())
                .add(ModAttributes.MECH_EVASION_LEFT_SPEED.get())
                .add(ModAttributes.MECH_EVASION_RIGHT_SPEED.get())
                .add(ModAttributes.MECH_JUMP_POWER.get())
                .add(ModAttributes.MECH_JUMP_SUSTAIN.get())
                .add(ModAttributes.MECH_PILOT_ACCURACY.get())
                .add(ModAttributes.MECH_PILOT_REACTION.get());
    }

    /** Forward dash ability. */
    public void dash() {
        if (consumeEnergy(DASH_COST)) {
            Vec3 dir = getLookAngle().scale(1.5F);
            setDeltaMovement(getDeltaMovement().add(dir));
            level().playSound(null, blockPosition(), SoundEvents.ENDERMAN_TELEPORT,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    /**
     * Side dash ability.
     *
     * @param left true for left, false for right
     */
    public void dashSide(boolean left) {
        if (consumeEnergy(DASH_SIDE_COST)) {
            Vec3 side = getLookAngle().yRot(left ? (float) Math.PI / 2F : (float) -Math.PI / 2F)
                    .scale(1.2F);
            setDeltaMovement(getDeltaMovement().add(side));
            level().playSound(null, blockPosition(), SoundEvents.ENDERMAN_TELEPORT,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    public void evasionLeft() {
        dashSide(true);
    }

    public void evasionRight() {
        dashSide(false);
    }

    /** Jump ability consuming energy. */
    public void jump() {
        if (onGround() && consumeEnergy(JUMP_COST)) {
            setDeltaMovement(getDeltaMovement().add(0.0D, 1.0D, 0.0D));
            level().addParticle(ParticleTypes.CLOUD, getX(), getY(), getZ(),
                    0.0D, 0.0D, 0.0D);
        }
    }

    /** Opens the cockpit if closed. */
    public void cockpitOpen() {
        if (!cockpitOpen && consumeEnergy(COCKPIT_COST)) {
            cockpitOpen = true;
            level().playSound(null, blockPosition(), SoundEvents.IRON_TRAPDOOR_OPEN,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    /** Closes the cockpit if open. */
    public void cockpitClose() {
        if (cockpitOpen && consumeEnergy(COCKPIT_COST)) {
            cockpitOpen = false;
            level().playSound(null, blockPosition(), SoundEvents.IRON_TRAPDOOR_CLOSE,
                    SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(HAS_PILOT, false);
        this.entityData.define(EQUIPMENT, "");
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

    /**
     * Retrieves the current energy amount.
     */
    public int getEnergy() {
        return energy;
    }

    /**
     * Sets the current energy amount.
     */
    public void setEnergy(int energy) {
        this.energy = Math.max(0, energy);
    }

    private boolean consumeEnergy(float amount) {
        return useEnergy(Math.round(amount));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setHasPilot(tag.getBoolean("HasPilot"));
        setEquipment(tag.getString("Equipment"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("HasPilot", hasPilot());
        tag.putString("Equipment", getEquipment());
    }

    /** Register animation controllers for this mech. */
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        super.registerControllers(controllers);
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

