package com.bouncingelf10.ragdollpushmod;

import com.bouncingelf10.ragdollpushmod.network.PushPayload;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(RagdollPushMod.MODID)
public class RagdollPushMod {
    public static final String MODID = "ragdollpushmod";

    public RagdollPushMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(RagdollPushMod::registerPayloads);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar("1").playToServer(PushPayload.TYPE, PushPayload.STREAM_CODEC, PushHandler::handle);
    }
}
