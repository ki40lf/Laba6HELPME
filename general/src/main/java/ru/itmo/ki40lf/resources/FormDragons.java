package ru.itmo.ki40lf.resources;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Scanner;

public class FormDragons implements Serializable {
    static Scanner scanner = new Scanner(System.in);

    public FormDragons() {}

    public FormDragons(Scanner scanner) {
        this.scanner = scanner;
    }

    public static Dragon createDragon() throws IllegalArgumentException {
        Dragon temp = new Dragon();

        while (true) {
            try {
                System.out.print("Введите имя дракона: ");
                temp.setName(scanner.nextLine()); // Используем сеттер с валидацией
                break; // Если ввели корректное значение, выходим из цикла
            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка: " + e.getMessage() + ". Повторите ввод.");
            }
        }

        while (true) {
            try {
                System.out.print("Введите координаты через пробел (x y): ");
                float x = Float.parseFloat(scanner.next());
                double y = Double.parseDouble(scanner.next());
                temp.setCoordinates(new Coordinates(x, y)); // Используем сеттер
                break;
            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка: " + e.getMessage() + ". Повторите ввод.");
                scanner.nextLine();
            }
        }

        while (true) {
            try {
                System.out.print("Введите возраст дракона: ");
                temp.setAge(Long.parseLong(scanner.next())); // Используем сеттер с валидацией
                break;
            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка: " + e.getMessage() + ". Повторите ввод.");
            }
        }

        while (true) {
            try {
                System.out.print("Введите цвет (BLUE, YELLOW, WHITE, BROWN): ");
                temp.setColor(Color.valueOf(scanner.next().toUpperCase())); // Используем сеттер
                break;
            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка: " + e.getMessage() + ". Повторите ввод.");
            }
        }

        while (true) {
            try {
                System.out.print("Введите тип дракона (WATER, UNDERGROUND, AIR): ");
                temp.setType(DragonType.valueOf(scanner.next().toUpperCase())); // Используем сеттер
                break;
            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка: " + e.getMessage() + ". Повторите ввод.");
            }
        }

        while (true) {
            try {
                System.out.print("Введите характер (CHAOTIC, CHAOTIC_EVIL, FICKLE): ");
                temp.setCharacter(DragonCharacter.valueOf(scanner.next().toUpperCase())); // Используем сеттер
                break;
            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка: " + e.getMessage() + ". Повторите ввод.");
            }
        }

        while (true) {
            try {
                System.out.print("Введите через пробел глубину пещеры и количество сокровищ: ");
                double depth = Double.parseDouble(scanner.next());
                float treasures = Float.parseFloat(scanner.next());
                temp.setCave(new DragonCave(depth, treasures)); // Используем сеттер
                break;
            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка: " + e.getMessage() + ". Повторите ввод.");
                scanner.nextLine();
            }
        }
        temp.setCreationDate(ZonedDateTime.now());

        return temp; // Возвращаем корректно созданного дракона
    }

    public static Dragon updateDragon(String nedoId) throws IllegalArgumentException {

        Dragon dragon = createDragon();
        return dragon;
    }
}