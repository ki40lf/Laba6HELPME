package ru.itmo.ki40lf.serverPart;

import ru.itmo.ki40lf.resources.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private final String filePath;

    public FileManager(String filePath) {
        this.filePath = filePath;
    }

    // Чтение из CSV файла
    public List<Dragon> readFromCSV() {
        List<Dragon> dragons = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader("D:/ITMO/dragons.csv"))) {

            String[] nextLine;
            reader.readNext();
            while ((nextLine = reader.readNext()) != null) {
                // Предполагается, что порядок колонок соответствует полям Dragon
                Dragon dragon = new Dragon(
                        Integer.parseInt(nextLine[0]),
                        nextLine[1],
                        new Coordinates(Float.parseFloat(nextLine[2]), Double.parseDouble(nextLine[3])),
                        ZonedDateTime.parse(nextLine[4], DateTimeFormatter.ISO_ZONED_DATE_TIME),
                        Long.parseLong(nextLine[5]),
                        Color.valueOf(nextLine[6]),
                        DragonType.valueOf(nextLine[7]),
                        DragonCharacter.valueOf(nextLine[8]),
                        new DragonCave(
                                Double.parseDouble(nextLine[9]),    // Depth
                                nextLine[10].equals("null") ? null : Float.parseFloat(nextLine[9])
                        )


                );
                dragons.add(dragon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dragons;
    }

    // Запись в CSV файл
    public void writeToCSV(List<Dragon> dragons) {
        try (CSVWriter writer = new CSVWriter(new FileWriter("D:/ITMO/dragons.csv"))) {
            for (Dragon dragon : dragons) {
                String[] record = {
                        String.valueOf(dragon.getId()),
                        dragon.getName(),
                        String.valueOf(dragon.getCoordinates().getX()),
                        String.valueOf(dragon.getCoordinates().getY()),
                        dragon.getCreationDate().format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                        String.valueOf(dragon.getAge()),
                        dragon.getColor().name(),
                        dragon.getType().name(),
                        String.valueOf(dragon.getCave().getDepth()),
                        dragon.getCave().getNumberOfTreasures() == null ? "null" : String.valueOf(dragon.getCave().getNumberOfTreasures())
                };
                writer.writeNext(record);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}