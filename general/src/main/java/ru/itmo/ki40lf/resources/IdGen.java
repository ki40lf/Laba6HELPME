package ru.itmo.ki40lf.resources;
import java.util.HashSet;

public class IdGen {
    private static int idCounter = 1;
    private static HashSet<Integer> usedIds = new HashSet<>();

    public HashSet<Integer> getUsedIds() {
        return usedIds;
    }

    //генерируем айди дракону
    public static int generateId() {
        int newId = 1;
        while (usedIds.contains(newId)) {
            newId++;
        }
        usedIds.add(newId);
        return newId;
    }

    //регистрирует айди драконов из файла
    public static Integer registerId(int id) {
        if (usedIds.contains(id)) {
            throw new IllegalArgumentException("Ошибка: ID " + id + " уже используется!");
        }
        usedIds.add(id);
        //idCounter = Math.max(idCounter, id + 1);
        //id = idCounter;
        return id;
    }

    //удалитель айди
    public static void releaseId(int id) {
        usedIds.remove(id);
    }

    public static Integer regenId() {
        return generateId();
    }
}
