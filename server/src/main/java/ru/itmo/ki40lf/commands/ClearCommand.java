package ru.itmo.ki40lf.commands;

import ru.itmo.ki40lf.serverPart.ServerEnvironment;
import ru.itmo.ki40lf.common.Request;
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
