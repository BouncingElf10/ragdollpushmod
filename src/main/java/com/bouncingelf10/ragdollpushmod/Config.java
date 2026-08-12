package com.bouncingelf10.ragdollpushmod;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue PUSH_RANGE = BUILDER
            .comment("How far away a target can be to get pushed, in blocks.")
            .defineInRange("pushRange", 6.0, 1.0, 64.0);

    public static final ModConfigSpec.DoubleValue PUSH_STRENGTH = BUILDER
            .comment("Launch speed along your look direction, in meters per second.")
            .defineInRange("pushStrength", 4.0, 0.0, 200.0);

    public static final ModConfigSpec.DoubleValue PUSH_LIFT = BUILDER
            .comment("Extra upward launch speed, in meters per second.")
            .defineInRange("pushLift", 1.0, -50.0, 50.0);

    public static final ModConfigSpec.IntValue PUSH_COOLDOWN_TICKS = BUILDER
            .comment("Ticks to wait between pushes.")
            .defineInRange("pushCooldownTicks", 20, 0, 1200);

    public static final ModConfigSpec.BooleanValue PUSH_MOBS = BUILDER
            .comment("Whether mobs can be pushed as well as players.")
            .define("pushMobs", false);

    public static final ModConfigSpec.BooleanValue TARGET_PARTICLES = BUILDER
            .comment("Whether to show particles around the target you are aiming at.")
            .define("targetParticles", true);

    public static final ModConfigSpec.BooleanValue TARGET_PROMPT = BUILDER
            .comment("Whether to show the target's name and push key under the crosshair.")
            .define("targetPrompt", true);

    static final ModConfigSpec SPEC = BUILDER.build();
}
