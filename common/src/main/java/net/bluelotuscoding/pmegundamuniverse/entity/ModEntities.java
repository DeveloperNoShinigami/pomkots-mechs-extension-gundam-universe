package net.bluelotuscoding.pmegundamuniverse.entity;

import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.bluelotuscoding.pmegundamuniverse.Pmegundamuniverse;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

/**
 * Holds entity type registrations for the addon.
 */
public final class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
        DeferredRegister.create(Pmegundamuniverse.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<MyMechEntity>> MY_MECH =
        ENTITIES.register("my_mech", () -> EntityType.Builder.of(MyMechEntity::new, MobCategory.CREATURE)
            .sized(4F, 10F).build("my_mech"));

    private ModEntities() {
    }

    /**
     * Registers entity types and attributes.
     */
    public static void init() {
        ENTITIES.register();
        EntityAttributeRegistry.register(MY_MECH::get, MyMechEntity::createAttributes);
    }
}
