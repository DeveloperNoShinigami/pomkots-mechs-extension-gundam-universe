# Custom Mech Model Guide

The extension uses [GeckoLib](https://geckolib.com/) to animate mechs. New models can be added without modifying the base mod.

## 1. Create model files

Place your resources under `src/main/resources/assets/pomkotsmechsextension` in the Forge module.

- `geo/<model_name>.geo.json` – geometry exported from Blockbench.
- `animations/<model_name>.animation.json` – GeckoLib animation definitions.
- `textures/entity/<model_name>.png` – texture for the mech.

## 2. Implement the model class

Create a class that extends `GeoModel` similar to `Pmac01EntityModel`:

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

Implement a renderer extending `GeoEntityRenderer` and register it on the client like the existing `Pmac01EntityRenderer`.

## 4. Register the entity

Register your mech entity and renderer during mod initialization. The `PomkotsMechsExtension` class provides examples of existing registrations.

With these steps your custom mech will render using your GeckoLib model, texture, and animations.

## 5. Register weapons and attack animations

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

Common attack animation keys include:

| Weapon/Action | Animation keys |
|---------------|----------------|
| Beam rifle | `shoot`, `shootUpperBody` |
| Beam saber combo | `saber1`, `saber2`, `saber3`, plus `_upper` variants when moving |
| Bazooka | `bazooka`, `bazooka_upper` |
| Missile | `missile`, `missileUpperBody` |
| Gatling gun | `gatling1` (spin‑up), `gatling2` (loop) |

Reference these with the pattern `animation.<mech_name>.<key>` in both your animation files and entity code.

## 6. Tuning stats with attributes

Define mech stats using Minecraft attributes. Start with a base value and apply modifiers to adjust them dynamically:

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

## 7. Additional features

Base mech classes expose extra actions such as dashes and evasions. The controller checks the current action and plays the matching animation (e.g. `evasion_left`, `dash_keep`). These hooks let you add boosts, cockpit open/close sequences or other movement abilities alongside your weapon animations.
