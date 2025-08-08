package net.bluelotuscoding.pmegundamuniverse.client.model;

import net.bluelotuscoding.pmegundamuniverse.Pmegundamuniverse;
import net.bluelotuscoding.pmegundamuniverse.entity.GundamMechEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

/**
 * Basic geckolib model for a Gundam mech.
 */
public class MyMechModel extends GeoModel<GundamMechEntity> {
    private static final ResourceLocation MODEL = new ResourceLocation(Pmegundamuniverse.MOD_ID, "geo/gundam_mech.geo.json");
    private static final ResourceLocation TEXTURE = new ResourceLocation(Pmegundamuniverse.MOD_ID, "textures/entity/gundam_mech.png");
    private static final ResourceLocation ANIMATION = new ResourceLocation(Pmegundamuniverse.MOD_ID, "animations/gundam_mech.animation.json");

    @Override
    public ResourceLocation getModelResource(GundamMechEntity animatable) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(GundamMechEntity animatable) {
        return TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(GundamMechEntity animatable) {
        return ANIMATION;
    }
}
