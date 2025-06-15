package ru.itmo.ki40lf.clientPart;

import ru.itmo.ki40lf.common.Request;
import ru.itmo.ki40lf.common.Response;
import ru.itmo.ki40lf.resources.Dragon;
import ru.itmo.ki40lf.resources.FormDragons;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;


public class ExecuteScript {
    private static ArrayList<String> usedFiles = new ArrayList<>() ;
    public static ArrayList<String> getUsedFiles() {
        return usedFiles;
    }

    public static void executeScript(String fileName, String currentLogin, String currentPassword) {
        //File scriptFile = new File(fileName);

        try {
            String filePath = new File(fileName).getAbsolutePath();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("У команды execute_script отсутствует название скрипта!");
            return;
        }

        if (usedFiles.contains(fileName)) {
            System.out.println("Файл попытался запустить " + fileName +  ". Пропускаем эту строку");

            return;
        }



        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Пропускаем пустые строки

                System.out.println("Выполнение команды: " + line);

                Request request = null;
                Dragon dragon = null;
                FormDragons dragonGenerator = new FormDragons();
                boolean success = false;

                // Парсим команду
                String[] commandLine = line.trim().split("\\s+");
                String command = commandLine[0];
                String[] arguments = Arrays.copyOfRange(commandLine, 1, commandLine.length);

                if (!command.isEmpty()) {
                    switch (command) {
                        case "exit":
                            System.out.println("Хорошего дня! ♡ (*^w^)");
                            System.exit(1);
                            break;
                        case "save":
                            System.out.println("Сохранение коллекции не доступно с клиента");
                            break;
                        case "update_id":
                            dragon = dragonGenerator.createDragon();
                            break;
                        case "add":
                            dragon = dragonGenerator.createDragon();
                            break;
                        case "execute_script":
                            if (arguments.length != 0) {
                                usedFiles.add(fileName);
                                ExecuteScript.executeScript(arguments[0], currentLogin, currentPassword);
                            } else {
                                System.out.println("В execute_script что-то не так с аргументами");
                            }
                            break;
                        default:
                            break;
                    }}


                    // Формируем запрос
                request = new Request(command, arguments, dragon, currentLogin, currentPassword); //ЧЕ ЗА КАЛЛ


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
            usedFiles.add(fileName);
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }

        //usedFiles.clear();
    }
}
