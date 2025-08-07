package net.bluelotuscoding.pmegundamuniverse.client.renderer;

import net.bluelotuscoding.pmegundamuniverse.Pmegundamuniverse;
import net.bluelotuscoding.pmegundamuniverse.entity.MyMechEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * Basic renderer for {@link MyMechEntity}.
 */
public class MyMechRenderer extends EntityRenderer<MyMechEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Pmegundamuniverse.MOD_ID, "textures/entity/my_mech.png");

    public MyMechRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(MyMechEntity entity) {
        return TEXTURE;
    }
}
