package ru.itmo.ki40lf.commands;

import ru.itmo.ki40lf.serverPart.Request;

public class ExitCommand extends Command {
    public ExitCommand() {
        super("exit");
    }

    @Override
    public String execute(Request request) {
        System.exit(0);
        return "Хорошего дня! ♡ (*^w^)";
    }

    @Override
    public String getHelp() {
        return "exit without save";
    }
}
