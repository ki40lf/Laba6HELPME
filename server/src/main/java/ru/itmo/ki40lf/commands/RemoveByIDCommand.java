package ru.itmo.ki40lf.commands;
import ru.itmo.ki40lf.common.Request;
import ru.itmo.ki40lf.resources.Dragon;
import ru.itmo.ki40lf.resources.IdGen;
import ru.itmo.ki40lf.serverPart.ServerEnvironment;

import java.util.Iterator;
import java.util.List;

public class RemoveByIDCommand extends Command {
    public RemoveByIDCommand() {
        super("remove_by_id");
    }


    @Override
    public String execute(Request request) {
        List<Dragon> dragons = ServerEnvironment.getInstance().getCollectionManager().getDragons();
        if (dragons.isEmpty()) {
            return "Коллекция пуста! Попробуйте другую команду";
        }

        int id;

        try {
            id = Integer.parseInt(request.getArgs()[0]);
        } catch (NumberFormatException e) {
            return "Ошибка: ID должен быть числом.";
        } catch (ArrayIndexOutOfBoundsException e) {
            return "Вы не ввели айди дракона";
        }

        Iterator<Dragon> iterator = dragons.iterator();
        boolean removed = false;

        while (iterator.hasNext()) {Dragon dragon = iterator.next();
            if (dragon.getId() == id &&
                    dragon.getOwner() != null) {
                if (!dragon.getOwner().equals(request.getCredentials().getLogin())) {
                    return "Ошибка: этот дракон не принадлежит вам.";
                }
                iterator.remove();
                IdGen.releaseId(dragon.getId());
                removed = true;
                break;
            }
        }

        if (removed) {
            return ("Дракон с ID " + id + " успешно удалён.");
        } else {
            return ("Дракон с таким ID не найден. Попробуйте другую команду ");
        }
    }

    @Override
    public String getHelp() {
        return "delete the element from the collection according to its ID";
    }
}
