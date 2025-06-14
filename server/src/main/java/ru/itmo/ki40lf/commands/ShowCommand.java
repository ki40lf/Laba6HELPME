package ru.itmo.ki40lf.commands;
import ru.itmo.ki40lf.common.Request;
import ru.itmo.ki40lf.resources.Dragon;
import ru.itmo.ki40lf.serverPart.ServerEnvironment;

import java.util.List;
import java.util.stream.Collectors;

public class ShowCommand extends Command {
    public ShowCommand() {
        super("show");
    }

    @Override
    public String execute(Request request) {
        List<Dragon> dragons = ServerEnvironment.getInstance().getCollectionManager().getDragons();
        if (dragons.isEmpty()) {
            return "Коллекция пуста";
        }

        String currentUser = request.getCredentials().getLogin();

        // Фильтруем драконов по владельцу
        String result = dragons.stream()
                .filter(dragon -> currentUser.equals(dragon.getOwner()))
                .map(Dragon::toString)
                .collect(Collectors.joining("\n"));

        return result.isEmpty() ? "У вас нет драконов." : result;
    }

    @Override
    public String getHelp() {
        return "show elements of collection Dragons";
    }
}
