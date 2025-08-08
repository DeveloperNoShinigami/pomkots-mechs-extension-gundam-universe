package net.bluelotuscoding.pmegundamuniverse;

import net.bluelotuscoding.pmegundamuniverse.entity.ModAttributes;

/**
 * Main mod class for Pomkots Mechs - Gundam Universe.
 */
public final class Pmegundamuniverse {
    public static final String MOD_ID = "pmegundamuniverse";

    private Pmegundamuniverse() {
    }

    public static void init() {
        ModAttributes.init();
        // TODO: register Gundam mechs and related content here

    }
}
