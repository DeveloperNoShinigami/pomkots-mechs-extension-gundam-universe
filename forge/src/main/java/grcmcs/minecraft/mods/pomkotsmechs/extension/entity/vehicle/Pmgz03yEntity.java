package grcmcs.minecraft.mods.pomkotsmechs.extension.entity.vehicle;

import grcmcs.minecraft.mods.pomkotsmechs.extension.config.CombatBalance;
import grcmcs.minecraft.mods.pomkotsmechs.extension.registry.ModAttributes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class Pmgz03yEntity extends Pmgz03Entity {
    @Override
    protected String getMechName() {
        return "base";
    }    public static AttributeSupplier.Builder createMobAttributes() {
        return createLivingAttributes()
                .add(Attributes.ATTACK_KNOCKBACK)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
                .add(Attributes.MAX_HEALTH, CombatBalance.BASE_HEALTH * 0.75)
                .add(ModAttributes.MECH_BEAM_DAMAGE.get(), CombatBalance.BASE_DAMAGE_BEAM)
                .add(ModAttributes.MECH_MACHINEGUN_DAMAGE.get(), CombatBalance.BASE_DAMAGE_MACHINEGUN)
                .add(ModAttributes.MECH_MISSILE_DAMAGE.get(), CombatBalance.BASE_DAMAGE_MISSILE)
                .add(ModAttributes.MECH_SABER_DAMAGE.get(), CombatBalance.BASE_DAMAGE_SABER)
                .add(ModAttributes.MECH_ENERGY.get(), CombatBalance.BASE_ENERGY)
                .add(ModAttributes.MECH_DASH_SPEED.get(), 2.0F)
                .add(ModAttributes.MECH_DASH_SIDE_SPEED.get(), 2.0F)
                .add(ModAttributes.MECH_EVASION_LEFT_SPEED.get(), 10F)
                .add(ModAttributes.MECH_EVASION_RIGHT_SPEED.get(), 10F)
                .add(ModAttributes.MECH_JUMP_SUSTAIN.get(), 2F / 3.5F)
                .add(ModAttributes.MECH_JUMP_POWER.get(), 3.5F)
                .add(ModAttributes.MECH_PILOT_ACCURACY.get(), CombatBalance.BASE_PILOT_ACCURACY)
                .add(ModAttributes.MECH_PILOT_REACTION.get(), CombatBalance.BASE_PILOT_REACTION);
    }

    public Pmgz03yEntity(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

}
