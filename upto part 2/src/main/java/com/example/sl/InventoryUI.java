package com.example.sl;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Deque;
import java.util.Set;

public class InventoryUI extends Application {

    private Inventory inventory;
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.inventory = new Inventory();

        initialize();

        Scene scene = new Scene(createLayout(), 400, 300);
        stage.setScene(scene);
        stage.setTitle("Inventory Management System");
        stage.show();
    }

    private void initialize() {
        inventory.addItem(new Item(1, "Product 1", 10, 15.0));
        inventory.addItem(new Item(2, "Product 2", 20, 25.0));
    }

    private VBox createLayout() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Button addButton = new Button("Add Item");
        addButton.setOnAction(event -> showAddItemDialog());

        Button displayButton = new Button("Display Items");
        displayButton.setOnAction(event -> displayItems());

        layout.getChildren().addAll(addButton, displayButton);
        return layout;
    }

    private void showAddItemDialog() {
        Dialog<Item> dialog = new Dialog<>();
        dialog.setTitle("Add Item");
        dialog.setHeaderText("Enter Item Details");

        TextField idField = new TextField();
        idField.setPromptText("ID");

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.getDialogPane().setContent(new VBox(8, idField, nameField, quantityField, priceField));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    String name = nameField.getText();
                    int quantity = Integer.parseInt(quantityField.getText());
                    double price = Double.parseDouble(priceField.getText());
                    return new Item(id, name, quantity, price);
                } catch (NumberFormatException e) {
                    showAlert("Error", "Invalid input. Please enter valid data.");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(item -> inventory.addItem(item));
    }

    private void displayItems() {
        TableView<Item> table = new TableView<>();
        TableColumn<Item, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        TableColumn<Item, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        TableColumn<Item, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        TableColumn<Item, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        TableColumn<Item, Void> actionColumn = new TableColumn<>("Action");

        Callback<TableColumn<Item, Void>, TableCell<Item, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Item, Void> call(final TableColumn<Item, Void> param) {
                final TableCell<Item, Void> cell = new TableCell<>() {
                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.setOnAction(event -> {
                            Item item = getTableView().getItems().get(getIndex());
                            inventory.deleteItem(item.getId());
                            getTableView().getItems().remove(item);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }
                };
                return cell;
            }
        };

        actionColumn.setCellFactory(cellFactory);

        table.getColumns().addAll(idColumn, nameColumn, quantityColumn, priceColumn, actionColumn);

        // Display items from ArrayList
        table.getItems().addAll(inventory.getItemList());

        // Display items from Deque
        Deque<Item> itemDeque = inventory.getItemDeque();
        System.out.println("Deque Items:");
        for (Item item : itemDeque) {
            System.out.println(item);
        }

        // Display items from Set
        Set<Item> itemSet = inventory.getItemSet();
        System.out.println("Set Items:");
        for (Item item : itemSet) {
            System.out.println(item);
        }

        VBox layout = new VBox(10, table);
        stage.setScene(new Scene(layout, 600, 400));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
