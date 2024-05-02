package com.example.sl;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Optional;

public class InventoryUI extends Application {

    private Inventory inventory;
    private Stage stage;
    private VBox mainLayout;
    private VBox displayLayout;
    private Scene mainScene;
    private Scene displayScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.inventory = new Inventory();

        initialize();

        mainLayout = createMainLayout();
        mainScene = new Scene(mainLayout, 400, 300);

        displayLayout = new VBox(); // Empty layout for displaying items
        displayScene = new Scene(displayLayout, 600, 400);

        stage.setScene(mainScene);
        stage.setTitle("Inventory Management System");
        stage.show();
    }

    private void initialize() {
        inventory.addItem(new Item(1, "Product 1", 10, 15.0));
        inventory.addItem(new Item(2, "Product 2", 20, 25.0));
    }

    private VBox createMainLayout() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Button addButton = new Button("Add Item");
        addButton.setOnAction(event -> showAddItemDialog());

        Button displayButton = new Button("Display Items");
        displayButton.setOnAction(event -> displayItems());

        Button searchButton = new Button("Search Item");
        searchButton.setOnAction(event -> showSearchItemDialog());

        layout.getChildren().addAll(addButton, displayButton, searchButton);
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

        Optional<Item> result = dialog.showAndWait();
        result.ifPresent(item -> inventory.addItem(item));
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

        // Sorting button
        Button sortButton = new Button("Sort by Name");
        sortButton.setOnAction(event -> sortItemsByName(table));

        // Back button
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> stage.setScene(mainScene));

        HBox buttonBox = new HBox(10, sortButton, backButton);
        buttonBox.setPadding(new Insets(10));

        displayLayout.getChildren().clear(); // Clear previous layout
        displayLayout.getChildren().addAll(buttonBox, table);

        stage.setScene(displayScene);
    }

    private void sortItemsByName(TableView<Item> table) {
        table.getItems().sort((item1, item2) -> {
            // Compare based on name
            return item1.getName().compareTo(item2.getName());
        });
    }


    private void showSearchItemDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Item");
        dialog.setHeaderText("Enter Item Name to Search:");
        dialog.setContentText("Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            if (!name.isEmpty()) {
                Item item = inventory.searchItemByName(name);
                if (item != null) {
                    showAlert("Item Found", "ID: " + item.getId() + "\nName: " + item.getName() + "\nQuantity: " + item.getQuantity() + "\nPrice: " + item.getPrice());
                } else {
                    showAlert("Item Not Found", "Item with name \"" + name + "\" not found.");
                }
            } else {
                showAlert("Error", "Please enter a valid name.");
            }
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
