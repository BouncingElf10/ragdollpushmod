package com.bouncingelf10.ragdollpushmod.client;

import javax.annotation.Nullable;

import com.bouncingelf10.ragdollpushmod.Config;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public final class PushTargeting {
    private static final int PARTICLE_INTERVAL_TICKS = 3;
    private static final int RING_POINTS = 3;
    private static final int RING_PERIOD_TICKS = 40;

    @Nullable
    private static Entity target;

    private PushTargeting() { }

    @Nullable
    public static Entity target() {
        return target;
    }

    static void tick(Player player, ClientLevel level) {
        target = raycast(player);
        if (target != null && Config.TARGET_PARTICLES.get()) {
            spawnRing(level, target);
        }
    }

    static void clear() {
        target = null;
    }

    @Nullable
    private static Entity raycast(Player player) {
        double range = Config.PUSH_RANGE.get();
        Vec3 eye = player.getEyePosition();
        Vec3 reach = player.getViewVector(1.0F).scale(range);
        AABB search = player.getBoundingBox().expandTowards(reach).inflate(1.0);

        EntityHitResult hit = ProjectileUtil.getEntityHitResult(player, eye, eye.add(reach), search, PushTargeting::pushable, range * range);
        return hit == null ? null : hit.getEntity();
    }

    private static boolean pushable(Entity entity) {
        if (!(entity instanceof LivingEntity) || entity.isSpectator() || !entity.isPickable()) {
            return false;
        }
        return entity instanceof Player || Config.PUSH_MOBS.get();
    }

    private static void spawnRing(ClientLevel level, Entity target) {
        long time = level.getGameTime();
        if (time % PARTICLE_INTERVAL_TICKS != 0) {
            return;
        }

        AABB box = target.getBoundingBox();
        double radius = box.getXsize() * 0.6 + 0.2;
        double y = box.minY + box.getYsize() * 0.5;
        double spin = (time % RING_PERIOD_TICKS) / (double) RING_PERIOD_TICKS * Math.PI * 2.0;

        for (int i = 0; i < RING_POINTS; i++) {
            double angle = spin + i * (Math.PI * 2.0 / RING_POINTS);
            level.addParticle(ParticleTypes.ELECTRIC_SPARK, target.getX() + Math.cos(angle) * radius, y, target.getZ() + Math.sin(angle) * radius, 0.0, 0.02, 0.0);
        }
    }
}
