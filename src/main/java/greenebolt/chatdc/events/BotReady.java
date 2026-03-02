package greenebolt.chatdc.events;

import greenebolt.chatdc.Afkchattodiscord;
import greenebolt.chatdc.Config;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotReady extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event) {
        super.onReady(event);

        Afkchattodiscord.JDAActive = true;
        Afkchattodiscord.channel = event.getJDA().getGuildById(Config.GUILD_ID).getTextChannelById(Config.CHANNEL_ID);
        Afkchattodiscord.channel.sendMessage("Chat Monitor Active...").queue();
    }
}
