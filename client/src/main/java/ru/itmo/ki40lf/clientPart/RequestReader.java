package ru.itmo.ki40lf.clientPart;

import java.util.Scanner;

public class RequestReader {
    private final Scanner scanner;

    public RequestReader(Scanner scanner) {
        this.scanner = scanner;
    }

    public Request readRequest() {
        System.out.print("Введите команду: ");
        String line = scanner.nextLine().trim();
        String[] parts = line.split("\\s+");
        String commandName = parts[0];
        String[] arguments = new String[parts.length - 1];
        System.arraycopy(parts, 1, arguments, 0, arguments.length);
        // пока без payload, можно расширить
        return new Request(commandName, arguments, null);
    }
}
