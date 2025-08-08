# Unimplemented Suggestions

This document tracks enhancement ideas that remain to be implemented in the Pomkots Mechs extension.

## Animation and Visual Effects
- Dash skid, hover lean, and cockpit reaction animations that vary with pilot accuracy. Prototype hooks: `GundamMechEntity#playDashSkidAnimation`, `playHoverLeanAnimation`, and `playCockpitReactionAnimation`.
- Vertical-thrust effects that scale with `mech_jump_sustain`, such as hover jets or exhaust plumes. Prototype hook: `GundamMechEntity#spawnVerticalThrustEffects`.
- Overheat, malfunction, or vent sequences showing smoke, sparks, steam, or warning alarms when energy or jump sustain is pushed too high. Prototype hook: `GundamMechEntity#spawnOverheatEffects`.
- Startup/shutdown, low-health warning, and other situational animations to enhance immersion.

## Combat and Movement Mechanics
- Attributes like `mech_pilot_stability` to reduce flinch and `mech_ai_aggression` to boost weapon usage frequency.
- A dedicated `MechPilotEntity` that seeks out mechs, mounts them, and drives using exposed pilot attributes.
- Weapon upgrade modules that visually change armaments and alter attack animations.
- Finisher attack chains that trigger on staggered enemies.
- Multi-stage aerial combos, dive attacks, and staggered missile volleys.
- Combo or chained melee animations, such as three-stage saber combos.
- Charged beam shots, burst-fire modes, or other alternate ranged attack sequences.

## Energy-Based Systems
- Energy thresholds that trigger special moves or temporary buffs.
- Visual and audio cues when the energy gauge depletes or overheats.

This list will evolve as ideas are explored and features are implemented.
