package ru.itmo.ki40lf.serverPart;

public class ServerEnvironment {
    private static ServerEnvironment instance;

    CollectionManager collectionManager;
    CommandManager commandManager;
    FileManager fileManager;

    public static ServerEnvironment getInstance() {
        if (instance == null) {
            instance = new ServerEnvironment();
        }
        return instance;
    }

    private ServerEnvironment() {}

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public void setCollectionManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }
}
