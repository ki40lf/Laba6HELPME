package ru.itmo.ki40lf.commands;
import ru.itmo.ki40lf.common.Request;
import ru.itmo.ki40lf.resources.Dragon;
import ru.itmo.ki40lf.resources.IdGen;
import ru.itmo.ki40lf.serverPart.ServerEnvironment;

import java.util.List;

public class AddCommand extends Command {
    public AddCommand() {
        super("add");
    }


    @Override
    public String execute(Request request) {
        Dragon dragon1 = request.getDragon();
        dragon1.setId(IdGen.regenId());
        dragon1.setOwner(request.getCredentials().getLogin());
        ServerEnvironment.getInstance().getCollectionManager().getDragons().add(dragon1);
        return "Дракон успешно добавлен";
    }

    @Override
    public String getHelp() {
        return "add new Dragon";
    }
}
