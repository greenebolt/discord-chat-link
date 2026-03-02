package greenebolt.chatdc.registration;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;

public class ChatListeners {
    public static void register() {
        ClientReceiveMessageEvents.GAME.register((message, timestamp) -> {
            try {
                String text = message.getString();
                if (JDAActive) {
                    channel.sendMessage(text).queue();
                }
            } catch (Exception e) {
                LOGGER.error("Error in chat listener", e);
            }
        });
        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, timestamp) -> {
            try {
                String text = message.getString();
                if (JDAActive) {
                    channel.sendMessage(text).queue();
                }
            } catch (Exception e) {
                LOGGER.error("Error in chat listener", e);
            }
        });
    }
}
