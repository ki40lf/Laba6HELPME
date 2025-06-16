package ru.itmo.ki40lf.serverPart;

import ru.itmo.ki40lf.resources.Dragon;
import ru.itmo.ki40lf.resources.IdGen;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CollectionManager {
    private List<Dragon> dragons = ServerEnvironment.getInstance().getFileManager().readFromCSV();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
//    IdGen idgen = new IdGen();
//    HashSet<Integer> usedIds = idgen.getUsedIds();
//
//    public void initUsedIds() {
//        for (int i = 0; i < dragons.size(); i++) {
//            Dragon dragon = dragons.get(i);
//            int id = dragon.getId();
//            usedIds.add(id);
//        }
//    }


    private ZonedDateTime initializationTime;

    public CollectionManager(List<Dragon> dragons) {
        this.initializationTime = ZonedDateTime.now();
    }

    public String add(Dragon dragon) {
        lock.writeLock().lock();
        try {
            dragons.add(dragon);
            return "Дракон добавлен: " + dragon.getName();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<Dragon> getAll() {
        lock.readLock().lock();
        try {
            return Collections.unmodifiableList(new ArrayList<>(dragons));
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Dragon> getDragons() {
        lock.readLock().lock();
        try {
            return dragons;
        } finally {
            lock.readLock().unlock();
        }
    }

    public ZonedDateTime getInitializationTime() {
        return initializationTime;
    }
}
