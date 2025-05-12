package ru.itmo.ki40lf.clientPart;
import ru.itmo.ki40lf.common.Request;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class RequestWriter {
    private final ObjectOutputStream out;

    public RequestWriter(ObjectOutputStream out) {
        this.out = out;
    }

    public void write(Request request) throws IOException {
        out.writeObject(request);
        out.flush();
    }
}