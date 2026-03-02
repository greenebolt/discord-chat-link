package greenebolt.chatdc.registration;

import greenebolt.chatdc.DiscordChatLink;
import greenebolt.chatdc.utils.Util;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;

import java.net.URI;
import java.net.URISyntaxException;

public class ChatListeners {
    public static void register() {
        ClientReceiveMessageEvents.GAME.register((message, timestamp) -> {
            try {
                String text = message.getString();
                if (DiscordChatLink.JDAActive) {
                    DiscordChatLink.channel.sendMessage(text).queue();
                }
            } catch (Exception e) {
                DiscordChatLink.LOGGER.error("Error in chat listener", e);
            }
        });
        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, timestamp) -> {
            try {
                String text = message.getString();
                if (DiscordChatLink.JDAActive) {
                    DiscordChatLink.channel.sendMessage(text).queue();
                }
            } catch (Exception e) {
                DiscordChatLink.LOGGER.error("Error in chat listener", e);
            }
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (DiscordChatLink.JDAActive) return;
            Util.SendConfigMessage(client);
        });
    }
}
