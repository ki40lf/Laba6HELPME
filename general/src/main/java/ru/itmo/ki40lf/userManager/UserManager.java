package ru.itmo.ki40lf.userManager;

import ru.itmo.ki40lf.common.PasswordHasher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {
    private static final Map<String, String> users = new HashMap<>();
    private static final Path userFile = Paths.get("users.csv");
    private final MessageDigest digest;

    public UserManager() {
        try {
            digest = MessageDigest.getInstance("SHA-224");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-224 не поддерживается", e);
        }
        loadUsers();
    }

    private void loadUsers() {
        if (!Files.exists(userFile)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(userFile, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String login = parts[0];
                    String hash = parts[1];
                    users.put(login, hash);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean registerUser(String login, String password) throws IOException {
        if (users.containsKey(login)) {
            return false; // пользователь уже существует
        }
        String hashPassword = hashPassword(password);
        users.put(login, hashPassword);
        // дописываем нового чела в CSV
        try (BufferedWriter writer = Files.newBufferedWriter(userFile, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(login + "," + hashPassword);
            writer.newLine();
        }
        return true;
    }

    public synchronized boolean authenticate(String login, String password) {
        String storedHash = users.get(login);
        if (storedHash == null) {
            return false;
        }
        String hashPassword = hashPassword(password);
        return storedHash.equals(hashPassword);
    }

    private String hashPassword(String password) {
        byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        // Конвертация в шестнадцатеричную строку
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

