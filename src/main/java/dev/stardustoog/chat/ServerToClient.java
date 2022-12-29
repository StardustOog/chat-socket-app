package dev.stardustoog.chat;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ServerToClient {
    private final List<User> chatMembers = new ArrayList<>();
    private final String groupId;

    public ServerToClient(String groupId) {
        this.groupId = groupId;
    }

    public void addNewUser(String name, Socket socket) {
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            User user = new User(name, writer,
                    reader);
            chatMembers.add(user);
            writeOldMessages(writer);

            sendMessages(socket, user);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessages(Socket socket, User user) {
        Thread clientListener = new Thread(() -> {


            try {
                String msg = user.getReader().readLine();
                while (msg != null) {
                    sendMsgToUsers(user, user.getName() + ": " + msg);
                    msg = user.getReader().readLine();
                }
                sendMsgToUsers(user, "Server Bot: " + user.getName() + " has disconnected!");
                socket.close();
                chatMembers.remove(user);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        clientListener.start();
    }

    private void sendMsgToUsers(User user, String msg) {
        try {
            Files.write(ServerInfo.PATH, (groupId + "," + msg + "\n").getBytes(),
                    StandardOpenOption.APPEND);

            chatMembers.stream()
                    .filter(u -> user != u)
                    .forEach(u -> {

                        try {
                            u.getWriter().write(msg);
                            u.getWriter().newLine();
                            u.getWriter().flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeOldMessages(BufferedWriter writer) {
        List<String> messages = MessageFileParser.getMessages(groupId);
        messages.stream()
                .forEach(msg -> {
                    try {
                        writer.write(msg);
                        writer.newLine();
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }


}
