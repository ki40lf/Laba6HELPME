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
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

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
        AtomicReference<String> user = new AtomicReference<>();
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
                            ServerEnvironment.getInstance().getUserManager().removeLoggedInUser(String.valueOf(user));
                            return;
                        }
                        Request request = (Request) received;
                        System.out.println("Получен запрос от клиента: " + request.getMessage());

                        requestProcessPool.submit(() -> {
                            try {
                                String result = null;
                                Response response;

                                String message = request.getMessage();

                                CommandManager commandManager = ServerEnvironment.getInstance().getCommandManager();
                                Command command = commandManager.getCommand(message);

                                if (command == null) {
                                    response = new Response("Неизвестная команда: " + message);
                                } else if (command.needsAuthorization() && request.getCredentials().getLogin() == null) {
                                    response = new Response("Ошибка: требуется авторизация.", false);
                                } else if ((result = command.execute(request)).equals("Неверный логин или пароль") || result.equals("Такой пользователь уже существует!") ) {
                                    response = new Response(result, false);
                                } else {
                                    user.set(request.getLogin());
                                    response = new Response(result);
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
                        break;
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
                ServerEnvironment.getInstance().getUserManager().removeLoggedInUser(String.valueOf(user));
                System.out.println("Соединение с клиентом закрыто.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}