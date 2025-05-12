package ru.itmo.ki40lf.commands;
import ru.itmo.ki40lf.common.Request;
public class AddCommand extends Command {
    public AddCommand() {
        super("add");
    }


    @Override
    public String execute(Request request) {
        //Пока что хуй знает как сделать
        return "";
    }

    @Override
    public String getHelp() {
        return "add new Dragon";
    }
}
