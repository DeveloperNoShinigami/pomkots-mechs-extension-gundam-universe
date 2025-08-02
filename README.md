# Pomkots Mechs Extension: Gundam Universe

This project is a Forge-only addon for the [Pomkots Mechs](https://example.com) mod. It targets Minecraft Forge 1.20.1 and provides additional mechs and content built on top of the base mod.

## Development

Use the Gradle wrapper for all development tasks:

```bash
./gradlew build
```

This compiles the mod and remaps the jar for distribution. A development client can be started with:

```bash
./gradlew :forge:runClient
```

## Custom Mech Models

The extension uses [GeckoLib](https://geckolib.com/) for animated mech models. To create your own models, see [docs/custom-mech-models.md](docs/custom-mech-models.md).
