package net.bluelotuscoding.pmegundamuniverse.forge;

import net.bluelotuscoding.pmegundamuniverse.Pmegundamuniverse;
import net.minecraftforge.fml.common.Mod;

/**
 * Forge entry point for the Gundam Universe addon.
 */
@Mod(Pmegundamuniverse.MOD_ID)
public class PmegundamuniverseForge {
    public PmegundamuniverseForge() {
        Pmegundamuniverse.init();
    }
}
