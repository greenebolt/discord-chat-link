package greenebolt.chatdc.events;

import greenebolt.chatdc.DiscordChatLink;
import greenebolt.chatdc.Config;
import greenebolt.chatdc.utils.Util;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class BotReady extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event) {
        super.onReady(event);

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        Guild guild = null;

        try {
            guild = event.getJDA().getGuildById(Config.GUILD_ID);
        } catch (Exception e) {
            DiscordChatLink.LOGGER.info("Error loading server: " + e);
            Util.SendConfigMessage(mc, "Error with loading discord server: The provided server id is invalid. Use /setchannel from discord.");
            return;
        }
        try {
            DiscordChatLink.channel = guild.getTextChannelById(Config.CHANNEL_ID);
        } catch (Exception e) {
            DiscordChatLink.LOGGER.info("Error loading channel: " + e);
            Util.SendConfigMessage(mc, "Error with loading discord channel: The provided channel id is invalid. Use /setchannel from discord.");
            return;
        }
        DiscordChatLink.JDAActive = true;
        DiscordChatLink.channel.sendMessage("[ACTIVATED]").queue();
        if (player != null) Minecraft.getInstance().player.displayClientMessage(Component.translatable("Discord bot online!").withStyle(ChatFormatting.GREEN), false);
    }

    @Override
    public void onShutdown(ShutdownEvent event) {
        System.out.println("JDA instance has shut down completely.");
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) return;
        if (DiscordChatLink.channel == null) return;
        client.execute(() -> client.player.displayClientMessage(Component.translatable("Discord Bot has shut down!").withStyle(ChatFormatting.GREEN), false));
    }
}
