package ru.itmo.ki40lf.serverPart;

import ru.itmo.ki40lf.resources.*;

import ru.itmo.ki40lf.common.Request;
import ru.itmo.ki40lf.common.Response;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Server {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;
    private int port;
    private CommandManager commandManager;

    public Server(int port, CommandManager commandManager) {
        this.port = port;
        this.commandManager = commandManager;
    }

    public void run() {
        try (Selector selector = Selector.open();
             ServerSocketChannel serverChannel = ServerSocketChannel.open()) {

            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Server started on port: " + port);

            while (true) {
                selector.select();
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    if (key.isAcceptable()) {
                        handleAccept(key, selector);
                    } else if (key.isReadable()) {
                        handleRead(key);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private void handleAccept(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ, new StringBuilder());
        System.out.println("Client connected: " + clientChannel.getRemoteAddress());
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        StringBuilder data = (StringBuilder) key.attachment();
        ByteBuffer buffer = ByteBuffer.allocate(8192);

        int bytesRead = clientChannel.read(buffer);
        if (bytesRead == -1) {
            clientChannel.close();
            return;
        }

        buffer.flip();
        String received = StandardCharsets.UTF_8.decode(buffer).toString();
        data.append(received);

        if (received.contains("\n")) {
            String csvData = data.toString().trim();
            System.out.println("Received CSV: " + csvData);

            try {
                Request request = parseCsvRequest(csvData);
                String result = commandManager.startExecuting(request);
                sendResponse(clientChannel, new Response(result));
            } catch (Exception e) {
                sendResponse(clientChannel, new Response("Error processing request: " + e.getMessage()));
            } finally {
                clientChannel.close();
            }
        }
    }

    private Request parseCsvRequest(String csvLine) {
        List<String> fields = parseCsvLine(csvLine);
        if (fields.isEmpty()) throw new IllegalArgumentException("Empty request");

        String command = fields.get(0);
        String[] arguments = fields.size() > 1 ?
                fields.subList(1, fields.size()).toArray(new String[0]) :
                new String[0];

        Dragon dragon = null;
        if (command.equals("insert") || command.equals("update")) {
            dragon = parseDragonFromCsv(fields);
        }

        return new Request(command, arguments, dragon);
    }

    private Dragon parseDragonFromCsv(List<String> fields) {
        if (fields.size() < 11) throw new IllegalArgumentException("Not enough fields for Dragon");

        Dragon dragon = new Dragon();
        int index = 1; // после команды

        index++;
        dragon.setName(fields.get(index++));

        Coordinates coord = new Coordinates(Integer.parseInt(fields.get(index++)), Double.parseDouble(fields.get(index++)));
        dragon.setCoordinates(coord);

        dragon.setCreationDate(ZonedDateTime.parse(fields.get(index++)));
        dragon.setAge(Long.parseLong(fields.get(index++)));
        dragon.setColor(Color.valueOf(fields.get(index++)));
        dragon.setType(DragonType.valueOf(fields.get(index++)));
        dragon.setCharacter(DragonCharacter.valueOf(fields.get(index++)));
        dragon.setCave(new DragonCave(Double.parseDouble(fields.get(index++)), Float.parseFloat(fields.get(index))));

        return dragon;
    }

    private List<String> parseCsvLine(String csvLine) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;

        for (char c : csvLine.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString().trim());
                currentField.setLength(0);
            } else {
                currentField.append(c);
            }
        }
        fields.add(currentField.toString().trim());
        return fields;
    }

    private void sendResponse(SocketChannel channel, Response response) throws IOException {
        String csvResponse = escapeCsv(response.getMessage()) + "\n";
        ByteBuffer buffer = ByteBuffer.wrap(csvResponse.getBytes(StandardCharsets.UTF_8));
        channel.write(buffer);
        System.out.println("Sent response: " + csvResponse.trim());
    }

    private String escapeCsv(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}