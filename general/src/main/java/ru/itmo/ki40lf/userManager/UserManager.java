package ru.itmo.ki40lf.userManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserManager {
    private final Map<String, String> users = new HashMap<>();
    private final Set<String> loggedInUsers = new HashSet<>();
    private final Set<String> loggedInPasswords = new HashSet<>();

    private final Path userFile = Paths.get("users.csv");
    private final MessageDigest digest;

    public UserManager() {
        try {
            digest = MessageDigest.getInstance("SHA-224");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-224 не поддерживается", e);
        }
    }

    public void loadUsers(String path) {
        Path file = Paths.get(path);
        if (!Files.exists(file)) return;
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения " + path + ": " + e.getMessage());
        }
    }

    public void saveUsers(String path) throws IOException {
        Path file = Paths.get(path);
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                writer.write(entry.getKey() + ";" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IOException("Ошибка записи " + path + ": " + e.getMessage());
        }
    }

    public synchronized boolean registerUser(String login, String password) throws IOException {
        if (users.containsKey(login)) return false;
        String hashPassword = hashPassword(password);
        users.put(login, hashPassword);
        try (BufferedWriter writer = Files.newBufferedWriter(userFile, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(login + ";" + hashPassword);
            writer.newLine();
        }
        return true;
    }

    public synchronized boolean authenticate(String login, String password) {
        String storedHash = users.get(login);
        if (storedHash == null) return false;
        String hashPassword = hashPassword(password);
        return storedHash.equals(hashPassword);
    }

    private String hashPassword(String password) {
        byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public synchronized boolean isLoggedIn(String login) {
        return loggedInUsers.contains(login);
    }

    public synchronized boolean isPasswordUsed(String password) {
        return loggedInPasswords.contains(password);
    }

    public synchronized void setLoggedInUser(String login) {
        loggedInUsers.add(login);
    }

    public synchronized void setPasswordUsed(String password) {
        loggedInPasswords.add(password);
    }

    public synchronized void removeLoggedInUser(String login) {
        loggedInUsers.remove(login);
    }

    public synchronized void removePasswordUsed(String password) {
        loggedInPasswords.remove(password);
    }

    public synchronized Map<String, String> getUsers() {
        return users;
    }
}

