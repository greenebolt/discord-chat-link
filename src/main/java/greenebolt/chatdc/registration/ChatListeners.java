package greenebolt.chatdc.registration;

import greenebolt.chatdc.DiscordChatLink;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;

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
    }
}
