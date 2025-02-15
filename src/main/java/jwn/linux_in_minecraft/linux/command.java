package jwn.linux_in_minecraft.linux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class command {
    public static boolean cmd(String message) {
        String[] commands = message.split(" ");
        System.out.println(Arrays.toString(commands));
        if (message.equals("exit")) {
            return false;
        } else if (commands[0].equals("cd")) {
            cd(commands);
            return true;
        } else if (commands[0].equals("ls")) {
            ls(commands);
            return true;
        } else if (message.equals("pwd")) {
            pwd();
        }
        return true;
    }

    private static void ls(String[] commands) {
        if (commands.length == 1) {
            try {
                Files.list(LinuxChatScreen.CURRENT_PATH).forEach(path -> {
                    LinuxChatScreen.sendChatMessage("." + LinuxChatScreen.CURRENT_PATH.relativize(path) + (Files.isDirectory(path) ? "/" : ""), false);
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void cd(String[] commands) {
        if (commands.length == 2) {
            Path newPath = LinuxChatScreen.CURRENT_PATH.resolve(commands[1]);
            if (Files.exists(newPath) && Files.isDirectory(newPath)) {
                LinuxChatScreen.CURRENT_PATH = newPath;
            }
        }
    }

    private static void pwd() {
        LinuxChatScreen.sendChatMessage(LinuxChatScreen.CURRENT_PATH.toAbsolutePath().normalize().toString(), false);
    }
}
