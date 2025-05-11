package ru.itmo.ki40lf.commands;

import ru.itmo.ki40lf.resources.Dragon;
import ru.itmo.ki40lf.serverPart.Request;
import ru.itmo.ki40lf.serverPart.ServerEnvironment;

import java.util.List;

public class HeadCommand extends Command {
    public HeadCommand() {
        super("head");
    }

    @Override
    public String execute(Request request) {
        List<Dragon> dragons = ServerEnvironment.getInstance().getCollectionManager().getDragons();
        if (dragons.isEmpty()) {
            return "Коллекция пуста! Попробуйте другую команду";
        }
        String result ="Первый элемент коллекции: " + dragons.get(0).toString();
        return result;
    }

    @Override
    public String getHelp() {
        return "bring the first element of the dragon collection";
    }
}
