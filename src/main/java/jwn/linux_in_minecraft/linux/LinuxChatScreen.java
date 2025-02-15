package jwn.linux_in_minecraft.linux;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

public class LinuxChatScreen {
    public static final AtomicBoolean linuxMode = new AtomicBoolean(false);

    public static final Path ROOT = MinecraftClient.getInstance().runDirectory.toPath();
    public static Path CURRENT_PATH;
    private static String PREFIX = "";

    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static boolean reChat = false;

    public static void updatePrefix() {
        ClientPlayerEntity player = client.player;
        if (CURRENT_PATH != null) {
            Path relativize = ROOT.relativize(CURRENT_PATH);
            PREFIX = "["
                    + (player != null ? player.getName().getString() : "user")
                    + "@Minecraft ~"
                    + (relativize.toString().isEmpty() ? "" : "\\" + relativize)
                    + "]$ ";
        }
    }

    public static String getPREFIX() {
        return PREFIX;
    }

    public static void startListeningForLinux() {
        linuxMode.set(true);

        CURRENT_PATH = client.runDirectory.toPath();
        updatePrefix();
        client.setScreen(new ChatScreen(getPREFIX()));

        sendChatMessage("Linux mode: " + linuxMode.get(), true);
    }

    public static void sendChatMessage(String message, boolean overlay) {
        ClientPlayerEntity player = client.player;
        if (player != null) {
            player.sendMessage(Text.literal(message), overlay);
        }
    }

    public static void register() {
        ClientSendMessageEvents.ALLOW_CHAT.register((message) -> {
            if (linuxMode.get()) {
                sendChatMessage(message, false);
                reChat = command.cmd(message.replace(getPREFIX(), "").stripTrailing());
                return false;
            }
            return true;
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (reChat) {
                updatePrefix();
                client.setScreen(new ChatScreen(getPREFIX()));
                reChat = false;
            }
            if (linuxMode.get() && !(client.currentScreen instanceof ChatScreen)) {
                linuxMode.set(false);
                sendChatMessage("Linux mode: "+ linuxMode.get(), true);
            }
        });
    }
}
