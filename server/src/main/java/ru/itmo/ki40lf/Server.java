package ru.itmo.ki40lf;

import ru.itmo.ki40lf.clientPart.Request;
import ru.itmo.ki40lf.clientPart.Response;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен на порту " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Клиент подключился: " + clientSocket.getInetAddress());

                try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                     ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

                    Request request = (Request) in.readObject();
                    System.out.println("Получена команда: " + request.getMessage());

                    // Простая заглушка
                    Response response = new Response("Сервер получил команду: " + request.getMessage());
                    out.writeObject(response);
                } catch (ClassNotFoundException e) {
                    System.err.println("Ошибка при чтении запроса: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
