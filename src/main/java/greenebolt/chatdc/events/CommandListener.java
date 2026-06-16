package greenebolt.chatdc.events;

import greenebolt.chatdc.Config;
import greenebolt.chatdc.DiscordChatLink;
import greenebolt.chatdc.utils.Util;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.network.chat.Component;

import java.io.File;

public class CommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {

            case "setchannel" -> {
                event.reply(">> This channel has been set to be your default connection point!\n[Discord Chat Link is active]").queue();
                Config.GUILD_ID = event.getGuild().getId();
                Config.CHANNEL_ID = event.getChannelId();
                Config.save();
                DiscordChatLink.channel = event.getChannel().asTextChannel();
                DiscordChatLink.JDAActive = true;

                Minecraft mc = Minecraft.getInstance();
                mc.execute(() -> {
                    if (mc.player != null) mc.player.sendSystemMessage(Component.translatable("Discord channel: \"" + event.getChannel().getName() + "\" is now connected...").withStyle(ChatFormatting.GREEN));
                });
            }

            case "quit" -> {
                if (Minecraft.getInstance().player == null) {
                    event.reply("Can not quit: You are not connected to a world.").setEphemeral(true).queue();
                    return;
                }
                event.reply("You left the game.").queue();
                Util.Disconnect();
            }

            case "health" -> event.reply(Util.getHealth()).queue();
            case "hunger" -> event.reply(Util.getHunger()).queue();

            case "screenshot" -> {
                Minecraft mc = Minecraft.getInstance();
                mc.execute(() -> {
                    event.reply("Taking Screenshot...").setEphemeral(true).queue();
                    Screenshot.grab(mc.gameDirectory, mc.gameRenderer.mainRenderTarget(), message -> mc.execute(() -> {

                        if (mc.player != null) mc.player.sendSystemMessage(Component.translatable("Discord Took Screenshot...").withStyle(ChatFormatting.UNDERLINE));
                        File screenshot = Util.GetMostRecentFile(mc.gameDirectory + "/screenshots");
                        DiscordChatLink.channel.sendFiles(FileUpload.fromData(screenshot)).queue();

                    }));
                });
            }
        }
    }
}
