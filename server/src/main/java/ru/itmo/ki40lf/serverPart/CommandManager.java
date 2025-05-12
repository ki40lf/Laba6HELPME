package ru.itmo.ki40lf.serverPart;
import ru.itmo.ki40lf.common.Request;
import ru.itmo.ki40lf.common.Response;

import ru.itmo.ki40lf.commands.*;

import java.util.HashMap;

public class CommandManager {
    public static HashMap<String, Command> commandList;

    public CommandManager() {
        commandList = new HashMap<>();
        commandList.put("add", new AddCommand());
        commandList.put("clear", new ClearCommand());
        commandList.put("help", new HelpCommand());
        commandList.put("exit", new ExitCommand());
        commandList.put("group_counting_by_age", new GroupCountingByAgeCommand());
        commandList.put("head", new HeadCommand());
        commandList.put("info", new InfoCommand());
        commandList.put("remove_by_character", new RemoveByCharacterCommand());
        commandList.put("remove_by_id", new RemoveByIDCommand());
        commandList.put("remove_first", new RemoveFirstCommand());
        commandList.put("remove_greater", new RemoveGreaterCommand());
        commandList.put("save", new SaveCommand());
        commandList.put("show", new ShowCommand());
        commandList.put("update_id", new UpdateIdCommand());
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
