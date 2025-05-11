package ru.itmo.ki40lf.commands;

import ru.itmo.ki40lf.clientPart.Request;
import ru.itmo.ki40lf.serverPart.ServerEnvironment;

public class ClearCommand extends Command {
    public ClearCommand() {
        super("clear");
    }

    @Override
    public String execute(Request request) {
        ServerEnvironment.getInstance().getCollectionManager().getDragons().clear();
        return "Collection is cleared!";
    }

    @Override
    public String getHelp() {
        return "";
    }
}
