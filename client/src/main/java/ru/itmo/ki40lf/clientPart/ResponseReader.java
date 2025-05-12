package ru.itmo.ki40lf.clientPart;
import ru.itmo.ki40lf.common.Request;
import ru.itmo.ki40lf.common.Response;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ResponseReader {
    private final ObjectInputStream in;

    public ResponseReader(ObjectInputStream in) {
        this.in = in;
    }

    public Response read() throws IOException, ClassNotFoundException {
        return (Response) in.readObject();
    }

    public void print(Response response) {
        System.out.println(response.getMessage());
    }
}