package ru.itmo.ki40lf.commands;
import ru.itmo.ki40lf.common.Request;
import ru.itmo.ki40lf.resources.Dragon;
import ru.itmo.ki40lf.resources.DragonCharacter;
import ru.itmo.ki40lf.resources.IdGen;
import ru.itmo.ki40lf.serverPart.ServerEnvironment;

import java.util.Iterator;
import java.util.List;

public class RemoveByCharacterCommand extends Command {
    public RemoveByCharacterCommand() {
        super("remove_by_character");
    }

    @Override
    public String execute(Request request) {
        List<Dragon> dragons = ServerEnvironment.getInstance().getCollectionManager().getDragons();
        if (dragons.isEmpty()) {
            return "Коллекция пуста! Попробуйте другую команду";
        }

        if (request.getArgs().length == 0){
            return "Вы не ввели характер дракона";
        }

        DragonCharacter finalCharacter;
        try {
            finalCharacter = DragonCharacter.valueOf(request.getArgs()[0]);
        } catch (IllegalArgumentException e) {
            return "Такого характера нет, попробуйте снова";
        }

        boolean removed = false;
        String currentUser = request.getCredentials().getLogin();

        // Удаляем только драконов с нужным характером И текущим владельцем
//        boolean removed = dragons.removeIf(dragon ->
//                dragon.getCharacter() == finalCharacter &&
//                        dragon.getOwner() != null &&
//                        dragon.getOwner().equals(currentUser)
//        );

        Iterator<Dragon> iterator = dragons.iterator();
        while (iterator.hasNext()) {
            Dragon dragon = iterator.next();
            if (
                    dragon.getCharacter().equals(finalCharacter) &&
                            dragon.getOwner() != null &&
                            dragon.getOwner().equals(currentUser)
            ) {
                iterator.remove();
                IdGen.releaseId(dragon.getId());
                removed = true;
            }
        }

        if (removed) {
            return "Ваши драконы с характером " + finalCharacter + " успешно удалены.";
        } else {
            return "Драконов с таким характером, принадлежащих вам, не найдено.";
        }
    }

    @Override
    public String getHelp() {
        return "remove all by character";
    }
}
