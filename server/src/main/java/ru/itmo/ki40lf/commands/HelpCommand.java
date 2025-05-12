package ru.itmo.ki40lf.commands;
import ru.itmo.ki40lf.common.Request;
import ru.itmo.ki40lf.serverPart.ServerEnvironment;

import java.util.HashMap;
import java.util.stream.Collectors;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    public String execute(Request request) {
        HashMap<String, Command> commands = ServerEnvironment.getInstance().getCommandManager().getCommandList();
        String commandsInfo = "Список доступных команд:\n" + commands.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue().getHelp())
                .collect(Collectors.joining("\n"));
        return commandsInfo;
    }

    @Override
    public String getHelp() {
        return "This is a help command";
    }
}
