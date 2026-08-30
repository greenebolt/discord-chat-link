package greenebolt.chatdc.registration;

import greenebolt.chatdc.Config;
import greenebolt.chatdc.DiscordChatLink;
import greenebolt.chatdc.utils.Util;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.multiplayer.ClientPacketListener;

import java.awt.desktop.QuitEvent;

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
            if (Config.BOT_TOKEN.equals("")) {
                Util.SendConfigMessage(client, "No discord bot token is defined: Do /set-discord-bot-token");
                return;
            }
            Util.SendConfigMessage(client, "No discord channel is defined: Do /setchannel command from discord.");
        });
        ClientLifecycleEvents.CLIENT_STOPPING.register((mc) -> {
            if (!DiscordChatLink.JDAActive) return;
            Util.Stop("ClientStopped");
        });
    }
}
