package ru.itmo.ki40lf.commands;
import ru.itmo.ki40lf.common.Request;
import ru.itmo.ki40lf.resources.Dragon;
import ru.itmo.ki40lf.resources.IdGen;
import ru.itmo.ki40lf.serverPart.ServerEnvironment;

import java.util.List;

public class RemoveFirstCommand extends Command {
    public RemoveFirstCommand() {
        super("removefirst");
    }

    @Override
    public String execute(Request request) {
        List<Dragon> dragons = ServerEnvironment.getInstance().getCollectionManager().getDragons();
        if (!dragons.isEmpty()) {
            Dragon firstDragon = dragons.get(0);
            if (!firstDragon.getOwner().equals(request.getCredentials().getLogin())) {
                return "Ошибка: первый дракон не принадлежит вам.";
            }
            dragons.remove(0);
            IdGen.releaseId(firstDragon.getId());
            return ("Первый дракон удален! Его ID:  " + firstDragon.getId());
        } else {
            return "В коллекции никого нет! Попробуйте другую команду";
        }
    }

    @Override
    public String getHelp() {
        return "Remove first element of collection";
    }
}
