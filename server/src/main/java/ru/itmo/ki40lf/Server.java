package ru.itmo.ki40lf;
import ru.itmo.ki40lf.common.Request;
import ru.itmo.ki40lf.common.Response;
import ru.itmo.ki40lf.commands.Command;
import ru.itmo.ki40lf.resources.Dragon;
import ru.itmo.ki40lf.serverPart.ServerEnvironment;
import ru.itmo.ki40lf.serverPart.CollectionManager;
import ru.itmo.ki40lf.serverPart.CommandManager;
import ru.itmo.ki40lf.serverPart.FileManager;
import ru.itmo.ki40lf.userManager.UserManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 12345;
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        ServerEnvironment environment = ServerEnvironment.getInstance();

        FileManager fileManager = new FileManager("dragons.csv");
        CollectionManager collectionManager = new CollectionManager();
        UserManager userManager = new UserManager();
        CommandManager commandManager = new CommandManager(userManager);

        environment.setFileManager(fileManager);
        environment.setCollectionManager(collectionManager);
        environment.setUserManager(userManager);
        environment.setCommandManager(commandManager);


        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен на порту " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Новое подключение: " + clientSocket.getInetAddress());

                // Обработка клиента в отдельном потоке
                threadPool.execute(() -> handleClient(clientSocket));
            }

        } catch (IOException e) {
            System.out.println("Ошибка при запуске сервера: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            System.out.println("Начало обработки клиента...");

            CommandManager commandManager = ServerEnvironment.getInstance().getCommandManager();
            UserManager userManager = ServerEnvironment.getInstance().getUserManager();

            while (true) {
                try {
                    Object received = in.readObject();
                    if (!(received instanceof Request)) {
                        System.out.println("Некорректный объект от клиента");
                        break;
                    }
                    Request request = (Request) received;

                    System.out.println("Получена команда: " + request.getMessage());

                    Command command = commandManager.getCommand(request.getMessage());
                    if (command == null) {
                        out.writeObject(new Response("Неизвестная команда: " + request.getMessage()));
                        out.flush();
                        continue;
                    }

                    // 🔐 Проверка авторизации
                    if (command.needsAuthorization()) {
                        if (request.getCredentials() == null ||
                                !userManager.authenticate(
                                        request.getCredentials().getLogin(),
                                        request.getCredentials().getPassword())) {
                            out.writeObject(new Response("Ошибка: требуется авторизация."));
                            out.flush();
                            continue;
                        }
                    }

                    // ✅ Выполнение команды
                    String result = command.execute(request);
                    Response response = new Response(result);
                    out.writeObject(response);
                    out.flush();

                    System.out.println("Ответ отправлен клиенту: " + result);

                } catch (ClassNotFoundException e) {
                    System.out.println("Неизвестный объект от клиента.");
                }
            }
        } catch (IOException e) {
            System.out.println("Проблема с клиентом, соединение закрыто: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Соединение с клиентом закрыто.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}