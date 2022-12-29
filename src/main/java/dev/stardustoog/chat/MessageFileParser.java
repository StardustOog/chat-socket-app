package dev.stardustoog.chat;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MessageFileParser {


    public static List<String> getMessages(String id) {

        try {

            return Files.readAllLines(ServerInfo.PATH).stream()
                    .filter(line -> line.split(",")[0].equals(id))
                    .map(line -> line.split(",")[1])
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

}
