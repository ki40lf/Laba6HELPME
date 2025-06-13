package ru.itmo.ki40lf.commands;
import ru.itmo.ki40lf.common.Request;
import ru.itmo.ki40lf.common.Response;

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
    public boolean needsAuthorization() {
        return true;
    }
}
