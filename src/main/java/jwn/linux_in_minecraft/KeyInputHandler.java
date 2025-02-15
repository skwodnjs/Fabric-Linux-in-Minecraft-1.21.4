package jwn.linux_in_minecraft;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import static jwn.linux_in_minecraft.LinuxInMinecraftClient.MOD_ID;
import static jwn.linux_in_minecraft.linux.LinuxChatScreen.startListeningForLinux;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_CONTROL = "key.category." + MOD_ID + ".control";
    public static final String KEY_LINUX_MODE_TOGGLE = "key." + MOD_ID + ".linux_mode_toggle";

    public static KeyBinding LinuxModeToggleKey;

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (LinuxModeToggleKey.wasPressed()) {
                startListeningForLinux();
            }
        });
    }

    public static void register() {
        LinuxModeToggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_LINUX_MODE_TOGGLE,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F4,
                KEY_CATEGORY_CONTROL
        ));

        registerKeyInputs();
    }
}
