package ru.itmo.ki40lf.commands;

import ru.itmo.ki40lf.common.Request;
import ru.itmo.ki40lf.common.Response;
import ru.itmo.ki40lf.userManager.UserManager;

import java.io.IOException;

public class RegisterCommand extends Command {
    private final UserManager userManager;

    public RegisterCommand(UserManager userManager) {
        super("register");
        this.userManager = userManager;
    }

    @Override
    public String execute(Request request) {
        String login = request.getCredentials().getLogin();
        String password = request.getCredentials().getPassword();

        if (login == null || login.isBlank() || password == null || password.isBlank()) {
            return "Логин и пароль не могут быть пустыми.";
        }

        if (userManager.isLoggedIn(login) && userManager.isPasswordUsed(password)) {
            return "Вы уже вошли в систему";
        }

        try {
            if (userManager.registerUser(login, password)) {
                userManager.setLoggedInUser(login);
                userManager.setPasswordUsed(password);
                return "Регистрация прошла успешно!";
            } else {
                userManager.removeLoggedInUser(login);
                userManager.removePasswordUsed(password);
                return "Такой пользователь уже существует!";
            }
        } catch (IOException e) {
            return "Ошибка при сохранении пользователя: " + e.getMessage();
        }
    }

    @Override
    public boolean needsAuthorization() {
        return false;
    }

    @Override
    public String getHelp() {
        return "зарегистрироваться на сервере";
    }
}
