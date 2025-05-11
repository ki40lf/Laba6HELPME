package ru.itmo.ki40lf.commands;

import ru.itmo.ki40lf.serverPart.Request;

public class InfoCommand extends Command {

    protected InfoCommand() {
        super("info");
    }

    @Override
    public String execute(Request request) {
        return "";
    }

    @Override
    public String getHelp() {
        return "Info about collection";
    }
}
