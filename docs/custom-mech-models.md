# Custom Mech Model Guide

The extension uses [GeckoLib](https://geckolib.com/) to animate mechs. New models can be added without modifying the base mod.

## 1. Create model files

Place your resources under `src/main/resources/assets/pomkotsmechsextension` in the Forge module.

- `geo/<model_name>.geo.json` – geometry exported from Blockbench.
- `animations/<model_name>.animation.json` – GeckoLib animation definitions.
- `textures/entity/<model_name>.png` – texture for the mech.

## 2. Implement the model class

Create a class that extends `GeoModel` similar to `Pmac01EntityModel` (e.g.
`src/main/java/<your package>/client/model/MyMechModel.java`):

```java
public class MyMechModel extends GeoModel<MyMechEntity> {
    @Override
    public ResourceLocation getModelResource(MyMechEntity animatable) {
        return new ResourceLocation(PomkotsMechsExtension.MODID, "geo/my_mech.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MyMechEntity animatable) {
        return new ResourceLocation(PomkotsMechsExtension.MODID, "textures/entity/my_mech.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MyMechEntity animatable) {
        return new ResourceLocation(PomkotsMechsExtension.MODID, "animations/my_mech.animation.json");
    }
}
```

## 3. Create a renderer

Implement a renderer extending `GeoEntityRenderer`. The renderer wires your
model class to Minecraft's rendering system and can tweak shadows or render
layers. Place it under something like
`src/main/java/<your package>/client/renderer/MyMechRenderer.java`:

```java
public class MyMechRenderer extends GeoEntityRenderer<MyMechEntity> {
    public MyMechRenderer(EntityRendererProvider.Context context) {
        super(context, new MyMechModel());
        this.shadowRadius = 0.7F; // optional shadow size
    }
}
```

This mirrors the existing `Pmac01EntityRenderer` shipped with the mod.

## 4. Register the entity and renderer

Declare a `RegistrySupplier` for your mech (e.g.
`src/main/java/<your package>/entity/vehicle/MyMechEntity.java`) and hook its
attributes during mod initialization. The `PomkotsMechsExtension` class shows
the same pattern for built‑in mechs:

```java
public static final RegistrySupplier<EntityType<MyMechEntity>> MY_MECH =
    registerEntityType("my_mech", MyMechEntity::new, MobCategory.CREATURE, 4F, 10F);

public static void initialize() {
    ENTITIES.register();
    EntityAttributeRegistry.register(MY_MECH::get, MyMechEntity::createMobAttributes);
}
```

On the client side register the renderer:

```java
EntityRendererRegistry.register(PomkotsMechsExtension.MY_MECH,
    context -> new MyMechRenderer(context));
```

With these steps your custom mech will render using your GeckoLib model, texture, and animations.

## 5. Register weapons and items

Weapon classes usually live under `src/main/java/<your package>/items`. Each needs a
JSON model in `src/main/resources/assets/<modid>/models/item` and a texture in
`textures/item`.

```java
public static final DeferredRegister<Item> ITEMS =
    DeferredRegister.create(MODID, Registries.ITEM);

public static final RegistrySupplier<Item> BEAM_RIFLE = ITEMS.register(
    "beam_rifle", () -> new BeamRifleItem(new Item.Properties().stacksTo(1)));

public static void initialize() {
    ITEMS.register();
}
```

## 6. Wire combat actions and attack animations

A mech's attacks are controlled by an `ActionController` and tied to GeckoLib animations. In your entity class, register the actions you want to support:

```java
protected static final int ACT_SHOOT = 7;
protected static final int ACT_GATLING = 10;
protected static final int ACT_BAZOOKA = 9;
protected static final int ACT_SABER = 8;

@Override
protected void registerCombatActions() {
    this.actionController.registerAction(ACT_SHOOT, new Action(20, 10, 10), ActionController.ActionType.R_ARM_MAIN);
    this.actionController.registerAction(ACT_SABER, new Action(60, 11, 9), ActionController.ActionType.L_ARM_MAIN);
    // additional actions...
}
```

Input handlers start these actions and an animation controller selects the matching animation key:

```java
controllers.add(new AnimationController<>(this, "attack", 2, event -> {
    if (this.actionController.getAction(ACT_SHOOT).isInAction()) {
        return event.setAndContinue(RawAnimation.begin().thenPlay("animation." + getMechName() + ".shoot"));
    } else if (this.actionController.getAction(ACT_SABER).isInAction()) {
        return event.setAndContinue(RawAnimation.begin().thenPlay("animation." + getMechName() + ".saber1"));
    }
    return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("animation." + getMechName() + ".nop"));
}));
```

The mod ships a wide set of attack animations. Reference them with the pattern
`animation.<mech_name>.<key>` in both your animation files and entity code.
All available keys are:

| Weapon/Action | Animation keys |
|---------------|----------------|
| Beam rifle | `shoot`, `shootUpperBody` |
| Dual beam rifles | `shoot1`, `shoot2`, `shoot1_upper`, `shoot2_upper`, `shootL1`, `shootL2`, `shootL1_upper`, `shootL2_upper` |
| Beam saber (simple) | `saber`, `saberUpperBody` |
| Beam saber combo | `saber1`, `saber2`, `saber3` and their `_upper` variants |
| Bazooka | `bazooka`, `bazooka_upper` |
| Missile | `missile`, `missileUpperBody` |
| Gatling gun | `gatling1` (spin‑up), `gatling2` (loop) |
| Vulcan guns | `vz`, `vz_upper` |

Use whichever keys your mech supports when wiring the `ActionController` and animation controller.

## 7. Tuning stats with attributes

Define mech stats using Minecraft attributes. Start with a base value and apply
modifiers to adjust them dynamically. Typical mech stats include movement speed,
attack damage, armor and knockback resistance:

```java
public static AttributeSupplier.Builder createMobAttributes() {
    return createLivingAttributes()
        .add(Attributes.ATTACK_KNOCKBACK)
        .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
        .add(Attributes.MAX_HEALTH, CombatBalance.BASE_HEALTH * 0.6);
}
```

Attributes support modifier operations:

- `ADDITION` – flat bonuses, e.g., +5 max health
- `MULTIPLY_BASE` – multiplies the base before other modifiers
- `MULTIPLY_TOTAL` – multiplies the final value after all other modifiers

Example applying a temporary speed boost modifier:

```java
this.getAttribute(Attributes.MOVEMENT_SPEED)
    .addTransientModifier(new AttributeModifier("dash_boost", 0.25, AttributeModifier.Operation.MULTIPLY_TOTAL));
```

Beyond raw numbers, the `ActionController` maintains status flags (dash, jump,
evasion, etc.) which your code can check to gate abilities or switch
animations.

## 8. Additional features

Base mech classes expose extra actions such as jumps, dashes, evasion slides, mounting and cockpit sequences. The controller checks the current action and plays the matching animation (e.g. `dash`, `dash_side`, `evasion_left`, `cockpit_open`). These hooks let you add boosts, open/close cockpits, mount/dismount procedures or other movement abilities alongside your weapon animations.
