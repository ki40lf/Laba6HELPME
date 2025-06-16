package ru.itmo.ki40lf.common;

import ru.itmo.ki40lf.resources.Dragon;

import java.io.Serializable;


public class Request implements Serializable {
    private final String message;
    private final String[] args;
    private final Dragon dragon; //если команда требует объект из коллекции

    private final String login;
    private final String passwordHash;
    private final Credentials credentials;

    public Request(String message, String[] args, Dragon dragon, String login, String passwordHash){
        this.message = message;
        this.args = args;
        this.dragon = dragon;
        this.login = login;
        this.passwordHash = passwordHash;
        this.credentials = new Credentials(login, passwordHash);
    }

    public Request(String message, String[] args, Dragon dragon, Credentials credentials) {
        this.message = message;
        this.credentials = credentials;
        this.args = args;
        this.dragon = dragon;
        this.login = null;
        this.passwordHash = null;
    }
    public Request(String message, String[] args, Dragon dragon, String login, String password, Credentials credentials) {
        this.message = message;
        this.args = args;
        this.dragon = dragon;
        this.login = login;
        this.passwordHash = password;
        this.credentials = credentials;
    }
    public Credentials getCredentials() {
        return credentials;
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
