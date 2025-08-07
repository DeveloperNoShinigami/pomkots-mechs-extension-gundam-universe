package net.bluelotuscoding.pmegundamuniverse;

import net.bluelotuscoding.pmegundamuniverse.entity.ModEntities;

/**
 * Main mod class for Pomkots Mechs - Gundam Universe.
 */
public final class Pmegundamuniverse {
    public static final String MOD_ID = "pmegundamuniverse";

    private Pmegundamuniverse() {
    }

    public static void init() {
        ModEntities.init();
    }
}
