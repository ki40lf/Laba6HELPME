package ru.itmo.ki40lf.serverPart;

import ru.itmo.ki40lf.resources.Dragon;
import ru.itmo.ki40lf.resources.IdGen;

import java.time.ZonedDateTime;
import java.util.*;

public class CollectionManager {
    private List<Dragon> dragons = ServerEnvironment.getInstance().getFileManager().readFromCSV();
    IdGen idgen = new IdGen();
    HashSet<Integer> usedIds = idgen.getUsedIds();

    public void initUsedIds() {
        for (int i = 0; i < dragons.size(); i++) {
            Dragon dragon = dragons.get(i);
            int id = dragon.getId();
            usedIds.add(id);
        }
    }


    private ZonedDateTime initializationTime;

    public CollectionManager() {
        this.initializationTime = ZonedDateTime.now();
        this.initUsedIds();
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
