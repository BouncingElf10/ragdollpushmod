package com.bouncingelf10.ragdollpushmod.network;

import com.bouncingelf10.ragdollpushmod.RagdollPushMod;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record PushPayload(int targetId) implements CustomPacketPayload {
    public static final Type<PushPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RagdollPushMod.MODID, "push"));

    public static final StreamCodec<ByteBuf, PushPayload> STREAM_CODEC =
            ByteBufCodecs.VAR_INT.map(PushPayload::new, PushPayload::targetId);

    @Override
    public Type<PushPayload> type() {
        return TYPE;
    }
}
