package greenebolt.chatdc.events;

import greenebolt.chatdc.DiscordChatLink;
import greenebolt.chatdc.Config;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.client.Minecraft;


public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);
        if (!event.getAuthor().isBot()) {

            String recievedMessage = event.getMessage().getContentDisplay().replaceAll("(\\r|\\n)", " ");
            recievedMessage = recievedMessage.replaceAll("\\uFE0F|\\u200D", "");
            DiscordChatLink.LOGGER.info("Recieved: " + recievedMessage);

            if (!event.getChannel().getId().equals(Config.CHANNEL_ID)) {
                DiscordChatLink.LOGGER.info("Message is not from valid channel");
                return;
            }

            DiscordChatLink.LOGGER.info("Message is from valid channel");
            Minecraft mc = Minecraft.getInstance();

            if (mc.player == null) {
                if (DiscordChatLink.JDAActive) {
                    DiscordChatLink.channel.sendMessage("Cannot send chat message: You are not connected to a world.").queue();
                }
                return;
            }

            if (recievedMessage.length() > 256) {
                if (DiscordChatLink.JDAActive) {
                    DiscordChatLink.channel.sendMessage("Cannot send chat message: It exceeds the Minecraft character limit.").queue();
                }
                return;
            }

            if (recievedMessage.startsWith("//")) {
                mc.player.connection.sendCommand(recievedMessage.replaceFirst("//", ""));
            } else {
                mc.player.connection.sendChat(recievedMessage);
            }
        }
    }
}
