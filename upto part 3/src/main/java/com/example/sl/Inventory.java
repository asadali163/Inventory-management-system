package com.example.sl;

import java.util.*;

public class Inventory {
    private ArrayList<Item> itemList;
    private Deque<Item> itemDeque;
    private Set<Item> itemSet;
    private Map<Integer, Item> itemMap; // Added for BST implementation
    private Map<String, Item> itemHashTable; // Added for hash table implementation

    public Inventory() {
        this.itemList = new ArrayList<>();
        this.itemDeque = new LinkedList<>(); // Using LinkedList as deque
        this.itemSet = new HashSet<>(); // Using HashSet as set
        this.itemMap = new TreeMap<>(); // Using TreeMap for BST implementation
        this.itemHashTable = new HashMap<>(); // Using HashMap for hash table implementation
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

    public void addItem(Item item) {
        itemList.add(item);
        itemDeque.add(item);
        itemSet.add(item);
        itemMap.put(item.getId(), item); // Insert into BST
        itemHashTable.put(item.getName(), item); // Insert into hash table
    }

    public void updateItem(Item updatedItem) {
        // Implement item update logic
    }

    public void deleteItem(int id) {
        // Implement item deletion logic
    }

    public Item searchItemByName(String name) {
        return itemHashTable.get(name);
    }
}
