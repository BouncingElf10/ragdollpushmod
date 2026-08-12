package com.bouncingelf10.ragdollpushmod;

import java.util.Map;
import java.util.WeakHashMap;

import com.bouncingelf10.ragdollpushmod.network.PushPayload;

import dev.leo.sableplayerragdoll.api.RagdollAPI;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class PushHandler {
    private static final double RANGE_TOLERANCE = 2.0;

    private static final Map<ServerPlayer, Long> LAST_PUSH = new WeakHashMap<>();

    private PushHandler() { }

    public static void handle(PushPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer pusher) {
                push(pusher, payload.targetId());
            }
        });
    }

    private static void push(ServerPlayer pusher, int targetId) {
        ServerLevel level = pusher.serverLevel();
        Entity target = level.getEntity(targetId);
        if (!(target instanceof LivingEntity living) || target == pusher || target.isSpectator()) {
            return;
        }
        if (!(living instanceof ServerPlayer) && !Config.PUSH_MOBS.get()) {
            return;
        }

        double reach = Config.PUSH_RANGE.get() + RANGE_TOLERANCE;
        if (pusher.distanceToSqr(target) > reach * reach) {
            return;
        }
        if (!tryConsumeCooldown(pusher, level.getGameTime())) {
            return;
        }

        pusher.swing(InteractionHand.MAIN_HAND, true);

        Vec3 velocity = pusher.getLookAngle().normalize()
                .scale(Config.PUSH_STRENGTH.get())
                .add(0.0, Config.PUSH_LIFT.get(), 0.0);

        if (living instanceof ServerPlayer targetPlayer) {
            RagdollAPI.launch(targetPlayer, velocity);
        } else {
            RagdollAPI.launchMob(level, living, velocity);
        }

        level.playSound(null, target.getX(), target.getY(), target.getZ(),
                SoundEvents.PLAYER_ATTACK_KNOCKBACK, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    private static boolean tryConsumeCooldown(ServerPlayer pusher, long gameTime) {
        Long last = LAST_PUSH.get(pusher);
        if (last != null && gameTime - last < Config.PUSH_COOLDOWN_TICKS.getAsInt()) {
            return false;
        }
        LAST_PUSH.put(pusher, gameTime);
        return true;
    }
}
