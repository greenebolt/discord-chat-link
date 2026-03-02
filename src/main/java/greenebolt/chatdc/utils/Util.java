package greenebolt.chatdc.utils;

import greenebolt.chatdc.DiscordChatLink;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

public class Util {
    public static void Disconnect() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.getSingleplayerServer() != null) {
            mc.execute(() -> {
                mc.disconnectFromWorld(Component.translatable("Discord quit world"));
            });
            return;
        }
        if (mc.player == null) return;
        mc.disconnect(new TitleScreen(), false);
        DiscordChatLink.channel.sendMessage("Successfully quit world.").queue();
    }
    public static String getHealth() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return "NA: You are not connected to a world...";
        return "Player Health" + String.valueOf(mc.player.getHealth());
    }
    public static String getHunger() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return "NA: You are not connected to a world...";
        return "Player Hunger: " + String.valueOf(mc.player.getFoodData().getFoodLevel()) + "Saturation: " + String.valueOf(mc.player.getFoodData().getSaturationLevel());
    }
}
