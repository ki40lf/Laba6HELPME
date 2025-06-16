package ru.itmo.ki40lf.commands;

import ru.itmo.ki40lf.resources.Dragon;
import ru.itmo.ki40lf.resources.IdGen;
import ru.itmo.ki40lf.serverPart.ServerEnvironment;
import ru.itmo.ki40lf.common.Request;

import java.util.Iterator;
import java.util.List;

public class ClearCommand extends Command {
    public ClearCommand() {
        super("clear");
    }

    @Override
    public String execute(Request request) {
        //ServerEnvironment.getInstance().getCollectionManager().getDragons().clear();
        List<Dragon> dragons = ServerEnvironment.getInstance().getCollectionManager().getDragons();

        Iterator<Dragon> iterator = dragons.iterator();
        while (iterator.hasNext()) {
            Dragon dragon = iterator.next();
            if (
                    dragon.getOwner() != null &&
                            dragon.getOwner().equals(request.getCredentials().getLogin())
            ) {
                iterator.remove();
                IdGen.releaseId(dragon.getId());
            }
        }

        return "Ваши драконы из коллекции удалены!";
    }

    @Override
    public String getHelp() {
        return "clean the dragon collection";
    }
}
