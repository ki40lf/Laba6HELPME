package ru.itmo.ki40lf.model;

/**
 * Класс Пещер
 */
public class DragonCave {
    /** Поле глубины */
    private double depth;
    /** Поле количества сокровищ */
    private Float numberOfTreasures; //Поле может быть null, Значение поля должно быть больше 0

    /**
     * Конструктор
     * @param depth
     * @param numberOfTreasures
     */
    public DragonCave(double depth, Float numberOfTreasures) {
        this.setDepth(depth);
        this.setNumberOfTreasures(numberOfTreasures);
    }
/**
 * Геттер глубины*/
    public double getDepth() {
        return depth;
    }

    /**
     * Сеттер глубины
     * @param depth
     */
    public void setDepth(double depth) {
        this.depth = depth;
    }

    /**
     * Геттер кол-ва сокровищ
     * @return
     */
    public Float getNumberOfTreasures() {
        return numberOfTreasures;
    }

    /**
     * Сеттер
     * @param numberOfTreasures
     */
    public void setNumberOfTreasures(Float numberOfTreasures) {
        if (numberOfTreasures >= 0 ) {
            this.numberOfTreasures = numberOfTreasures;
        } else {
            throw new IllegalArgumentException("NumberOfTreasures не может быть отрицательным");
        }
    }
}
