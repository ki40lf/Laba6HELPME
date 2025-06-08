package ru.itmo.ki40lf.common;

import ru.itmo.ki40lf.resources.Dragon;

import java.io.Serializable;


public class Request implements Serializable {
    private final String message;
    private final String[] args;
    private final Dragon dragon; //если команда требует объект из коллекции

    private final String login;
    private final String passwordHash;

    public Request(String message, String[] args, Dragon dragon) {
        this.message = message;
        this.args = args;
        this.dragon = dragon;
        this.login = null;
        this.passwordHash = null;
    }

    public Request(String message, String[] args, Dragon dragon, String login, String password) {
        this.message = message;
        this.args = args;
        this.dragon = dragon;
        this.login = login;
        this.passwordHash = password;
    }

    public String getMessage() {
        return message;
    }

    public String[] getArgs() {
        return args;
    }

    public Dragon getDragon() {
        return dragon;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
