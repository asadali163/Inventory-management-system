package com.example.sl;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Comparator; // Add this import statement
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
        inventory.initializeDatabase(); // Initialize database connection

        initialize();

        Scene scene = new Scene(createLayout(), 400, 300);
        stage.setScene(scene);
        stage.setTitle("Inventory Management System");
        stage.show();
    }

    private void initialize() {
    }

    private void showSearchItemDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Search Item");
        dialog.setHeaderText("Enter Product Name:");

        TextField searchField = new TextField();
        searchField.setPromptText("Product Name");

        ButtonType searchButtonType = new ButtonType("Search", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(searchButtonType, ButtonType.CANCEL);

        dialog.getDialogPane().setContent(new VBox(8, searchField));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == searchButtonType) {
                return searchField.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(productName -> {
            Item foundItem = inventory.searchItemByName(productName);
            if (foundItem != null) {
                showAlert("Product Found", "ID: " + foundItem.getId() +
                        "\nName: " + foundItem.getName() +
                        "\nQuantity: " + foundItem.getQuantity() +
                        "\nPrice: $" + foundItem.getPrice());
            } else {
                showAlert("Product Not Found", "No product found with the name: " + productName);
            }
        });
    }

    private VBox createLayout() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        // Add search button to the layout
        Button searchButton = new Button("Search Item");
        searchButton.setOnAction(event -> showSearchItemDialog());
        layout.getChildren().add(searchButton);

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

    private Scene createLayoutScene() {
        VBox layout = createLayout();
        return new Scene(layout, 400, 300);
    }

    public void displayItems() {
        TableView<Item> table = new TableView<>();
        table.getItems().clear();

        TableColumn<Item, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Item, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Item, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Item, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Item, Void> actionColumn = new TableColumn<>("Action");

        Callback<TableColumn<Item, Void>, TableCell<Item, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Item, Void> call(final TableColumn<Item, Void> param) {
                final TableCell<Item, Void> cell = new TableCell<>() {
                    private final Button deleteButton = new Button("Delete");
                    private final Button updateButton = new Button("Update");

                    {
                        deleteButton.setOnAction(event -> {
                            Item item = getTableView().getItems().get(getIndex());
                            inventory.deleteItem(item.getId());
                            getTableView().getItems().remove(item);
                        });

                        updateButton.setOnAction(event -> {
                            Item item = getTableView().getItems().get(getIndex());
                            showUpdateItemDialog(item);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox buttons = new HBox(deleteButton, updateButton);
                            buttons.setSpacing(5);
                            setGraphic(buttons);
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

        // Back button
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> stage.setScene(createLayoutScene()));

        // Sort by name button
        Button sortByNameButton = new Button("Sort by Name");
        sortByNameButton.setOnAction(event -> {
            inventory.sortItemsByName();
            displayItems(); // Refresh the display after sorting
        });

        HBox buttonBox = new HBox(10, backButton, sortByNameButton); // Use HBox for horizontal arrangement
        buttonBox.setAlignment(Pos.TOP_LEFT); // Align buttons to top left

        VBox layout = new VBox(10, buttonBox, table);
        layout.setPadding(new Insets(10)); // Add padding to the layout

        stage.setScene(new Scene(layout, 600, 400));
    }

    private void showUpdateItemDialog(Item item) {
        Dialog<Item> dialog = new Dialog<>();
        dialog.setTitle("Update Item");
        dialog.setHeaderText("Enter Updated Item Details");

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        nameField.setText(item.getName());

        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");
        quantityField.setText(String.valueOf(item.getQuantity()));

        TextField priceField = new TextField();
        priceField.setPromptText("Price");
        priceField.setText(String.valueOf(item.getPrice()));

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        dialog.getDialogPane().setContent(new VBox(8, nameField, quantityField, priceField));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                try {
                    String name = nameField.getText();
                    int quantity = Integer.parseInt(quantityField.getText());
                    double price = Double.parseDouble(priceField.getText());
                    item.setName(name);
                    item.setQuantity(quantity);
                    item.setPrice(price);
                    return item;
                } catch (NumberFormatException e) {
                    showAlert("Error", "Invalid input. Please enter valid data.");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedItem -> {
            inventory.updateItemDB(updatedItem);
            showAlert("Success", "Item updated successfully.");
        });
    }


    private void sortByName() {
        inventory.getItemList().sort(Comparator.comparing(Item::getName));
        // Implement your logic to display sorted items
        // For example, update the UI to display sorted items
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
