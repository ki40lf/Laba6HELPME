package ru.itmo.ki40lf.commands;

import ru.itmo.ki40lf.resources.Dragon;
import ru.itmo.ki40lf.serverPart.Request;
import ru.itmo.ki40lf.serverPart.ServerEnvironment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupCountingByAgeCommand extends Command {
    public GroupCountingByAgeCommand() {
        super("group_counting_by_age");
    }

    @Override
    public String execute(Request request) {
        List<Dragon> dragons = ServerEnvironment.getInstance().getCollectionManager().getDragons();
        if (dragons.isEmpty()) {
            return "Коллекция пуста! Попробуйте другую команду";
        }

        String result = "Группировка по возрасту: " +
                dragons.stream()
                        .collect(Collectors.groupingBy(Dragon::getAge, Collectors.counting()))
                        .entrySet().stream()
                        .map(entry -> "Возраст " + entry.getKey() + ": " + entry.getValue() + " драконов")
                        .collect(Collectors.joining(", "));
        return result;
    }

    @Override
    public String getHelp() {
        return "group elements of the collection by the age, derive the number of elements in each group";
    }
}
