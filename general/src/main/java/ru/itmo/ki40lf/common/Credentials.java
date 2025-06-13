package ru.itmo.ki40lf.common;

import java.io.Serializable;

public class Credentials implements Serializable {
    private String login;
    private String password;

    public Credentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}