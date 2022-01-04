package com.github.rysefoxx.verifybot.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.*;

public class Config {

    private static final Dotenv dotenv = Dotenv.load();

    public static String get(String key) {
        return dotenv.get(key);
    }

    public static void saveObject(Object object, File file) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            objectOutputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object loadObject(File file) {
        Object object = null;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            object = objectInputStream.readObject();
            inputStream.close();
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return object;
    }

}
