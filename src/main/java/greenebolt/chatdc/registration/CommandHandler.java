package greenebolt.chatdc.registration;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import greenebolt.chatdc.Config;
import greenebolt.chatdc.utils.Util;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class CommandHandler {
    public static void register() {

        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommands.literal("set-discord-bot-token")
                                .then(
                                        ClientCommands.argument("token", StringArgumentType.string())
                                                .executes(CommandHandler::setToken)
                                )
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommands.literal("start-discord-chat-link")
                                .executes(CommandHandler::start)
                )
        );
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> dispatcher.register(

                        ClientCommands.literal("stop-discord-chat-link")
                                .executes(CommandHandler::stop)
                )
        );
    }

    private static int setToken(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        String token = StringArgumentType.getString(context, "token");
        Config.BOT_TOKEN = token;
        Config.save();
        Component msg = Component.translatable("Set discord bot token to: \"%s\". \n\nRun /start-discord-chat-link", token)
                .withStyle(ChatFormatting.GREEN);
        Minecraft.getInstance().player.sendSystemMessage(msg);
        return 1;
    }
    private static int start(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        Util.Start();
        return 1;
    }
    private static int stop(CommandContext<FabricClientCommandSource> context){
        assert Minecraft.getInstance().player != null;
        Util.Stop("CommandHandler");
        return 1;
    }
}
