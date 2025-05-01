package ru.itmo.ki40lf.model;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Основной класс Дркона
 */
public class Dragon {
    /** Поле индентификатора */
    private final int id;//Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    /** Поле имени */
    private String name; //Поле не может быть null, Строка не может быть пустой
    /** Поле координат */
    private Coordinates coordinates; //Поле не может быть null
    /** Поле даты создания */
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    /**Поле имени */
    private Long age; //Значение поля должно быть больше 0, Поле может быть null
    /** Поле цвета */
    private Color color; //Поле может быть null
    /** Поле типа */
    private DragonType type; //Поле не может быть null
    /** Поле характера */
    private DragonCharacter character; //Поле может быть null
    /** Поле пещеры*/
    private DragonCave cave;
    /**Поле для генерации айди */
    private static final Set<Integer> usedIds = new HashSet<>();//Поле не может быть null

    /**
     * Пустой конструктор
     */
    public Dragon() {
        this.id = IdGen.generateId();
    }

    /**
     * Конструктор с автоматической генерацией id
     * @param name
     * @param coordinates
     * @param creationDate
     * @param age
     * @param color
     * @param type
     * @param character
     * @param cave
     */
    public Dragon( String name, Coordinates coordinates, ZonedDateTime creationDate, Long age, Color color, DragonType type, DragonCharacter character, DragonCave cave) {
        this.id = IdGen.generateId();
        this.setName(name);
        this.setCoordinates(coordinates);
        this.setCreationDate(creationDate);
        this.setAge(age);
        this.setColor(color);
        this.setType(type);
        this.setCharacter(character);
        this.setCave(cave);
    }

    /**
     * Конструктор с id
     * @param id
     * @param name
     * @param coordinates
     * @param creationDate
     * @param age
     * @param color
     * @param type
     * @param character
     * @param cave
     */

    public Dragon(int id, String name, Coordinates coordinates, ZonedDateTime creationDate, Long age, Color color, DragonType type, DragonCharacter character, DragonCave cave){
        this.id = id;
        this.setName(name);
        this.setCoordinates(coordinates);
        this.setCreationDate(creationDate);
        this.setAge(age);
        this.setColor(color);
        this.setType(type);
        this.setCharacter(character);
        this.setCave(cave);
    }

    /**
     * Получить id
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Получить имя
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Сеттер
     * @param name
     */
    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("name не может быть 0 и не может быть пустым");
        }
    }

    /**
     * Геттер
     * @return
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Сеттер координат
     * @param coordinates
     */
    public void setCoordinates(Coordinates coordinates) {
        if (coordinates != null) {
            this.coordinates = coordinates;
        } else {
            throw new IllegalArgumentException("координаты не могут быть null");
        }
    }

    /**
     * Геттер
     * @return
     */
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Сеттер
     * @param creationDate
     */
    public void setCreationDate(ZonedDateTime creationDate) {
        if (creationDate != null) {
            this.creationDate = ZonedDateTime.now();
        } else {
            throw new IllegalArgumentException("ZoneDateTime не может быть пустым или отрицательным");
        }
    }

    /**
     * Геттер возраста
     * @return
     */
    public Long getAge() {
        return age;
    }

    /**
     * Сеттер возраста
     * @param age
     */
    public void setAge(Long age) {
        if (age != null && age > 0) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("Age не может быть пустым или отрицательным");
        }
    }

    /**
     * Геттер
     * @return
     */
    public Color getColor() {
        return color;
    }

    /**
     * Сеттер
     * @param color
     */
    public void setColor(Color color) {
        if (color != null) {
            this.color = color;
        } else {throw new IllegalArgumentException("Color не может быть пустым");}
    }

    /**
     * Геттер
     * @return
     */
    public DragonType getType() {
        return type;
    }

    /**
     * Сеттер
     * @param type
     */
    public void setType(DragonType type) {
        if (type != null) {
            this.type = type;
        } else {
            throw new IllegalArgumentException("Type не может быть пустым");
        }
    }

    /**
     * Геттер
     * @return
     */
    public DragonCharacter getCharacter() {
        return character;
    }

    /**
     * Сеттер характера
     * @param character
     */
    public void setCharacter(DragonCharacter character) {
        if (character != null) {
            this.character = character;
        } else {throw new IllegalArgumentException("Character не может быть пустым");}
    }

    /**
     * Геттер пещер
     * @return
     */
    public DragonCave getCave() {
        return cave;
    }

    /**
     * Сеттер
     * @param cave
     */
    public void setCave(DragonCave cave) {
        if (cave != null) {
            this.cave = cave;
        } else {
            throw new IllegalArgumentException("Cave не может быть пустым");
        }
    }

    /**
     * Переопределение тустринга
     * @return
     */
    @Override
    public String toString() {
        return "Dragon: " +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=(" + coordinates.getX() + ", " + coordinates.getY() + ")" +
                ", creationDate=" + creationDate +
                ", age=" + age +
                ", color=" + color +
                ", type=" + type +
                ", character=" + character +
                ", cave(depth=" + cave.getDepth() + ", treasures=" + cave.getNumberOfTreasures() + ")";
    }
}
