package ru.itmo.ki40lf.commands;


import ru.itmo.ki40lf.serverPart.Request;

import java.io.InputStream;
import java.io.PrintStream;

public abstract class Command {
    private final String name;

    protected Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract String execute(Request request);

    public abstract String getHelp();
}
