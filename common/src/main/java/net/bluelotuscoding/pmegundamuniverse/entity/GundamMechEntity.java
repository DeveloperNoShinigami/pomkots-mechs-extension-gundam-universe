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
        AttributeInstance max = this.getAttribute(ModAttributes.MECH_ENERGY.get());
        this.energy = max != null ? max.getValue() : 0.0D;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AttributeSupplier.builder()
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

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        double max = getAttributeValue(ModAttributes.MECH_ENERGY.get());
        this.energy = Math.min(energy, max);
    }

    public void addEnergy(double delta) {
        setEnergy(this.energy + delta);
    }

    @Override
    public void tick() {
        super.tick();
        double max = getAttributeValue(ModAttributes.MECH_ENERGY.get());
        if (this.energy > max) {
            this.energy = max;
        }
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
