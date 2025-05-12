package ru.itmo.ki40lf;

import ru.itmo.ki40lf.clientPart.*;

import java.io.*;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
//        String host = "localhost";
//        int port = 12345;
//        try //(
//              //  ConnectionManager conn = new ConnectionManager(host, port)
//        //)
//        { ConnectionManager conn = new ConnectionManager(host, port);
//            conn.connect();
//            RequestReader reader = new RequestReader(new Scanner(System.in));
//            RequestWriter writer = new RequestWriter(conn.getOut());
//            ResponseReader responder = new ResponseReader(conn.getIn());
//
//            while (true) {
//                Request request = reader.readRequest();
//                writer.write(request);
//
//                Response response = responder.read();
//                responder.print(response);
//
//                if ("exit".equalsIgnoreCase(request.getMessage())) {
//                    break;
//                }
//            }
//
//        } catch (IOException | ClassNotFoundException e) {
//            System.err.println("Ошибка клиента: " + e.getMessage());
//        }

        Client client = new Client();
        client.run();
    }
}
