package net.bluelotuscoding.pmegundamuniverse.entity;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

/**
 * Simple mech entity placeholder with basic attributes.
 */
public class MyMechEntity extends Mob {
    public MyMechEntity(EntityType<? extends Mob> type, Level level) {
        super(type, level);
    }

    /**
     * Creates the default attribute set for this mech.
     *
     * @return attribute builder defining health and movement speed
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 20.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.2D);
    }

    @Override
    protected void registerGoals() {
        // TODO: implement AI goals
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}
