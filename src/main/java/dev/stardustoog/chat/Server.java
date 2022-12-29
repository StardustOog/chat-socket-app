package dev.stardustoog.chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class Server {

    private final ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        filesCreateAndLoad();
        this.serverSocket = serverSocket;
    }

    private void filesCreateAndLoad() {
        try {

            if (!Files.exists(ServerInfo.PATH)) {
                Files.createFile(ServerInfo.PATH);
            }

            if (!Files.exists(ServerInfo.GROUP_ID_PATH)) {
                Files.createFile(ServerInfo.GROUP_ID_PATH);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Groups.getGroupsSingleton().load();
    }

    public void serverStart() {
        while (true) {
            try {
                Socket client = serverSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                writer.write("Please, enter your nickname: ");
                writer.newLine();
                writer.flush();
                String name = reader.readLine();
                writer.write("Please, enter one of this: " + Groups.getGroupsSingleton().getGroups()
                        + " or simply create your chatroom by just entering new group id");
                writer.newLine();
                writer.flush();
                String groupId = reader.readLine();
                getClientConnected(client, name, groupId);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void getClientConnected(Socket clientSocket, String name, String chatId) {

        if (!Groups.getGroupsSingleton().getGroups().contains(chatId)) {
            Groups.getGroupsSingleton().addNewServer(chatId);
        }

        ServerToClient server = Groups.getGroupsSingleton().getGroupServers(chatId);
        server.addNewUser(name, clientSocket);
    }

    public static void main(String[] args) {
        try {
            Server server = new Server(new ServerSocket(ServerInfo.PORT));
            server.serverStart();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
