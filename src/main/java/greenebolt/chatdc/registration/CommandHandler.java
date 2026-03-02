package greenebolt.chatdc.registration;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import greenebolt.chatdc.Config;
import greenebolt.chatdc.utils.Util;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class CommandHandler {
    public static void register() {

        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommandManager.literal("set-discord-bot-token")
                                .then(
                                        ClientCommandManager.argument("token", StringArgumentType.string())
                                                .executes(CommandHandler::setToken)
                                )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommandManager.literal("set-discord-server-id")
                                .then(
                                        ClientCommandManager.argument("id", StringArgumentType.string())
                                                .executes(CommandHandler::setGuild)
                                )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommandManager.literal("set-discord-channel-id")
                                .then(
                                        ClientCommandManager.argument("id", StringArgumentType.string())
                                                .executes(CommandHandler::setChannel)
                                )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommandManager.literal("start-discord-chat-link")
                                .executes(CommandHandler::start)
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommandManager.literal("stop-discord-chat-link")
                                .executes(CommandHandler::stop)
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommandManager.literal("quit")
                                .executes(CommandHandler::quit)

                )
        );
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
    private static int quit(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        Util.Disconnect();
        return 1;
    }
    private static int start(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        Util.Start();
        return 1;
    }
    private static int stop(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        Util.Stop();
        return 1;
    }
}
