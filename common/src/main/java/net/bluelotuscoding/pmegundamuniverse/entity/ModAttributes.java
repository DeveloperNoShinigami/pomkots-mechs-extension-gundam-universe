package net.bluelotuscoding.pmegundamuniverse.entity;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.bluelotuscoding.pmegundamuniverse.Pmegundamuniverse;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import java.util.UUID;

/**
 * Holds custom Gundam mech attributes and helper utilities for applying
 * transient modifiers such as temporary dash boosts.
 */
public final class ModAttributes {
    private ModAttributes() {}

    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(Pmegundamuniverse.MOD_ID, Registries.ATTRIBUTE);

    public static final RegistrySupplier<Attribute> MECH_BEAM_DAMAGE = ATTRIBUTES.register(
            "mech_beam_damage",
            () -> new RangedAttribute("attribute.name.pmegundamuniverse.mech_beam_damage", 0.0D, 0.0D, 1024.0D).setSyncable(true));

    public static final RegistrySupplier<Attribute> MECH_MACHINEGUN_DAMAGE = ATTRIBUTES.register(
            "mech_machinegun_damage",
            () -> new RangedAttribute("attribute.name.pmegundamuniverse.mech_machinegun_damage", 0.0D, 0.0D, 1024.0D).setSyncable(true));

    public static final RegistrySupplier<Attribute> MECH_MISSILE_DAMAGE = ATTRIBUTES.register(
            "mech_missile_damage",
            () -> new RangedAttribute("attribute.name.pmegundamuniverse.mech_missile_damage", 0.0D, 0.0D, 1024.0D).setSyncable(true));

    public static final RegistrySupplier<Attribute> MECH_SABER_DAMAGE = ATTRIBUTES.register(
            "mech_saber_damage",
            () -> new RangedAttribute("attribute.name.pmegundamuniverse.mech_saber_damage", 0.0D, 0.0D, 1024.0D).setSyncable(true));

    public static final RegistrySupplier<Attribute> MECH_ENERGY = ATTRIBUTES.register(
            "mech_energy",
            () -> new RangedAttribute("attribute.name.pmegundamuniverse.mech_energy", 100.0D, 0.0D, 10000.0D).setSyncable(true));

    public static final RegistrySupplier<Attribute> MECH_DASH_SPEED = ATTRIBUTES.register(
            "mech_dash_speed",
            () -> new RangedAttribute("attribute.name.pmegundamuniverse.mech_dash_speed", 0.0D, 0.0D, 1024.0D).setSyncable(true));

    public static final RegistrySupplier<Attribute> MECH_DASH_SIDE_SPEED = ATTRIBUTES.register(
            "mech_dash_side_speed",
            () -> new RangedAttribute("attribute.name.pmegundamuniverse.mech_dash_side_speed", 0.0D, 0.0D, 1024.0D).setSyncable(true));

    public static final RegistrySupplier<Attribute> MECH_EVASION_LEFT_SPEED = ATTRIBUTES.register(
            "mech_evasion_left_speed",
            () -> new RangedAttribute("attribute.name.pmegundamuniverse.mech_evasion_left_speed", 0.0D, 0.0D, 1024.0D).setSyncable(true));

    public static final RegistrySupplier<Attribute> MECH_EVASION_RIGHT_SPEED = ATTRIBUTES.register(
            "mech_evasion_right_speed",
            () -> new RangedAttribute("attribute.name.pmegundamuniverse.mech_evasion_right_speed", 0.0D, 0.0D, 1024.0D).setSyncable(true));

    public static final RegistrySupplier<Attribute> MECH_JUMP_POWER = ATTRIBUTES.register(
            "mech_jump_power",
            () -> new RangedAttribute("attribute.name.pmegundamuniverse.mech_jump_power", 0.0D, 0.0D, 1024.0D).setSyncable(true));

    public static final RegistrySupplier<Attribute> MECH_JUMP_SUSTAIN = ATTRIBUTES.register(
            "mech_jump_sustain",
            () -> new RangedAttribute("attribute.name.pmegundamuniverse.mech_jump_sustain", 0.0D, 0.0D, 1024.0D).setSyncable(true));

    public static final RegistrySupplier<Attribute> MECH_PILOT_ACCURACY = ATTRIBUTES.register(
            "mech_pilot_accuracy",
            () -> new RangedAttribute("attribute.name.pmegundamuniverse.mech_pilot_accuracy", 1.0D, 0.0D, 10.0D).setSyncable(true));

    public static final RegistrySupplier<Attribute> MECH_PILOT_REACTION = ATTRIBUTES.register(
            "mech_pilot_reaction",
            () -> new RangedAttribute("attribute.name.pmegundamuniverse.mech_pilot_reaction", 1.0D, 0.0D, 10.0D).setSyncable(true));

    public static void init() {
        ATTRIBUTES.register();
    }

    /**
     * Applies a transient attribute modifier to the given entity. Callers are expected to
     * schedule removal when the boost expires.
     */
    public static void applyTransientModifier(LivingEntity entity, Attribute attribute, UUID id, String name, double amount, AttributeModifier.Operation operation) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            AttributeModifier modifier = new AttributeModifier(id, name, amount, operation);
            instance.addTransientModifier(modifier);
        }
    }

    /**
     * Removes a transient attribute modifier that was previously added.
     */
    public static void removeModifier(LivingEntity entity, Attribute attribute, UUID id) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance != null) {
            instance.removeModifier(id);
        }
    }
}
