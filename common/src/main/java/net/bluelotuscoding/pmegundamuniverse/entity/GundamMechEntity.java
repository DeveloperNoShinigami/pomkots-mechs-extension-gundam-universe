package net.bluelotuscoding.pmegundamuniverse.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.ItemStack;
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
        // TODO: sync Gundam data
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        this.energy = tag.getDouble("Energy");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putDouble("Energy", this.energy);
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public boolean isSensitiveToWater() {
        return false;
    }

    /**
     * Retrieves the pilot's accuracy rating.
     * <p>
     * Currently returns a placeholder value until pilot stats are implemented.
     *
     * @return pilot accuracy from 0-1
     */
    protected float getPilotAccuracy() {
        return 1.0f; // TODO: calculate actual pilot accuracy
    }

    /**
     * Dash skid animation scaled by pilot accuracy.
     */
    protected void playDashSkidAnimation() {
        float intensity = getPilotAccuracy();
        // TODO: trigger dash skid animation with intensity
    }

    /**
     * Hover lean animation scaled by pilot accuracy.
     */
    protected void playHoverLeanAnimation() {
        float intensity = getPilotAccuracy();
        // TODO: trigger hover lean animation with intensity
    }

    /**
     * Cockpit reaction animation scaled by pilot accuracy.
     */
    protected void playCockpitReactionAnimation() {
        float intensity = getPilotAccuracy();
        // TODO: trigger cockpit reaction animation with intensity
    }

    /**
     * Prototype overheat particle or sound effects.
     */
    protected void spawnOverheatEffects() {
        float intensity = getPilotAccuracy();
        // TODO: spawn overheat effects based on intensity
    }

    /**
     * Prototype vertical thrust effects scaled by mech jump sustain.
     */
    protected void spawnVerticalThrustEffects() {
        float sustain = getMechJumpSustain();
        // TODO: spawn vertical thrust effects based on sustain
    }

    protected float getMechJumpSustain() {
        return mechJumpSustain; // TODO: read from mech attributes
    }
}
