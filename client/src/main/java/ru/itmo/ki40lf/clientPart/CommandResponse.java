package ru.itmo.ki40lf.clientPart;

public class CommandResponse {
    private final String message;

    public CommandResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}