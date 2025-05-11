package ru.itmo.ki40lf.serverPart;

import ru.itmo.ki40lf.resources.Dragon;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileManager {
    private final String filePath;

    public void readCollection(DragonCollection dragonCollection) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(isr);
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                Dragon dragon = parseDragon(line);
                if (dragon != null) {
                    dragonCollection.addDragon(dragon);
                }
            }
        } catch (IOException e){
            System.out.println("Ошибка чтения файл: ");
            System.exit(0);
        }
    }
}