package net.bluelotuscoding.pmegundamuniverse.fabric;

import net.bluelotuscoding.pmegundamuniverse.Pmegundamuniverse;
import net.fabricmc.api.ModInitializer;

/**
 * Fabric entry point for the Gundam Universe addon.
 */
public class PmegundamuniverseFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Pmegundamuniverse.init();
    }
}
