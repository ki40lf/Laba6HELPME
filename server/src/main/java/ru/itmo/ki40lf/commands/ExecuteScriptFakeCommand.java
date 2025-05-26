package ru.itmo.ki40lf.commands;

import ru.itmo.ki40lf.common.Request;

public class ExecuteScriptFakeCommand extends Command {
    public ExecuteScriptFakeCommand() {
        super("ExecuteScriptFakeCommand");
    }

    @Override
    public String execute(Request request) {
        return "Execute_script завершен";
    }

    @Override
    public String getHelp() {
        return "consider and execute the script from the specified file. The script contains commands in the same form in which they are introduced by the user in interactive mode";
    }
}
