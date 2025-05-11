package ru.itmo.ki40lf.commands;

import ru.itmo.ki40lf.clientPart.Request;
import ru.itmo.ki40lf.resources.Dragon;
import ru.itmo.ki40lf.serverPart.ServerEnvironment;

import java.util.List;
import java.util.stream.Collectors;

public class ShowCommand extends Command {
    public ShowCommand() {
        super("show");
    }

    @Override
    public String execute(Request request) {
        List<Dragon> dragons = ServerEnvironment.getInstance().getCollectionManager().getDragons();
        if (dragons.isEmpty()) {
            return "Collection is empty";
        }
        StringBuilder text = new StringBuilder();
        if (request.getMessage().split(" ").length == 1) {
            text.append(dragons.entrySet().stream()
                    .map(entry -> entry.getKey() + ": " + entry.getValue())
                    .collect(Collectors.joining("\n")));
        }
        return "";
    }

    @Override
    public String getHelp() {
        return "show elements of collection Dragons";
    }
}
