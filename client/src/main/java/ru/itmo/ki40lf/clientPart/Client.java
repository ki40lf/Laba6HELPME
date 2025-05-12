package ru.itmo.ki40lf.clientPart;

import ru.itmo.ki40lf.common.Request;
import ru.itmo.ki40lf.common.Response;
import ru.itmo.ki40lf.resources.Coordinates;
import ru.itmo.ki40lf.resources.Dragon;
import ru.itmo.ki40lf.resources.FormDragons;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client {
    private SocketChannel channel;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final String host = "localhost";
    private final int port = 12345;

    /**
     * Метод для запуска клиента и обработки команд
     */
    public void run() {
        connect();
        Scanner scanner = new Scanner(System.in);
        FormDragons dragonGenerator = new FormDragons();
        System.out.println("✅ Подключение установлено с сервером " + host + ":" + port);
        System.out.println("Добро пожаловать! Введите команду (или введите help для списка команд)");

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            String[] commandLine = input.trim().split("\\s+");
            String[] arguments = Arrays.copyOfRange(commandLine, 1, commandLine.length);

            String command = commandLine[0];
            Request request = null;
            Dragon dragon = null;

            if (!command.isEmpty()) {
                switch (command) {
                    case "exit":
                        disconnect();
                        System.exit(1);
                        break;

                    case "save":
                        System.out.println("Сохранение коллекции не доступно с клиента");
                        continue;

                    case "update_id":
                        if (arguments.length > 0) {
                            long id = Long.parseLong(arguments[0]);
                            dragon = dragonGenerator.createDragon(id);
                        } else {
                            System.out.println("ID не передан для обновления");
                            continue;
                        }
                        break;

                    case "insert":
                        dragon = dragonGenerator.createDragon();
                        break;

                    default:
                        break;
                }

                request = new Request(input, arguments, dragon);

                try {
                    sendRequest(request);
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Ошибка при отправке запроса: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Метод для подключения к серверу
     */
    public void connect() {
        try {
            if (channel == null || !channel.isConnected()) {
                channel = SocketChannel.open();
                channel.connect(new InetSocketAddress(host, port));
                System.out.println("✅ Подключение установлено с сервером " + host + ":" + port);

                // ✅ Потоки создаются только при первом успешном соединении
                if (out == null && in == null) {
                    out = new ObjectOutputStream(channel.socket().getOutputStream());
                    out.flush(); // <-- важно, чтобы заголовок сразу отправился
                    in = new ObjectInputStream(channel.socket().getInputStream());
                    System.out.println("✅ Потоки ввода-вывода настроены");
                }
            }
        } catch (IOException e) {
            System.out.println("⛔ Ошибка подключения: " + e.getMessage());
        }
    }

    /**
     * Метод для отправки запроса на сервер и получения ответа
     */
    public void sendRequest(Request request) throws IOException, ClassNotFoundException {
        if (channel == null || !channel.isConnected()) {
            System.out.println("⛔ Нет подключения к серверу, попытка подключения...");
            connect();
        }

        if (out != null) {
            out.writeObject(request);
            out.flush();
            System.out.println("📤 Запрос отправлен на сервер: " + request.getMessage());

            // ✅ Чтение объекта-ответа
            Object responseObject = in.readObject();
            if (responseObject instanceof Response) {
                Response response = (Response) responseObject;
                System.out.println("📦 Ответ от сервера: " + response.getMessage());
            } else {
                System.out.println("❌ Получен неизвестный ответ от сервера.");
            }
        } else {
            System.out.println("❌ Поток вывода не инициализирован!");
        }
    }

    /**
     * Метод для отключения от сервера
     */
    public void disconnect() {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
                System.out.println("🔌 Соединение закрыто.");
            }
        } catch (IOException e) {
            System.out.println("⛔ Ошибка при закрытии подключения: " + e.getMessage());
        }
    }

    /**
     * Точка входа в программу
     */
    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}