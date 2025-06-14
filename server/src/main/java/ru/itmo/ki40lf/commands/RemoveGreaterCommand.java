package ru.itmo.ki40lf.commands;
import ru.itmo.ki40lf.common.Request;
import ru.itmo.ki40lf.resources.Dragon;
import ru.itmo.ki40lf.serverPart.ServerEnvironment;

import java.util.Iterator;
import java.util.List;

public class RemoveGreaterCommand extends Command {
    public RemoveGreaterCommand() {
        super("remove_greater");
    }

    @Override
    public String execute(Request request) {
        List<Dragon> dragons = ServerEnvironment.getInstance().getCollectionManager().getDragons();
        if (dragons.isEmpty()) {
            return "Коллекция пуста! Попробуйте другую команду";
        }

        Dragon referenceDragon = null;
        String name;

        try {
            name = request.getArgs()[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            return ("Вы не ввели имя дракона");
        }

        for (Dragon d : dragons) {
            if (d.getName().equals(name)) {
                referenceDragon = d;
                break;
            }
        }

        if (referenceDragon == null) {
            return ("Дракон с таким именем не найден! Попробуйте другую команду");
        }

        int initialSize = dragons.size();

        Iterator<Dragon> iterator = dragons.iterator();
        while (iterator.hasNext()) {
            Dragon dragon = iterator.next();
            if (
                    dragon.getAge() > referenceDragon.getAge() &&
                            dragon.getOwner() != null &&
                            dragon.getOwner().equals(request.getCredentials().getLogin())
            ) {
                iterator.remove();
            }
        }

        int removedCount = initialSize - dragons.size();
        return ("Удалено элементов: " + removedCount);
    }

    @Override
    public String getHelp() {
        return "delete all dragons from the collection, which are older than a given";
    }
}
