package ru.itmo.ki40lf.clientPart;

import ru.itmo.ki40lf.resources.Coordinates;
import ru.itmo.ki40lf.resources.Dragon;
import ru.itmo.ki40lf.resources.FormDragons;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
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
            String[] arguments = Arrays.copyOfRange(commandLine, 1, commandLine.length);
            //System.out.println(arguments.toString());
            String command = commandLine[0];
            Request request = null;
            Dragon dragon = null;

            if (!command.isEmpty()) {
                switch (command) {
                    case "exit":
                        System.exit(1);
                        break;
                    case "save":
                        System.out.println("Сохранение коллекции не доступно с клиента");
                        break;
                    case "update_id":
                        long id = Long.parseLong(arguments[0]); //сделать для айдишника
                        dragon = dragonGenerator.createDragon(id);
                        break;
                    case "insert":
                        dragon = dragonGenerator.createDragon();
                        break;
//                    case "execute_script":
//                        if (arguments.length != 0) {
//                            ExecuteScript.execute(arguments[0]);
//                        } else {
//                            System.out.println("Something wrong with arguments. Write script file name");
//                        }
//                        break;
                    default:
                        break;
                }

                request = new Request(input, arguments, dragon);
                String csvRequest = serializeRequestToCSV(request) + "\n";


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
    }

    private String serializeRequestToCSV(Request request) {
        List<String> fields = new ArrayList<>();
        fields.add(escapeCsv(request.getMessage()));

        // Аргументы команды
        for (String arg : request.getArgs()) {
            fields.add(escapeCsv(arg));
        }

        // Данные Dragon, если есть
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
            System.out.println("Server connection error: " + e.getMessage());
        }
    }
}
