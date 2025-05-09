package ru.itmo.ki40lf.clientPart;

import java.io.Serializable;


public class CommandRequest implements Serializable {
    private final String commandName;
    private final String[] args;
    private final Object objArg; //если команда требует объект из коллекции

    public CommandRequest(String commandName, String[] args, Object objArg) {
        this.commandName = commandName;
        this.args = args;
        this.objArg = objArg;
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getArgs() {
        return args;
    }

    public Object getObjArg() {
        return objArg;
    }
}
