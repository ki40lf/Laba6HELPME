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

        List<Dragon> dragons = ServerEnvironment.getInstance().getCollectionManager().getDragons();
        int id;

        try {
            String arg = request.getArgs()[0];
            id = Integer.parseInt(arg); // Читаем ID
        } catch (NumberFormatException e) {
            return ("Ошибка: ID должен быть числом.");
        } catch (ArrayIndexOutOfBoundsException e) {
            return ("Вы не ввели айди дракона");
        }


        // Проверяем, есть ли дракон с таким ID
        boolean removed = dragons.removeIf(dragon ->
                dragon.getId() == id &&
                        dragon.getOwner() != null &&
                        dragon.getOwner().equals(request.getCredentials().getLogin())
        );

        if (!removed) {
            return "Ошибка: Дракон с таким ID не найден или он вам не принадлежит.";
        } IdGen.releaseId(id);

        dragon1.setId(id);
        dragon1.setOwner(request.getCredentials().getLogin()); // 🔥 вот здесь

        dragons.add(dragon1);
        return "Дракон с ID " + id + " обновлён.";
    }

    @Override
    public String getHelp() {
        return "update elements of Dragon on Id";
    }
}
