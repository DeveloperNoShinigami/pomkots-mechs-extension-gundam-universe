package net.bluelotuscoding.pmegundamuniverse.fabric.client;

import net.bluelotuscoding.pmegundamuniverse.PmegundamuniverseClient;
import net.fabricmc.api.ClientModInitializer;

/**
 * Fabric client entry point.
 */
public class PmegundamuniverseClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PmegundamuniverseClient.init();
    }
}
