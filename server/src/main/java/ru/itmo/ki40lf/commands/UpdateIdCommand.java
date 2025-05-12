package ru.itmo.ki40lf.commands;
import ru.itmo.ki40lf.common.Request;
import ru.itmo.ki40lf.resources.Dragon;
import ru.itmo.ki40lf.resources.IdGen;
import ru.itmo.ki40lf.serverPart.ServerEnvironment;

import java.util.List;

public class UpdateIdCommand extends Command {
    public UpdateIdCommand() {
        super("update_id");
    }

    @Override
    public String execute(Request request) {
        Dragon dragon1 = request.getDragon();
        String arg = request.getArgs()[0];

        List<Dragon> dragons = ServerEnvironment.getInstance().getCollectionManager().getDragons();
        int id;

        try {
            id = Integer.parseInt(arg); // Читаем ID
        } catch (NumberFormatException e) {
            return ("Ошибка: ID должен быть числом.");
        } catch (ArrayIndexOutOfBoundsException e) {
            return ("Вы не ввели айди дракона");
        }


        // Проверяем, есть ли дракон с таким ID
        boolean removed = dragons.removeIf(dragon -> dragon.getId() == id);

        if (!removed) {
            return ("Ошибка: Дракон с ID " + id + " не найден.");
        } IdGen.releaseId(id);

        Dragon newDragon = dragon1; // Создаём нового дракона

        dragons.add(newDragon); // Добавляем нового дракона в коллекцию

        return "Дракон успешно обновлен!";
    }

    @Override
    public String getHelp() {
        return "update elements of Dragon on Id";
    }
}
