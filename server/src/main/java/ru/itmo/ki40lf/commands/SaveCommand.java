package ru.itmo.ki40lf.commands;

import ru.itmo.ki40lf.serverPart.Request;

public class SaveCommand extends Command {
    public SaveCommand() {
        super("save");
    }

    @Override
    public String execute(Request request) {
        return "";
    }

    @Override
    public String getHelp() {
        return "save in file";
    }
}
