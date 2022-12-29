package dev.stardustoog.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public class User {
    private final String name;
    private BufferedWriter writer;
    private BufferedReader reader;

    public User(String name, BufferedWriter writer, BufferedReader reader) {
        this.name = name;
        this.writer = writer;
        this.reader = reader;
    }

    public String getName() {
        return name;
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public BufferedReader getReader() {
        return reader;
    }

    @Override
    public String toString() {
        return name;
    }

}
