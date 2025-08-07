package net.bluelotuscoding.pmegundamuniverse.entity;

import java.util.Collections;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Base class for Gundam mechs added by the addon.
 * TODO: extend the core mod's PmgBaseEntity when available.
 */
public class GundamMechEntity extends LivingEntity {
    private double energy;

    protected GundamMechEntity(EntityType<? extends LivingEntity> type, Level level) {
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
}
