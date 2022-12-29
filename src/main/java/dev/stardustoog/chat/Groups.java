package dev.stardustoog.chat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Groups {
    private Map<String, ServerToClient> groupServers;
    private Set<String> groups;
    private static Groups grSingleton = null;

    private Groups() {
        groups = new HashSet<>();
        groupServers = new HashMap<>();
    }

    public static Groups getGroupsSingleton() {
        if(grSingleton == null) {
            grSingleton = new Groups();
        }
        return grSingleton;
    }

    public void addNewServer(String id) {
        try {
            Files.write(ServerInfo.GROUP_ID_PATH, (id + "\n").getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            groupServers.put(id, new ServerToClient(id));

            groups.add(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            groups.addAll(Files.readAllLines(ServerInfo.GROUP_ID_PATH));
            Files.readAllLines(ServerInfo.GROUP_ID_PATH).stream()
                    .forEach(groupId -> {
                        groupServers.put(groupId, new ServerToClient(groupId));
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Set<String> getGroups() {
        return groups;
    }

    public ServerToClient getGroupServers(String id) {
        return groupServers.get(id);
    }
}
