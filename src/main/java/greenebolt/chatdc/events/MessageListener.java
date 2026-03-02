package greenebolt.chatdc.events;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import greenebolt.chatdc.Afkchattodiscord;
import greenebolt.chatdc.Config;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;


public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        super.onMessageReceived(event);
        if (!event.getAuthor().isBot()) {

            String recievedMessage = event.getMessage().getContentDisplay();
            Afkchattodiscord.LOGGER.info("Recieved: " + recievedMessage);

            if (!event.getChannel().getId().equals(Config.CHANNEL_ID)) {
                Afkchattodiscord.LOGGER.info("Message is not from valid channel");
                return;
            }

            Afkchattodiscord.LOGGER.info("Message is from valid channel");
            Minecraft mc = Minecraft.getInstance();

            if (recievedMessage.equals("quit")) {
                Afkchattodiscord.Disconnect();
                return;
            }

            if (mc.player == null) {
                if (Afkchattodiscord.JDAActive) {
                    Afkchattodiscord.channel.sendMessage("Cannot send chat message: You are not connected to a world...").queue();
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
