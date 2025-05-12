package ru.itmo.ki40lf.commands;

import ru.itmo.ki40lf.resources.Dragon;
import ru.itmo.ki40lf.serverPart.Request;
import ru.itmo.ki40lf.serverPart.ServerEnvironment;

import java.io.IOException;
import java.util.List;

public class SaveCommand extends Command {
    public SaveCommand() {
        super("save");
    }

    @Override
    public String execute(Request request) {
        List<Dragon> dragons = ServerEnvironment.getInstance().getCollectionManager().getDragons();
        ServerEnvironment.getInstance().getFileManager().writeToCSV(dragons);
        return "Драконы успешно записаны в файл!";
    }

    @Override
    public String getHelp() {
        return "save in file";
    }
}
