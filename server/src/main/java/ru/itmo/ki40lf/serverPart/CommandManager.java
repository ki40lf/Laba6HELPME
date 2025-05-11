package ru.itmo.ki40lf.serverPart;

import ru.itmo.ki40lf.clientPart.Request;
import ru.itmo.ki40lf.commands.Command;

import java.util.HashMap;

public class CommandManager {
    public static HashMap<String, Command> commandList;

    public CommandManager() {
        commandList = new HashMap<>();

        //commandList.put("clear", new Clear());

    }

    public HashMap<String, Command> getCommandList() {
        return commandList;
    }

    public String startExecuting(Request request) {
        String commandName = request.getMessage();
        if (commandList.containsKey(commandName)) {
            Command command = commandList.get(commandName);
            String message = command.execute(request);
            return message;
        } else {
            return "Command doesn't exist";
        }
    }
}
