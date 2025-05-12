package ru.itmo.ki40lf.commands;
import ru.itmo.ki40lf.common.Request;
import ru.itmo.ki40lf.resources.Dragon;
import ru.itmo.ki40lf.serverPart.ServerEnvironment;

import java.util.List;

public class InfoCommand extends Command {

    public InfoCommand() {
        super("info");
    }

    @Override
    public String execute(Request request) {
        List<Dragon> dragons = ServerEnvironment.getInstance().getCollectionManager().getDragons();
        if (dragons.isEmpty()) {
            return "Коллекция пуста! Попробуйте другую команду";
        }
        String info = "Информация о коллекции:\n" +
                "Тип коллекции: " + dragons.getClass().getSimpleName() + "\n" +
                "Дата инициализации: " + ServerEnvironment.getInstance().getCollectionManager().getInitializationTime() + "\n" +
                "Количество элементов: " + dragons.size() + "\n";
        return info;
    }

    @Override
    public String getHelp() {
        return "Info about collection";
    }
}
