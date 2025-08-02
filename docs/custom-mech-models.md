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
