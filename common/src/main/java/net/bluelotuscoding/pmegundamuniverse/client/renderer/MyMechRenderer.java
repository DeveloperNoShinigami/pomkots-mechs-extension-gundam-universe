package net.bluelotuscoding.pmegundamuniverse.client.renderer;

import net.bluelotuscoding.pmegundamuniverse.client.model.MyMechModel;
import net.bluelotuscoding.pmegundamuniverse.entity.GundamMechEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/**
 * Renderer for {@link GundamMechEntity} that leverages Geckolib for animations.
 */
public class MyMechRenderer extends GeoEntityRenderer<GundamMechEntity> {
    public MyMechRenderer(EntityRendererProvider.Context context) {
        super(context, new MyMechModel());
        this.shadowRadius = 0.5f;
    }
}
