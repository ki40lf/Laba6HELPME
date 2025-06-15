package ru.itmo.ki40lf.commands;

import ru.itmo.ki40lf.common.Request;
import ru.itmo.ki40lf.common.Response;
import ru.itmo.ki40lf.userManager.UserManager;

public class LoginCommand extends Command {
    private final UserManager userManager;

    public LoginCommand(UserManager userManager) {
        super("login");
        this.userManager = userManager;
    }

    @Override
    public String execute(Request request) {
        String login = request.getCredentials().getLogin();
        String password = request.getCredentials().getPassword();

        if (userManager.isLoggedIn(login) && userManager.isPasswordUsed(password)) {
            return "Вы уже вошли в систему";
        }
        if (userManager.authenticate(login, password)) {
            userManager.setLoggedInUser(login);
            userManager.setPasswordUsed(password);
            return "Успешный вход в систему как: " + login;
        } else {
            userManager.removeLoggedInUser(login);
            userManager.removePasswordUsed(password);
            return "Неверный логин или пароль";
        }
    }

    @Override
    public boolean needsAuthorization() {
        return false;
    }

    @Override
    public String getHelp() {
        return "войти в систему";
    }
}
