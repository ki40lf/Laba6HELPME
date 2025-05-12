package ru.itmo.ki40lf.resources;

import java.io.Serializable;

/**
 * Класс координат
 */
public class Coordinates implements Serializable {
    /** Поле х */
    private float x;
    /** Поле у */
    private double y; //Максимальное значение поля: 273

    /**
     * Конструктор
     * @param x
     * @param y
     */
    public Coordinates(float x, double y) {
        this.x = x;
        if (y <= 273) {
            this.y = y;
        } else {
            throw new IllegalArgumentException("Значение y не должно быть больше 273");
        }

    }

    /**
     * Получить Х
     * @return
     */
    public float getX() {
        return x;
    }

    /**
     * Получить У
     * @return
     */
    public double getY() {
        return y;
    }
}

