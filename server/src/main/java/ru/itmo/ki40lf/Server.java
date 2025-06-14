// Server.java (обновлённый с поддержкой Java 8, многопоточностью и сохранением пользователей)
package ru.itmo.ki40lf;

import ru.itmo.ki40lf.common.Request;
import ru.itmo.ki40lf.common.Response;
import ru.itmo.ki40lf.commands.Command;
import ru.itmo.ki40lf.serverPart.ServerEnvironment;
import ru.itmo.ki40lf.serverPart.CollectionManager;
import ru.itmo.ki40lf.serverPart.CommandManager;
import ru.itmo.ki40lf.serverPart.FileManager;
import ru.itmo.ki40lf.userManager.UserManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 12345;
    private static final ForkJoinPool requestReadPool = new ForkJoinPool();
    private static final ForkJoinPool requestProcessPool = new ForkJoinPool();
    private static final ExecutorService responseSendPool = Executors.newFixedThreadPool(8);

    public static void main(String[] args) {
        ServerEnvironment environment = ServerEnvironment.getInstance();
        environment.setFileManager(new FileManager("dragons.csv"));
        environment.setCollectionManager(new CollectionManager());

        UserManager userManager = new UserManager();
        userManager.loadUsers("users.csv");
        environment.setUserManager(userManager);

        environment.setCommandManager(new CommandManager(userManager));

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен на порту " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Новое подключение: " + clientSocket.getInetAddress());

                requestReadPool.execute(() -> handleClient(clientSocket));
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
            while (true) {
                //requestReadPool.submit(() -> {
                    try {
                        Object received = in.readObject();
                        if (!(received instanceof Request)) {
                            System.out.println("Некорректный объект от клиента.");
                            clientSocket.close();
                            return;
                        }
                        Request request = (Request) received;
                        System.out.println("Получен запрос от клиента: " + request.getMessage());

                        requestProcessPool.submit(() -> {
                            try {
                                String result = null;
                                Response response;

                                String message = request.getMessage();
                                if ("register".equals(message)) {
                                    if (request.getLogin() != null) {
                                        response = new Response("Вы уже вошли в аккаунт", true);
                                    } else {
                                    boolean ok = ServerEnvironment.getInstance().getUserManager()
                                            .registerUser(request.getLogin(), request.getPasswordHash());
                                    result = ok ? "Регистрация прошла успешно!" : "Такой пользователь уже существует!";
                                    response = new Response(result, ok);
                                    }

                                } else if ("login".equals(message)) {
                                    boolean ok = ServerEnvironment.getInstance().getUserManager()
                                            .authenticate(request.getLogin(), request.getPasswordHash());
                                    result = ok ? "Вход выполнен: " + request.getLogin()
                                            : "Неверный логин или пароль.";
                                    response = new Response(result, ok);

                                } else {
                                    CommandManager commandManager = ServerEnvironment.getInstance().getCommandManager();
                                    Command command = commandManager.getCommand(message);

                                    if (command == null) {
                                        response = new Response("Неизвестная команда: " + message);
                                    } else if (command.needsAuthorization() && request.getCredentials().getLogin() == null) {
                                        response = new Response("Ошибка: требуется авторизация.");
                                    } else {
                                        result = command.execute(request);
                                        response = new Response(result);
                                    }
                                }

                                final String finalResult = result;
                                responseSendPool.submit(() -> {
                                    try {
                                        out.writeObject(response);
                                        out.flush();
                                        System.out.println("Ответ отправлен клиенту: " + finalResult);
                                    } catch (IOException e) {
                                        System.out.println("Ошибка при отправке ответа: " + e.getMessage());
                                    }
                                });

                            } catch (Exception e) {
                                System.out.println("Ошибка при обработке команды: " + e.getMessage());
                            }
                        });

                    } catch (IOException e) {
                        System.out.println("Ошибка чтения от клиента: " + e.getMessage());
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                //});
            }
        } catch (IOException e) {
            System.out.println("Проблема с клиентом: " + e.getMessage());
        } finally {
            try {
                ServerEnvironment.getInstance().getUserManager().saveUsers("users.csv");
                clientSocket.close();
                System.out.println("Соединение с клиентом закрыто.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
} // Совместим с Java 8


