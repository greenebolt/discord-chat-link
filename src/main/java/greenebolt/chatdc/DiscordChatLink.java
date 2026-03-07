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
	public static TextChannel channel = null;

	@Override
	public void onInitialize() {

		LOGGER.info("Discord Chat Link: Initializing!");

		// Initialize config
		Minecraft mc = Minecraft.getInstance();
		config = new Config(mc.gameDirectory.getAbsolutePath() + File.separator + "config" + File.separator + "DiscordChatLinkConfig.cfg");
		config.read();

		InitializeDiscrdBot();

		CommandHandler.register();
		ChatListeners.register();
	}

	public static void InitializeDiscrdBot() {

		config.read();
		if (Config.BOT_TOKEN.equals("") || Config.GUILD_ID.equals("") || Config.CHANNEL_ID.equals("")) {
			LOGGER.info("Config not properly configured: Abandoning Bot Init...");
			Minecraft mc = Minecraft.getInstance();
			Util.SendConfigMessage(mc, "Config not properly configured: Abandoning Bot Init...");
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
							.setDefaultPermissions(DefaultMemberPermissions.DISABLED),
					Commands.slash("screenshot", "Takes a screenshot of your game")
							.setContexts(InteractionContextType.GUILD)
							.setDefaultPermissions(DefaultMemberPermissions.DISABLED)
			);
			commands.queue();

		} catch (Exception e) {
			LOGGER.info("Error with loading discord bot: " + e);
			if (Minecraft.getInstance().player == null) return;
			if (e.toString().contains("The provided token is invalid!")) {
				Util.SendConfigMessage(Minecraft.getInstance(), "Error with loading discord bot: The provided token is invalid. Use /set-discord-bot-token");
				return;
			}
			Minecraft.getInstance().player.displayClientMessage(Component.translatable("Error with loading discord bot: " + e).withStyle(ChatFormatting.RED), false);
		}

	}
}