package ru.itmo.ki40lf.resources;
import java.util.HashSet;

/**
 * Класс уникальной генерации ID
 */
public class IdGen {
    private static int idCounter = 1;
    private static HashSet<Integer> usedIds = new HashSet<>();

    /** Генерирует уникальный ID
     * @return
     */
    public static int generateId() {
        int newId = 1;
        while (usedIds.contains(newId)) {
            newId++;
        }
        usedIds.add(newId);
        return newId;
    }

    /** Регистрирует ID (если уже существует — выбрасывает ошибку)
     * @param id
     */
    public static void registerId(int id) {
        if (usedIds.contains(id)) {
            throw new IllegalArgumentException("Ошибка: ID " + id + " уже используется!");
        }
        usedIds.add(id);
        idCounter = Math.max(idCounter, id + 1);
    }

    /** Освобождает ID
     * @param id
     */
    public static void releaseId(int id) {
        usedIds.remove(id);
    }
}
