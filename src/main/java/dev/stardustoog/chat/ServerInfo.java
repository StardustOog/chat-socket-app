package dev.stardustoog.chat;

import java.nio.file.Path;

public interface ServerInfo {
    int PORT = 11111;
    String ADDRESS = "localhost";
    Path PATH = Path.of("messages.txt");
    Path GROUP_ID_PATH = Path.of("groups.txt");
}
