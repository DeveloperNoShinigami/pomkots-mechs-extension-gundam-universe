# Custom Mech Model Guide

The extension relies on [GeckoLib](https://geckolib.com/) for all model and animation work.  This guide walks through creating a new mech, wiring every attack animation shipped with the mod, and tuning attributes with modifiers.

## 1. Resource setup

Store resources in the Forge module at `src/main/resources/assets/pomkotsmechsextension`:

| Path | Description |
|------|-------------|
| `geo/<model_name>.geo.json` | geometry exported from Blockbench |
| `animations/<model_name>.animation.json` | GeckoLib animation file |
| `textures/entity/<model_name>.png` | model texture |

Example `animations/my_mech.animation.json` skeleton containing all attack clips:

```json
{
  "animations": {
    "shoot": {}, "shootUpperBody": {},
    "shoot1": {}, "shoot2": {}, "shoot1_upper": {}, "shoot2_upper": {},
    "shootL1": {}, "shootL2": {}, "shootL1_upper": {}, "shootL2_upper": {},
    "saber": {}, "saberUpperBody": {},
    "saber1": {}, "saber2": {}, "saber3": {},
    "saber1_upper": {}, "saber2_upper": {}, "saber3_upper": {},
    "bazooka": {}, "bazooka_upper": {},
    "missile": {}, "missileUpperBody": {},
    "gatling1": {}, "gatling2": {},
    "vz": {}, "vz_upper": {}
  }
}
```

## 2. Model class

Create `src/main/java/<your package>/client/model/MyMechModel.java`:

```java
public class MyMechModel extends GeoModel<MyMechEntity> {
    private static final ResourceLocation GEO = new ResourceLocation(PomkotsMechsExtension.MODID, "geo/my_mech.geo.json");
    private static final ResourceLocation TEX = new ResourceLocation(PomkotsMechsExtension.MODID, "textures/entity/my_mech.png");
    private static final ResourceLocation ANIM = new ResourceLocation(PomkotsMechsExtension.MODID, "animations/my_mech.animation.json");

    @Override
    public ResourceLocation getModelResource(MyMechEntity animatable) { return GEO; }

    @Override
    public ResourceLocation getTextureResource(MyMechEntity animatable) { return TEX; }

    @Override
    public ResourceLocation getAnimationResource(MyMechEntity animatable) { return ANIM; }
}
```

## 3. Renderer

Place a renderer under `src/main/java/<your package>/client/renderer/MyMechRenderer.java`:

```java
public class MyMechRenderer extends GeoEntityRenderer<MyMechEntity> {
    public MyMechRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new MyMechModel());
        this.shadowRadius = 0.7F; // tweak shadow size if desired
    }
}
```

## 4. Entity registration

Entity classes live in `entity/vehicle`.  Register the type and its attributes:

```java
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(MODID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<MyMechEntity>> MY_MECH =
        ENTITIES.register("my_mech", () -> EntityType.Builder.of(MyMechEntity::new, MobCategory.CREATURE)
            .sized(4F, 10F).build("my_mech"));

    public static void init() {
        ENTITIES.register();
        EntityAttributeRegistry.register(MY_MECH::get, MyMechEntity::createMobAttributes);
    }
}
```

Client‑side renderer hook:

```java
EntityRendererRegistry.register(ModEntities.MY_MECH.get(), MyMechRenderer::new);
```

## 5. Weapon and item registration

Items usually sit in `src/main/java/<your package>/item` with resources under `assets/<modid>`:

```java
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MODID, Registries.ITEM);

    public static final RegistrySupplier<Item> BEAM_RIFLE = ITEMS.register("beam_rifle",
        () -> new BeamRifleItem(new Item.Properties().stacksTo(1)));

    public static void init() { ITEMS.register(); }
}
```

## 6. Combat actions and attack animations

Actions are identified by IDs and mapped to GeckoLib keys of the form `animation.<mech_name>.<key>`:

```java
protected static final int ACT_SHOOT = 7;
protected static final int ACT_GATLING = 10;
protected static final int ACT_BAZOOKA = 9;
protected static final int ACT_SABER = 8;

@Override
protected void registerCombatActions() {
    actionController.registerAction(ACT_SHOOT, new Action(20, 10, 10), ActionController.ActionType.R_ARM_MAIN);
    actionController.registerAction(ACT_SABER, new Action(60, 11, 9), ActionController.ActionType.L_ARM_MAIN);
    // add gatling, bazooka, etc.
}

controllers.add(new AnimationController<>(this, "attack", 2, event -> {
    if (actionController.getAction(ACT_SHOOT).isInAction()) {
        return event.setAndContinue(RawAnimation.begin().thenPlay("animation." + getMechName() + ".shoot"));
    }
    if (actionController.getAction(ACT_SABER).isInAction()) {
        return event.setAndContinue(RawAnimation.begin().thenPlay("animation." + getMechName() + ".saber1"));
    }
    return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation." + getMechName() + ".nop"));
}));
```

### Attack key reference

| Weapon | Animation keys |
|--------|----------------|
| Beam rifle | `shoot`, `shootUpperBody` |
| Dual beam rifles | `shoot1`, `shoot2`, `shoot1_upper`, `shoot2_upper`, `shootL1`, `shootL2`, `shootL1_upper`, `shootL2_upper` |
| Beam saber | `saber`, `saberUpperBody`, `saber1`, `saber2`, `saber3`, `saber1_upper`, `saber2_upper`, `saber3_upper` |
| Bazooka | `bazooka`, `bazooka_upper` |
| Missile | `missile`, `missileUpperBody` |
| Gatling gun | `gatling1` (spin up), `gatling2` (loop) |
| Vulcan guns | `vz`, `vz_upper` |

Define any key you use in `my_mech.animation.json` and return the matching name in `RawAnimation` calls.

## 7. Extra mech abilities

Base classes expose additional hooks such as `dash`, `dash_side`, `evasion_left`, `evasion_right`, `jump`, `cockpit_open`, and `cockpit_close`.  Actions can consume the energy gauge maintained by `PomkotsVehicleBase` to balance these moves.

## 8. Attributes and modifiers

Mech stats are attributes with base values and optional modifiers:

```java
public static AttributeSupplier.Builder createMobAttributes() {
    return createLivingAttributes()
        .add(Attributes.MAX_HEALTH, BattleBalance.BASE_HEALTH)
        .add(Attributes.ATTACK_KNOCKBACK)
        .add(Attributes.KNOCKBACK_RESISTANCE, 0.8);
}

// temporary speed buff
getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(
    new AttributeModifier("dash_boost", 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL));
```

The extension also registers weapon-specific attributes and an energy gauge so
their values can be inspected and changed through the `/attribute` command:

| Attribute | Purpose |
|-----------|---------|
| `pomkotsmechsextension:mech_beam_damage` | Base damage for beam projectiles |
| `pomkotsmechsextension:mech_machinegun_damage` | Damage dealt by gatling bullets |
| `pomkotsmechsextension:mech_missile_damage` | Damage for missile explosions |
| `pomkotsmechsextension:mech_saber_damage` | Melee damage for sabers and piles |
| `pomkotsmechsextension:mech_energy` | Maximum capacity of the internal energy gauge |

Like vanilla attributes these support the three modifier operations above, so
addons can apply flat bonuses or multiplicative boosts.

Modifier operations:

* **ADDITION** – flat bonus, e.g. `+5` health
* **MULTIPLY_BASE** – multiplies the starting value before other modifiers
* **MULTIPLY_TOTAL** – multiplies the result after all modifiers

## 9. Attribute reference

**Mod stats** – `MECH_HEALTH`, `MECH_PILE_DAMAGE`, `MECH_GATLING_DAMAGE`, `MECH_GRENADE_DAMAGE`, `MECH_GRENADE_EXPLOSION`, `MECH_MISSILE_DAMAGE`, `MECH_MISSILE_EXPLOSION`, and the weapon constants in `CombatBalance` for base damage and projectile speed.  `MECH_ENERGY` controls the size of the internal gauge consumed by boosts and weapons.

**Vanilla attributes** – `MAX_HEALTH`, `ATTACK_KNOCKBACK`, `KNOCKBACK_RESISTANCE` and any other `Attribute` supported by Minecraft can be attached to mechs for further tuning.

Other useful vanilla stats include `MOVEMENT_SPEED`, `FLYING_SPEED`, `ARMOR`,
`ARMOR_TOUGHNESS`, `ATTACK_DAMAGE`, `ATTACK_SPEED`, and `LUCK` which can be
combined with modifiers to further customize mech behavior.
