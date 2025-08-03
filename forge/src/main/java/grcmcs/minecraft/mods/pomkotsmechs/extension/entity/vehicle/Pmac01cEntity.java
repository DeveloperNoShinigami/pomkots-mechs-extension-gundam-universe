package grcmcs.minecraft.mods.pomkotsmechs.extension.entity.vehicle;

import grcmcs.minecraft.mods.pomkotsmechs.PomkotsMechs;
import grcmcs.minecraft.mods.pomkotsmechs.client.input.DriverInput;
import grcmcs.minecraft.mods.pomkotsmechs.entity.vehicle.equipment.action.Action;
import grcmcs.minecraft.mods.pomkotsmechs.entity.vehicle.equipment.action.ActionController;
import grcmcs.minecraft.mods.pomkotsmechs.extension.PomkotsMechsExtension;
import grcmcs.minecraft.mods.pomkotsmechs.extension.config.CombatBalance;
import grcmcs.minecraft.mods.pomkotsmechs.extension.entity.projectile.BulletLargeEntity;
import grcmcs.minecraft.mods.pomkotsmechs.extension.entity.projectile.MachineGunBulletEntity;
import grcmcs.minecraft.mods.pomkotsmechs.extension.entity.projectile.MissileHorizontalEntity;
import grcmcs.minecraft.mods.pomkotsmechs.extension.registry.ModAttributes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class Pmac01cEntity extends Pmac01Entity {
    public static final float DEFAULT_SCALE = 0.5f;

    public static AttributeSupplier.Builder createMobAttributes() {
        return createLivingAttributes()
                .add(Attributes.ATTACK_KNOCKBACK)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
                .add(Attributes.MAX_HEALTH, CombatBalance.BASE_HEALTH * 0.3)
                .add(ModAttributes.MECH_BEAM_DAMAGE.get(), CombatBalance.BASE_DAMAGE_BEAM)
                .add(ModAttributes.MECH_MACHINEGUN_DAMAGE.get(), CombatBalance.BASE_DAMAGE_MACHINEGUN)
                .add(ModAttributes.MECH_MISSILE_DAMAGE.get(), CombatBalance.BASE_DAMAGE_MISSILE)
                .add(ModAttributes.MECH_SABER_DAMAGE.get(), CombatBalance.BASE_DAMAGE_SABER)
                .add(ModAttributes.MECH_ENERGY.get(), CombatBalance.BASE_ENERGY);
    }

    public Pmac01cEntity(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

}
