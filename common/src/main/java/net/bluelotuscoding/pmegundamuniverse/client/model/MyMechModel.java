package net.bluelotuscoding.pmegundamuniverse.client.model;

import net.bluelotuscoding.pmegundamuniverse.Pmegundamuniverse;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

/**
 * Basic GeckoLib model for a custom Gundam mech.
 *
 * <p>This model demonstrates the pattern for resource lookups used by
 * other mechs. The geometry, texture, and animation files are all
 * resolved relative to the mod's namespace.</p>
 */
public class MyMechModel extends GeoModel<GeoAnimatable> {
    private static final ResourceLocation GEO =
            new ResourceLocation(Pmegundamuniverse.MOD_ID, "geo/my_mech.geo.json");
    private static final ResourceLocation TEX =
            new ResourceLocation(Pmegundamuniverse.MOD_ID, "textures/entity/my_mech.png");
    private static final ResourceLocation ANIM =
            new ResourceLocation(Pmegundamuniverse.MOD_ID, "animations/my_mech.animation.json");

    @Override
    public ResourceLocation getModelResource(GeoAnimatable animatable) {
        return GEO;
    }

    @Override
    public ResourceLocation getTextureResource(GeoAnimatable animatable) {
        return TEX;
    }

    @Override
    public ResourceLocation getAnimationResource(GeoAnimatable animatable) {
        return ANIM;
    }
}
