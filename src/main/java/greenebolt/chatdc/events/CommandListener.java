package greenebolt.chatdc.events;

import greenebolt.chatdc.utils.Util;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "quit" -> {
                event.reply("Quitting...").setEphemeral(true).queue();
                Util.Disconnect();
            }
            case "health" -> event.reply(Util.getHealth()).setEphemeral(true).queue();
            case "hunger" -> event.reply(Util.getHunger()).setEphemeral(true).queue();
        }
    }
}
