package ru.itmo.ki40lf.serverPart;

import ru.itmo.ki40lf.resources.Dragon;

import java.time.ZonedDateTime;
import java.util.*;

public class CollectionManager {
    private List<Dragon> dragons = ServerEnvironment.getInstance().getFileManager().readFromCSV();
    private ZonedDateTime initializationTime;

    public CollectionManager() {
        this.initializationTime = ZonedDateTime.now();
    }

    public String add(Dragon dragon) {
        dragons.add(dragon);
        return "Dragon added: " + dragon.getName();
    }

    public String show() {
        if (dragons.isEmpty()) return "Collection is empty.";
        StringBuilder sb = new StringBuilder();
        for (Dragon d : dragons) sb.append(d).append("\n");
        return sb.toString();
    }

    public List<Dragon> getAll() {
        return Collections.unmodifiableList(dragons);
    }

    public List<Dragon> getDragons() {
        return dragons;
    }

    public ZonedDateTime getInitializationTime() {
        return initializationTime;
    }
}