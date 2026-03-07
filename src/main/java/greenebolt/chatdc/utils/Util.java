package greenebolt.chatdc.utils;

import greenebolt.chatdc.DiscordChatLink;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;

import java.net.URI;

public class Util {
    public static void Disconnect() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.getSingleplayerServer() != null) {
            mc.execute(() -> {
                mc.disconnectFromWorld(Component.translatable("Discord quit world"));
            });
            return;
        }
        if (mc.player == null) return;
        mc.execute(() -> {
            mc.disconnect(new TitleScreen(), false);
        });
    }
    public static void Start() {
        if (DiscordChatLink.JDAActive) {
            Minecraft.getInstance().player.displayClientMessage(Component.translatable("Discord bot is already online! Use \"/stop-discord-chat-link\" first if you wish to restart.").withStyle(ChatFormatting.RED), false);
            return;
        }
        if (DiscordChatLink.jda != null) {
            if (DiscordChatLink.jda.getStatus() != JDA.Status.SHUTDOWN) {
                DiscordChatLink.LOGGER.info("Can't start bot because it's status is: " + DiscordChatLink.jda.getStatus());
                Minecraft.getInstance().player.displayClientMessage(Component.translatable("Can't start bot because it's status is: " + DiscordChatLink.jda.getStatus()).withStyle(ChatFormatting.RED), false);
                return;
            }
        }
        Minecraft.getInstance().player.displayClientMessage(Component.translatable("Starting Discord Chat Link... ").withStyle(ChatFormatting.GREEN), false);
        DiscordChatLink.InitializeDiscrdBot();
    }
    public static void Stop(String caller) {

        Player player = Minecraft.getInstance().player;
        if (DiscordChatLink.JDAActive) {
            if (player != null && caller.equals("CommandHandler")) player.displayClientMessage(Component.translatable("Discord bot is shutting down...").withStyle(ChatFormatting.GREEN), false);
        } else {
            if (player != null && caller.equals("CommandHandler")) player.displayClientMessage(Component.translatable("Discord bot is already offline or shutting down...").withStyle(ChatFormatting.RED), false);
        }

        DiscordChatLink.LOGGER.info("Stopping Discord Bot");
        DiscordChatLink.jda.shutdown();
        DiscordChatLink.JDAActive = false;
    }
    public static String getHealth() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return "NA: You are not connected to a world.";
        return "Player Health: " + Math.round(mc.player.getHealth() * 100)/100 + "/20";
    }
    public static String getHunger() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return "NA: You are not connected to a world.";
        return "Player Hunger: " + Math.round(mc.player.getFoodData().getFoodLevel() * 100)/100 + "/20, Saturation: " + Math.round(mc.player.getFoodData().getSaturationLevel()*100)/100 + "/20";
    }
    public static void SendConfigMessage(Minecraft client, String error) {
        if (client.player == null) return;
        String url = "https://github.com/greenebolt/discord-chat-link";
        ClickEvent click = new ClickEvent.OpenUrl(URI.create(url));
        HoverEvent hoverEvent = new HoverEvent.ShowText(Component.translatable(url));
        Component msg = Component.translatable("Discord bot is not initialized:\n\nPlease follow the modrinth/github/youtube instructions\nto set up Discord Chat Link Config\n\n(Click Here)")
                .withStyle(Style.EMPTY
                        .withClickEvent(click)
                        .withHoverEvent(hoverEvent)
                        .withColor(ChatFormatting.GREEN));

        client.execute(() -> {
            client.player.displayClientMessage(msg, false);
            if (error != null)
                client.player.displayClientMessage(Component.translatable(error).withStyle(ChatFormatting.RED), false);
        });

    }
}
