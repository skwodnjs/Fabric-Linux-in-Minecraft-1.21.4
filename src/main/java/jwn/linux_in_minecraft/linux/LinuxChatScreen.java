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
    public static String PREFIX = "";

    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static boolean reChat = false;

    public static void startListeningForLinux() {
        linuxMode.set(!linuxMode.get());
        if (linuxMode.get()) {
            CURRENT_PATH = client.runDirectory.toPath();
            Path relativize = ROOT.relativize(CURRENT_PATH);
            client.setScreen(new ChatScreen("~%s$: ".formatted(relativize)));
        } else {
            client.setScreen(null);
        }
        sendChatMessage("Linux mode: "+ linuxMode.get(), true);
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
                reChat = command.cmd(message.replace("~%s$ ".formatted(ROOT.relativize(CURRENT_PATH)), "").stripTrailing());
                return false;
            }
            return true;
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (reChat) {
                client.setScreen(new ChatScreen("~%s$ ".formatted(ROOT.relativize(CURRENT_PATH))));
                reChat = false;
            }
            if (linuxMode.get() && !(client.currentScreen instanceof ChatScreen)) {
                linuxMode.set(false);
                sendChatMessage("Linux mode: "+ linuxMode.get(), true);
            }
        });
    }
}
