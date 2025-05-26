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
    private static SocketChannel socket;

    public void run() {
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
                        System.exit(1);
                        break;
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
                            //ExecuteScript.executeScript("C/Users/lubst/FilesCommands.csv");
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

    private String serializeRequestToCSV(Request request) {
        List<String> fields = new ArrayList<>();
        fields.add(escapeCsv(request.getMessage()));

        for (String arg : request.getArgs()) {
            fields.add(escapeCsv(arg));
        }

        if (request.getDragon() != null) {
            fields.addAll(serializeDragon(request.getDragon()));
        }

        return String.join(",", fields);
    }

    private List<String> serializeDragon(Dragon dragon) {
        List<String> fields = new ArrayList<>();
        fields.add(escapeCsv(String.valueOf(dragon.getId())));
        fields.add(escapeCsv(dragon.getName()));

        Coordinates coord = dragon.getCoordinates();
        fields.add(escapeCsv(String.valueOf(coord.getX())));
        fields.add(escapeCsv(String.valueOf(coord.getY())));
        fields.add(escapeCsv(String.valueOf(dragon.getCreationDate())));  // Изменилось CreationDate -> ZoneDate
        fields.add(escapeCsv(String.valueOf(dragon.getAge())));
        fields.add(escapeCsv(dragon.getColor().toString()));
        fields.add(escapeCsv(dragon.getType().toString()));
        fields.add(escapeCsv(dragon.getCharacter().toString()));
        fields.add(escapeCsv(String.valueOf(dragon.getCave().getDepth())));
        fields.add(escapeCsv(String.valueOf(dragon.getCave().getNumberOfTreasures())));

        return fields;
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    public static void sendRequest(Request request) throws IOException {
        Client client = new Client();
        String csvRequest = client.serializeRequestToCSV(request) + "\n";

        try (SocketChannel channel = SocketChannel.open()) {
            channel.connect(new InetSocketAddress("localhost", 12345));
            ByteBuffer buffer = ByteBuffer.wrap(csvRequest.getBytes());
            channel.write(buffer);

            ByteBuffer responseBuffer = ByteBuffer.allocate(8192);
            channel.read(responseBuffer);
            responseBuffer.flip();
            String response = new String(responseBuffer.array()).trim();
            System.out.println(response);

        } catch (IOException e) {
            System.out.println("Ошибка подключения к серверу: " + e.getMessage());
        }
    }
}
