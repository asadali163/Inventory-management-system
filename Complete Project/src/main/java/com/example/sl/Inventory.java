package com.example.sl;

import javafx.scene.control.Alert;

import java.sql.*;
import java.util.*;

public class Inventory {
    private ArrayList<Item> itemList;
    private Deque<Item> itemDeque;
    private Set<Item> itemSet;
    private Map<Integer, Item> itemMap;
    private Map<String, Item> itemHashTable;

    private Connection connection;

    public Inventory() {
        this.itemList = new ArrayList<>();
        this.itemDeque = new LinkedList<>();
        this.itemSet = new HashSet<>();
        this.itemMap = new TreeMap<>();
        this.itemHashTable = new HashMap<>();
        initializeDatabase(); // Initialize the database only once
        fetchDataFromDatabase(); // Fetch data from the database during initialization
    }

    void initializeDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory_db", "root", "asad234#jan");
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        try {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS items (id INT PRIMARY KEY, name VARCHAR(255), quantity INT, price DOUBLE)";
            Statement statement = connection.createStatement();
            statement.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addItemToDatabase(Item item) {
        try {
            String insertSQL = "INSERT INTO items (id, name, quantity, price) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, item.getId());
            preparedStatement.setString(2, item.getName());
            preparedStatement.setInt(3, item.getQuantity());
            preparedStatement.setDouble(4, item.getPrice());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                // Show success message using Alert dialog
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Item '" + item.getName() + "' successfully added.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fetchDataFromDatabase() {
        try {
            String query = "SELECT * FROM items";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int quantity = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");
                Item item = new Item(id, name, quantity, price);
                addItemToList(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addItemToList(Item item) {
        itemList.add(item);
        itemDeque.add(item);
        itemSet.add(item);
        itemMap.put(item.getId(), item);
        itemHashTable.put(item.getName(), item);
    }

    public ArrayList<Item> getItemList() {
        return itemList;
    }

    public Deque<Item> getItemDeque() {
        return itemDeque;
    }

    public Set<Item> getItemSet() {
        return itemSet;
    }

    public void sortItemsByName() {
        itemList.sort(Comparator.comparing(Item::getName));
    }

    public void addItem(Item item) {
        if (itemMap.containsKey(item.getId())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Duplicate Item");
            alert.setHeaderText(null);
            alert.setContentText("Duplicate item with ID " + item.getId() + " not allowed.");
            alert.showAndWait();
        } else {
            addItemToList(item);
            addItemToDatabase(item);
        }
    }

    public void updateItem(Item updatedItem) {
        // Implement item update logic
    }

    public void deleteItem(int id) {
        try {
            String deleteSQL = "DELETE FROM items WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No item found with ID " + id);
            } else {
                itemList.removeIf(item -> item.getId() == id);
                itemDeque.removeIf(item -> item.getId() == id);
                itemSet.removeIf(item -> item.getId() == id);
                itemMap.remove(id);
                System.out.println("Item with ID " + id + " deleted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateItemDB(Item updatedItem) {
        try {
            String updateSQL = "UPDATE items SET name = ?, quantity = ?, price = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
            preparedStatement.setString(1, updatedItem.getName());
            preparedStatement.setInt(2, updatedItem.getQuantity());
            preparedStatement.setDouble(3, updatedItem.getPrice());
            preparedStatement.setInt(4, updatedItem.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                // Update item details in data structures
                for (Item item : itemList) {
                    if (item.getId() == updatedItem.getId()) {
                        item.setName(updatedItem.getName());
                        item.setQuantity(updatedItem.getQuantity());
                        item.setPrice(updatedItem.getPrice());
                        break;
                    }
                }
                // Show success message using Alert dialog
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Item '" + updatedItem.getName() + "' successfully updated.");
                alert.showAndWait();
            } else {
                System.out.println("No item found with ID " + updatedItem.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Item searchItemByName(String name) {
        return itemHashTable.get(name);
    }
}
