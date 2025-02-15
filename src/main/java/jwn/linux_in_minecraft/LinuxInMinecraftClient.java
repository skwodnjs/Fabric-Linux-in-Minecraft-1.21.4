package jwn.linux_in_minecraft;

import jwn.linux_in_minecraft.linux.LinuxChatScreen;
import net.fabricmc.api.ClientModInitializer;

public class LinuxInMinecraftClient implements ClientModInitializer {
    public static final String MOD_ID = "linux_in_minecraft";

    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
        LinuxChatScreen.register();
    }
}
