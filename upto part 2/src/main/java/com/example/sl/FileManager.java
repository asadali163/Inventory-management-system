package com.example.sl;

import java.io.*;
import java.util.ArrayList;

public class FileManager {

    private static final String FILE_NAME = "inventory.txt";

    public static void saveToFile(ArrayList<Item> itemList) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(itemList);
            System.out.println("Data saved to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Item> loadFromFile() {
        ArrayList<Item> itemList = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            itemList = (ArrayList<Item>) ois.readObject();
            System.out.println("Data loaded from file.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return itemList;
    }
}
