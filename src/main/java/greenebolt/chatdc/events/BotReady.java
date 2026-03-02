package greenebolt.chatdc.events;

import greenebolt.chatdc.DiscordChatLink;
import greenebolt.chatdc.Config;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotReady extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event) {
        super.onReady(event);

        DiscordChatLink.JDAActive = true;
        DiscordChatLink.channel = event.getJDA().getGuildById(Config.GUILD_ID).getTextChannelById(Config.CHANNEL_ID);
        DiscordChatLink.channel.sendMessage("Chat Monitor Active...").queue();
    }
}
