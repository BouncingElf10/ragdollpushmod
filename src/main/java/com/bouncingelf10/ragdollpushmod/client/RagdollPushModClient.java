package com.bouncingelf10.ragdollpushmod.client;

import com.bouncingelf10.ragdollpushmod.Config;
import com.bouncingelf10.ragdollpushmod.RagdollPushMod;
import com.bouncingelf10.ragdollpushmod.network.PushPayload;
import com.mojang.blaze3d.platform.InputConstants;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.network.PacketDistributor;

import org.lwjgl.glfw.GLFW;

@Mod(value = RagdollPushMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = RagdollPushMod.MODID, value = Dist.CLIENT)
public class RagdollPushModClient {
    public static final KeyMapping PUSH_KEY = new KeyMapping(
            "key.ragdollpushmod.push",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "key.categories.ragdollpushmod");

    private static final ResourceLocation PUSH_HUD_LAYER = ResourceLocation.fromNamespaceAndPath(RagdollPushMod.MODID, "push_target");

    private static final int PROMPT_OFFSET_Y = 10;
    private static final int PROMPT_COLOR = 0x9BE8FF;

    public RagdollPushModClient(ModContainer container, IEventBus modEventBus) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        modEventBus.addListener(RagdollPushModClient::registerKeys);
        modEventBus.addListener(RagdollPushModClient::registerGuiLayers);
    }

    private static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(PUSH_KEY);
    }

    private static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.CROSSHAIR, PUSH_HUD_LAYER, RagdollPushModClient::renderPrompt);
    }

    private static void renderPrompt(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        Entity target = PushTargeting.target();
        if (target == null || minecraft.options.hideGui || !Config.TARGET_PROMPT.get()) {
            return;
        }

        Component label = Component.translatable("hud.ragdollpushmod.push_target", target.getDisplayName(), PUSH_KEY.getTranslatedKeyMessage());
        int x = (graphics.guiWidth() - minecraft.font.width(label)) / 2;
        int y = graphics.guiHeight() / 2 + PROMPT_OFFSET_Y;
        graphics.drawString(minecraft.font, label, x, y, PROMPT_COLOR, true);
    }

    @SubscribeEvent
    static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) {
            PushTargeting.clear();
            return;
        }

        PushTargeting.tick(minecraft.player, minecraft.level);

        boolean pressed = false;
        while (PUSH_KEY.consumeClick()) {
            pressed = true;
        }

        Entity target = PushTargeting.target();
        if (pressed && target != null) {
            PacketDistributor.sendToServer(new PushPayload(target.getId()));
        }
    }
}
