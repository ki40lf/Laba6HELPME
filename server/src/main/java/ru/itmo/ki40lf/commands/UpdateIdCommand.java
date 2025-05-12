package ru.itmo.ki40lf.commands;
import ru.itmo.ki40lf.common.Request;
public class UpdateIdCommand extends Command {
    public UpdateIdCommand() {
        super("update_id");
    }

    @Override
    public String execute(Request request) {
        return "";
    }

    @Override
    public String getHelp() {
        return "update elements of Dragon on Id";
    }
}
