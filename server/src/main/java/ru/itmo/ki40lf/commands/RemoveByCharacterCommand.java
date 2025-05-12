package ru.itmo.ki40lf.commands;
import ru.itmo.ki40lf.common.Request;
import ru.itmo.ki40lf.resources.Dragon;
import ru.itmo.ki40lf.resources.DragonCharacter;
import ru.itmo.ki40lf.serverPart.ServerEnvironment;

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

        DragonCharacter finalCharacter;
        while (true) {
            try {
                finalCharacter = DragonCharacter.valueOf(request.getArgs()[0]);
                break;
            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка: Неверный ввод. Попробуйте снова.");
            }
        }

        DragonCharacter finalCharacter1 = finalCharacter;
        boolean removed = dragons.removeIf(dragon -> dragon.getCharacter() == finalCharacter1);

        if (removed) {
            return "Драконы с характером " + finalCharacter.toString() + " успешно удалены.";
        } else {
            return "Драконов с характером " + finalCharacter.toString() + " не найдено.";
        }
    }

    @Override
    public String getHelp() {
        return "remove all by character";
    }
}
