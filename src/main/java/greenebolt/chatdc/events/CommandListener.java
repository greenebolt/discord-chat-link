package greenebolt.chatdc.events;

import greenebolt.chatdc.DiscordChatLink;
import greenebolt.chatdc.utils.Util;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.network.chat.Component;

import java.io.File;

public class CommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {

            case "quit" -> {
                if (Minecraft.getInstance().player == null) {
                    event.reply("Can not quit: You are not connected to a world.").setEphemeral(true).queue();
                    return;
                }
                event.reply("You left the game.").setEphemeral(true).queue();
                Util.Disconnect();
            }

            case "health" -> event.reply(Util.getHealth()).setEphemeral(true).queue();
            case "hunger" -> event.reply(Util.getHunger()).setEphemeral(true).queue();

            case "screenshot" -> {
                Minecraft mc = Minecraft.getInstance();
                mc.execute(() -> {
                    event.reply("Taking Screenshot...").setEphemeral(true).queue();
                    Screenshot.grab(mc.gameDirectory, mc.getMainRenderTarget(), message -> mc.execute(() -> {

                        if (mc.player != null) mc.player.displayClientMessage(Component.translatable("Took Screenshot"), false);
                        File screenshot = Util.GetMostRecentFile(mc.gameDirectory + "/screenshots");
                        DiscordChatLink.channel.sendFiles(FileUpload.fromData(screenshot)).queue();

                    }));
                });
            }

        }
    }
}
