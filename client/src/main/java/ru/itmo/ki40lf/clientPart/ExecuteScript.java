package ru.itmo.ki40lf.clientPart;

import ru.itmo.ki40lf.common.Request;

import java.io.*;
import java.util.Arrays;
import java.util.Stack;

import static ru.itmo.ki40lf.clientPart.Client.sendRequest;

public class ExecuteScript {
    public static void executeScript(String filePath) {
        File scriptFile = new File(filePath);

        if (!scriptFile.exists() || !scriptFile.isFile()) {
            System.out.println("⛔️ Файл не найден или это не файл.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(scriptFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Пропускаем пустые строки

                System.out.println("📜 Выполнение команды: " + line);

                // Парсим команду
                String[] commandLine = line.trim().split("\\s+");
                String command = commandLine[0];
                String[] arguments = Arrays.copyOfRange(commandLine, 1, commandLine.length);

                // Формируем запрос
                Request request = new Request(command, arguments, null);

                // Отправляем на сервер и читаем ответ
                try {
                    sendRequest(request);
                } catch (IOException e) {
                    System.out.println("⛔️ Ошибка при выполнении команды: " + command);
                }
            }
        } catch (IOException e) {
            System.out.println("⛔️ Ошибка при чтении файла: " + e.getMessage());
        }
    }
}
