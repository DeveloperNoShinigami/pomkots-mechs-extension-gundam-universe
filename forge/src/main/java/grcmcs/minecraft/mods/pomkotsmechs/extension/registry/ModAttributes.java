package grcmcs.minecraft.mods.pomkotsmechs.extension.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import grcmcs.minecraft.mods.pomkotsmechs.extension.PomkotsMechsExtension;
import grcmcs.minecraft.mods.pomkotsmechs.extension.config.CombatBalance;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(PomkotsMechsExtension.MODID, Registries.ATTRIBUTE);

    public static final RegistrySupplier<Attribute> MECH_BEAM_DAMAGE = ATTRIBUTES.register("mech_beam_damage",
            () -> new RangedAttribute("attribute.name." + PomkotsMechsExtension.MODID + ".mech_beam_damage",
                    CombatBalance.BASE_DAMAGE_BEAM, 0.0D, 2048.0D).setSyncable(true));

    public static final RegistrySupplier<Attribute> MECH_MISSILE_DAMAGE = ATTRIBUTES.register("mech_missile_damage",
            () -> new RangedAttribute("attribute.name." + PomkotsMechsExtension.MODID + ".mech_missile_damage",
                    CombatBalance.BASE_DAMAGE_MISSILE, 0.0D, 2048.0D).setSyncable(true));

    public static final RegistrySupplier<Attribute> MECH_MACHINEGUN_DAMAGE = ATTRIBUTES.register("mech_machinegun_damage",
            () -> new RangedAttribute("attribute.name." + PomkotsMechsExtension.MODID + ".mech_machinegun_damage",
                    CombatBalance.BASE_DAMAGE_MACHINEGUN, 0.0D, 2048.0D).setSyncable(true));

    public static final RegistrySupplier<Attribute> MECH_SABER_DAMAGE = ATTRIBUTES.register("mech_saber_damage",
            () -> new RangedAttribute("attribute.name." + PomkotsMechsExtension.MODID + ".mech_saber_damage",
                    CombatBalance.BASE_DAMAGE_SABER, 0.0D, 2048.0D).setSyncable(true));

    public static final RegistrySupplier<Attribute> MECH_ENERGY = ATTRIBUTES.register("mech_energy",
            () -> new RangedAttribute("attribute.name." + PomkotsMechsExtension.MODID + ".mech_energy",
                    CombatBalance.BASE_ENERGY, 0.0D, 2048.0D).setSyncable(true));

    public static void init() {
        ATTRIBUTES.register();
    }
}
