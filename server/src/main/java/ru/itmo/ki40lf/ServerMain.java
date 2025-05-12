package ru.itmo.ki40lf;

import ru.itmo.ki40lf.clientPart.Request;
import ru.itmo.ki40lf.clientPart.Response;
import ru.itmo.ki40lf.serverPart.CollectionManager;
import ru.itmo.ki40lf.serverPart.CommandManager;
import ru.itmo.ki40lf.serverPart.FileManager;
import ru.itmo.ki40lf.serverPart.ServerEnvironment;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {
    public static void main(String[] args) {
        ServerEnvironment serverEnvironment = ServerEnvironment.getInstance();
        CollectionManager collectionManager = serverEnvironment.getCollectionManager();
        CommandManager commandManager = serverEnvironment.getCommandManager();
        FileManager fileManager = serverEnvironment.getFileManager();

        // Инициализация зависимостей
        serverEnvironment.setCollectionManager(collectionManager);
        serverEnvironment.setCommandManager(commandManager);
        serverEnvironment.setFileManager(fileManager);

        // Регистрация команд должна быть здесь, если используется
        // commandManager.registerCommand(new SomeCommand());

        String filePath = System.getenv("MY_FILE_PATH");
        if (filePath == null) {
            System.err.println("Environmental variable MY_FILE_PATH wasn't found");
            return;
        }

        System.out.println("Reading file: " + filePath);

        File file = new File(filePath);
        if (!file.exists() || !file.canRead()) {
            System.err.println("File access error: " + filePath);
            return;
        }

        try {
            // Чтение CSV вместо JSON
            System.out.println("Loading data from CSV file...");
            fileManager.readCsvFile(filePath);
            System.out.println("Data loaded successfully. Elements count: " + collectionManager.getCollection().size());
        } catch (Exception e) {
            System.err.println("Error reading file: " + e.getMessage());
            return;
        }

        // Создаём сервер с передачей commandManager
        Server server = new Server(6651, commandManager);
        System.out.println("Starting server...");
        server.run();
    }
}