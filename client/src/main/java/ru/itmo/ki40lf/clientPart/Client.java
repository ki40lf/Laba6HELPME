package ru.itmo.ki40lf.clientPart;
import ru.itmo.ki40lf.common.Request;
import ru.itmo.ki40lf.common.Response;
import ru.itmo.ki40lf.resources.Coordinates;
import ru.itmo.ki40lf.resources.Dragon;
import ru.itmo.ki40lf.resources.FormDragons;
import ru.itmo.ki40lf.resources.IdGen;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client {
    private static Socket socket;
    private static ObjectOutputStream outputStream;
    private static ObjectInputStream inputStream;
    public void connect() {
        try {
            socket = new Socket("localhost", 12345);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("Подключение установлено с сервером.");
        } catch (IOException e) {
            System.out.println("Ошибка подключения: " + e.getMessage());
        }
    }
    public void disconnect() {
        try {
            if (socket != null) socket.close();
            if (outputStream != null) outputStream.close();
            if (inputStream != null) inputStream.close();
        } catch (IOException e) {
            System.out.println("Ошибка при закрытии соединения.");
        }
    }
    public void run() {
        connect();
        Scanner scanner = new Scanner(System.in);
        FormDragons dragonGenerator = new FormDragons();
        System.out.println("Добро пожаловать! Введите команду (или введите help для списка команд)");

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            String[] commandLine = input.trim().split("\\s+");
            String[] arguments = new String[commandLine.length - 1];
            System.arraycopy(commandLine, 1, arguments, 0, arguments.length);
            //System.out.println(arguments.toString());
            String command = commandLine[0];
            Request request = null;
            Dragon dragon = null;

            if (!command.isEmpty()) {
                switch (command) {
                    case "exit":
                        System.out.println("Хорошего дня! ♡ (*^w^)");
                        disconnect();
                        return;
                    case "save":
                        System.out.println("Сохранение коллекции не доступно с клиента");
                        break;
                    case "update_id":
                        dragon = dragonGenerator.createDragon();
                        break;
                    case "add": //переделываю
                        dragon = dragonGenerator.createDragon();
                        break;
                    case "execute_script":
                        if (arguments.length != 0) {
                            ExecuteScript.executeScript(arguments[0]);
                            ExecuteScript.getUsedFiles().clear();
                        } else {
                            System.out.println("Что-то не так с аргументами");
                        }
                        break;
                    default:
                        break;
                }

                request = new Request(command, arguments, dragon);
                try (Socket socket = new Socket("localhost", 12345);
                     ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                     ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {

                    // Отправляем объект Request
                    outputStream.writeObject(request);
                    outputStream.flush();

                    // Читаем ответ от сервера
                    Response response = (Response) inputStream.readObject();
                    if (response.getMessage() != null) {
                        System.out.println("Ответ от сервера: " + response.getMessage());
                    }

                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Ошибка на сервере: " + e.getMessage());
                }
            }
        }
    }
}
