package greenebolt.chatdc;

import greenebolt.chatdc.events.CommandListener;
import greenebolt.chatdc.registration.ChatListeners;
import greenebolt.chatdc.registration.CommandHandler;
import greenebolt.chatdc.events.BotReady;
import greenebolt.chatdc.events.MessageListener;
import greenebolt.chatdc.utils.Util;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.fabricmc.api.ModInitializer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class DiscordChatLink implements ModInitializer {
	public static final String MOD_ID = "discord-chat-link";
	public static final String VERSION = "1.0.0";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static Config config;
	public static boolean JDAActive = false;
	public static JDA jda;
	public static TextChannel channel;

	@Override
	public void onInitialize() {

		// Initialize config
		Minecraft mc = Minecraft.getInstance();
		config = new Config(mc.gameDirectory.getAbsolutePath() + File.separator + "config" + File.separator + "DiscordChatLinkConfig.cfg");
		config.read();

		InitializeDiscrdBot();

		CommandHandler.register();
		ChatListeners.register();

		LOGGER.info("Discord Chat Link is active...");

	}

	public static void InitializeDiscrdBot() {

		if (Config.BOT_TOKEN.equals("") || Config.GUILD_ID.equals("") || Config.CHANNEL_ID.equals("")) {
			LOGGER.info("Config not properly configured: Abandoning Bot Init...");
			Minecraft mc = Minecraft.getInstance();
			if (mc.player == null) return;
			mc.player.displayClientMessage(Component.translatable("Config not properly configured: Abandoning Bot Init...").withStyle(ChatFormatting.RED), false);
			Util.SendConfigMessage(mc);
			return;
		}

		JDABuilder jdaBuilder = JDABuilder.createLight(Config.BOT_TOKEN);
		jdaBuilder.enableIntents(
				GatewayIntent.MESSAGE_CONTENT,
				GatewayIntent.GUILD_MESSAGES
		);
		jdaBuilder.addEventListeners(
				new MessageListener(),
				new BotReady(),
				new CommandListener()
		);

		try {
			jda = jdaBuilder.build();

			CommandListUpdateAction commands = jda.updateCommands();
			commands.addCommands(
					Commands.slash("quit", "Leave the server/world")
							.setContexts(InteractionContextType.GUILD)
							.setDefaultPermissions(DefaultMemberPermissions.DISABLED),
					Commands.slash("health", "Gets player health")
							.setContexts(InteractionContextType.GUILD)
							.setDefaultPermissions(DefaultMemberPermissions.DISABLED),
					Commands.slash("hunger", "Gets player hunger")
							.setContexts(InteractionContextType.GUILD)
							.setDefaultPermissions(DefaultMemberPermissions.DISABLED)
			);
			commands.queue();

		} catch (Exception e) {
			LOGGER.info("Error with loading discord bot: " + e);
		}

	}
}