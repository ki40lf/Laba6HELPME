package ru.itmo.ki40lf.common;

import java.io.Serializable;

public class Response implements Serializable {
    private final String message;
    private final boolean success; //new

    public Response(String message) {
        this.message = message;
        this.success = true;
    }

    public Response(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}