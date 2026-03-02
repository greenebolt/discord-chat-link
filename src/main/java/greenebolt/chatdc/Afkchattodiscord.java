package greenebolt.chatdc;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import greenebolt.chatdc.events.BotReady;
import greenebolt.chatdc.events.MessageListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.utils.JDALogger;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Afkchattodiscord implements ModInitializer {
	public static final String MOD_ID = "afk-chat-to-discord";
	public static final String VERSION = "1.0.0";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static Config config;
	public static boolean JDAActive = false;
	private static boolean isDisconnecting = false;
	public static TextChannel channel;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// Initialize config
		Minecraft mc = Minecraft.getInstance();
		config = new Config(mc.gameDirectory.getAbsolutePath() + File.separator + "config" + File.separator + "ChatToDiscordConfig.cfg");
		config.read();

		if (!Config.BOT_TOKEN.equals("")) {
			JDABuilder jdaBuilder = JDABuilder.createLight(Config.BOT_TOKEN);
			jdaBuilder.enableIntents(
					GatewayIntent.MESSAGE_CONTENT,
					GatewayIntent.GUILD_MESSAGES
			);
			jdaBuilder.addEventListeners(
					new MessageListener(),
					new BotReady()
			);

			try {
				jdaBuilder.build();
			} catch (Exception e) {
				LOGGER.info("Error with loading discord bot: " + e);
			}
		}

		ClientReceiveMessageEvents.GAME.register((message, timestamp) -> {
			try {
				String text = message.getString();
				if (JDAActive) {
					channel.sendMessage(text).queue();
				}
			} catch (Exception e) {
				Afkchattodiscord.LOGGER.error("Error in chat listener", e);
			}
		});
		ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, timestamp) -> {
			try {
				String text = message.getString();
				if (JDAActive) {
					channel.sendMessage(text).queue();
				}
			} catch (Exception e) {
				Afkchattodiscord.LOGGER.error("Error in chat listener", e);
			}
		});

		ClientCommandRegistrationCallback.EVENT.register(
				(dispatcher, registryAccess) -> dispatcher.register(

						ClientCommandManager.literal("set-discord-bot-token")
								.then(
										ClientCommandManager.argument("token", StringArgumentType.string())
												.executes(Afkchattodiscord::setToken)
								)
				)
		);
		ClientCommandRegistrationCallback.EVENT.register(
				(dispatcher, registryAccess) -> dispatcher.register(

						ClientCommandManager.literal("set-discord-server-id")
								.then(
										ClientCommandManager.argument("id", StringArgumentType.string())
												.executes(Afkchattodiscord::setGuild)
								)
				)
		);
		ClientCommandRegistrationCallback.EVENT.register(
				(dispatcher, registryAccess) -> dispatcher.register(

						ClientCommandManager.literal("set-discord-channel-id")
								.then(
										ClientCommandManager.argument("id", StringArgumentType.string())
												.executes(Afkchattodiscord::setChannel)
								)
				)
		);

		LOGGER.info("Afk-Chat-To-Discord is active...");
	}

	private static int setToken(CommandContext<FabricClientCommandSource> context){
		assert Minecraft.getInstance().player != null;
		String token = StringArgumentType.getString(context, "token");
		Config.BOT_TOKEN = token;
		Config.save();
		Component msg = Component.translatable("Set discord bot token to: \"%s\"", token)
				.withStyle(ChatFormatting.GREEN);
		Minecraft.getInstance().player.displayClientMessage(msg, false);
		return 1;
	}
	private static int setGuild(CommandContext<FabricClientCommandSource> context){
		assert Minecraft.getInstance().player != null;
		String id = StringArgumentType.getString(context, "id");
		Config.GUILD_ID = id;
		Config.save();
		Component msg = Component.translatable("Set discord bot server ID to: \"%s\"", id)
				.withStyle(ChatFormatting.GREEN);
		Minecraft.getInstance().player.displayClientMessage(msg, false);
		return 1;
	}
	private static int setChannel(CommandContext<FabricClientCommandSource> context){
		assert Minecraft.getInstance().player != null;
		String id = StringArgumentType.getString(context, "id");
		Config.CHANNEL_ID = id;
		Config.save();
		Component msg = Component.translatable("Set discord bot channel ID to: \"%s\"", id)
				.withStyle(ChatFormatting.GREEN);
		Minecraft.getInstance().player.displayClientMessage(msg, false);
		return 1;
	}
	public static void Disconnect() {
		Minecraft mc = Minecraft.getInstance();
		if (mc.getSingleplayerServer() != null) {
			// Execute on the client thread
			mc.execute(() -> {
				IntegratedServer server = mc.getSingleplayerServer();
				if (server != null) {
					server.stopServer(); // direct call, but only if you don’t care about in-progress lighting
				}
				mc.disconnect(null, false);
			});
		}
	}
}