package net.bluelotuscoding.pmegundamuniverse;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.bluelotuscoding.pmegundamuniverse.client.renderer.MyMechRenderer;
import net.bluelotuscoding.pmegundamuniverse.entity.PmegundamuniverseEntities;

/**
 * Client-side initialization for Pomkots Mechs - Gundam Universe.
 */
public final class PmegundamuniverseClient {
    private PmegundamuniverseClient() {
    }

    public static void init() {
        EntityRendererRegistry.register(() -> PmegundamuniverseEntities.GUNDAM_MECH, MyMechRenderer::new);
    }
}
