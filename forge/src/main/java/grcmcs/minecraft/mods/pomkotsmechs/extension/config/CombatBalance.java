package grcmcs.minecraft.mods.pomkotsmechs.extension.config;

public class CombatBalance {
    public static final int BASE_HEALTH = 300;
    public static final int BASE_DAMAGE = BASE_HEALTH / 10;

    public static final int BASE_DAMAGE_ZAKUBAZ = BASE_DAMAGE * 3;
    public static final float BASE_SPEED_ZAKUBAZ = 2.5F;

    // Flat base damage and speed values
    public static final int BASE_DAMAGE_BEAM_LARGE = BASE_DAMAGE * 4; // flat
    public static final float BASE_SPEED_BEAM_LARGE = 2.5F;

    public static final int BASE_DAMAGE_BEAM = BASE_DAMAGE * 2; // flat
    public static final float BASE_SPEED_BEAM = 2.5F;

    public static final int BASE_DAMAGE_MISSILE = BASE_DAMAGE * 3; // flat
    public static final float BASE_SPEED_MISSILE = 2F;

    public static final int BASE_DAMAGE_MACHINEGUN = BASE_DAMAGE / 3; // flat
    public static final float BASE_SPEED_MACHINEGUN = 2.5F;

    public static final int BASE_DAMAGE_SABER = BASE_DAMAGE * 10; // flat

    public static final int BASE_ENERGY = 100; // flat

    // Flat movement and mobility values
    public static final float BASE_DASH_SPEED = 2.5F;
    public static final float BASE_DASH_SIDE_SPEED = 2.5F;
    public static final float BASE_EVASION_LEFT_SPEED = 7.125F;
    public static final float BASE_EVASION_RIGHT_SPEED = 7.125F;
    public static final float BASE_JUMP_SPEED = 2.0F;
    public static final float BASE_JUMP_SUSTAIN_MULTIPLIER = 0.35F; // innate modifier: portion of jump speed applied while button held

    // AI pilot behaviour defaults
    public static final float BASE_PILOT_ACCURACY = 1.0F; // innate multiplier for ranged attack spread
    public static final float BASE_PILOT_REACTION = 1.0F; // innate multiplier for AI action cooldowns

    // Innate modifiers applied by mech logic
    public static final float DAMAGE_MODIFIER = 2F / 3F; // scales most weapon damage
    public static final float ENERGY_COST_MULTIPLIER = 2F; // doubles energy cost in base class
    public static final float PMA_ENERGY_COST_MODIFIER = 2F / 3F; // aerial units consume less energy before base multiplier
    public static final float LARGE_BEAM_DAMAGE_MULTIPLIER = 2F; // doubles damage for large beam variants
}
