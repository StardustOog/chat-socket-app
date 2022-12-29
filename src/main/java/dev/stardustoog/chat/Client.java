package dev.stardustoog.chat;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket clientSocket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public void clientStart() {
        Scanner sc = new Scanner(System.in);
        clientSocket = getServerConnection();
        getReaderWriter();
        System.out.println(getMessage());
        writeMessage(sc.nextLine());
        String msg = getMessage();
        if (msg != null) {
            System.out.println(msg);
        } else {
            return;
        }
        writeMessage(sc.nextLine());
        startReading();
        startWriting();
    }

    private void startReading() {
        new Thread(() -> {

            String msg = null;
            if(clientSocket.isConnected()) {
                msg = getMessage();
            }

            while (msg != null && clientSocket.isConnected()) {
                System.out.println(msg);
                msg = getMessage();
            }

        }).start();
    }

    private void closeConnection() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startWriting() {
        Scanner sc = new Scanner(System.in);

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();
            while (message != null && message.length() != 0) {
                writeMessage(message);
                message = scanner.nextLine();
            }
            scanner.close();
            closeConnection();
        }).start();
    }

    private void writeMessage(String message) {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            closeConnection();
            e.printStackTrace();
        }
    }


    private String getMessage() {
        try {
            if (clientSocket.isConnected()) {
                return reader.readLine();
            } else return null;
        } catch (IOException e) {
            closeConnection();
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    private void getReaderWriter() {
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            closeConnection();
            e.printStackTrace();
        }
    }

    private Socket getServerConnection() {
        try {
            return new Socket(ServerInfo.ADDRESS, ServerInfo.PORT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.clientStart();
    }
}
