package com.example.sl;

import java.util.*;

public class Inventory {
    private ArrayList<Item> itemList;
    private Deque<Item> itemDeque;
    private Set<Item> itemSet;

    public Inventory() {
        this.itemList = new ArrayList<>();
        this.itemDeque = new LinkedList<>(); // Using LinkedList as deque
        this.itemSet = new HashSet<>(); // Using HashSet as set
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
    }

    public void updateItem(Item updatedItem) {
        // Implement item update logic
    }

    public void deleteItem(int id) {
        // Implement item deletion logic
    }
}
