package view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Ingredient;
import model.RetailSystem;
import java.util.ArrayList;

import controller.InventoryController;

// inventory management view
// readOnly = true limits to view-only mode for cashier
public class InventoryView extends View{

    private RetailSystem system;
    private boolean readOnly;            // true if accessed by cashier
    private VBox view;

    private TableView<Ingredient> table; // main ingredient table
    private Label statusLabel;           // shows save/error messages
    private boolean showLowStockOnly;    // toggle for low stock filter
    
    private ArrayList<Button> inventoryBtns;
    private InventoryController controller;
    private TextField qtyField;
    private TextField packField;
	private TextField priceField;

    /* InventoryView constructor:
     * - stores references and builds the view */
    public InventoryView(RetailSystem system, boolean readOnly) {
        this.system = system;
        this.readOnly = readOnly;
        this.showLowStockOnly = false;
        this.buildView();
    }

    /* buildView method:
     * - constructs the full inventory layout */
    private void buildView() {
        Label pageTitle = new Label("Inventory Management");
        pageTitle.getStyleClass().add("page-title");

        // search bar
        Label searchLabel = new Label("Search");
        searchLabel.getStyleClass().add("field-label");

        TextField searchField = new TextField();
        searchField.setPromptText("Search ingredient...");
        searchField.getStyleClass().add("login-input");
        searchField.setMaxWidth(220);

        // low stock filter toggle
        ToggleButton lowStockToggle = new ToggleButton("Show Low Stock Only");
        lowStockToggle.getStyleClass().add("secondary-btn");

        HBox searchRow = new HBox(12, searchLabel, searchField, lowStockToggle);
        searchRow.setAlignment(Pos.CENTER_LEFT);

        this.table = this.buildTable();

        // filter table as user types
        searchField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                filterTable(searchField.getText().trim(), lowStockToggle.isSelected());
            }
        });

        lowStockToggle.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                showLowStockOnly = lowStockToggle.isSelected();
                filterTable(searchField.getText().trim(), showLowStockOnly);
            }
        });

        if (this.readOnly) {
            Label readOnlyLabel = new Label("View only - contact manager to restock");
            readOnlyLabel.getStyleClass().add("low-stock-label");

            this.view = new VBox(12, pageTitle, searchRow, readOnlyLabel, this.table);
            this.view.setPadding(new Insets(24));
            return;
        }

        this.statusLabel = new Label("");
        this.statusLabel.getStyleClass().add("info-label");

        // restock form
        VBox restockForm = this.buildRestockForm();

        // edit market price form
        VBox editPriceForm = this.buildEditPriceForm();

        HBox formsRow = new HBox(16, restockForm, editPriceForm);
        HBox.setHgrow(restockForm, Priority.ALWAYS);
        HBox.setHgrow(editPriceForm, Priority.ALWAYS);

        this.view = new VBox(16, pageTitle, searchRow, formsRow, this.statusLabel, this.table);
        this.view.setPadding(new Insets(24));

        controller = new InventoryController(system,this);
        controller.initializeController();
    }

    /* buildRestockForm method:
     * - builds the restock by pack form */
    private VBox buildRestockForm() {
        Label title = new Label("Restock Ingredient");
        title.getStyleClass().add("field-label");

        Label qtyLabel = new Label("Quantity to Add");
        qtyLabel.getStyleClass().add("field-label");

        this.qtyField = new TextField();
        qtyField.setPromptText("Enter units");
        qtyField.getStyleClass().add("login-input");
        qtyField.setMaxWidth(120);

        Label orLabel = new Label("or");
        orLabel.getStyleClass().add("info-label");

        Label packLabel = new Label("Packs to Add");
        packLabel.getStyleClass().add("field-label");

        this.packField = new TextField();
        packField.setPromptText("Enter packs");
        packField.getStyleClass().add("login-input");
        packField.setMaxWidth(120);
        
        Button restockByPackBtn = new Button("Restock by Pack");
        restockByPackBtn.setId("by packs");
        restockByPackBtn.getStyleClass().add("login-button");
        
        this.inventoryBtns = new ArrayList<>();
        this.inventoryBtns.add(restockByPackBtn);
        
        HBox packRow = new HBox(8, packLabel, packField, restockByPackBtn);
        packRow.setAlignment(Pos.CENTER_LEFT);

        VBox form = new VBox(8, title,packRow);
        form.setPadding(new Insets(16));
        form.getStyleClass().add("summary-card");

        return form;
    }

    /* buildEditPriceForm method:
     * - builds the market price editor form */
    private VBox buildEditPriceForm() {
        Label title = new Label("Edit Market Price");
        title.getStyleClass().add("field-label");

        Label priceLabel = new Label("New Market Price (PHP)");
        priceLabel.getStyleClass().add("field-label");

        this.priceField = new TextField();
        priceField.setPromptText("e.g. 250");
        priceField.getStyleClass().add("login-input");
        priceField.setMaxWidth(120);

        Label previewLabel = new Label("New unit cost: -");
        previewLabel.getStyleClass().add("info-label");

        // show preview as user types
        priceField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                Ingredient selected = table.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    previewLabel.setText("Select an ingredient first.");
                    return;
                }
                try {
                    double newPrice = Double.parseDouble(priceField.getText().trim());
                    double newUnitCost = newPrice / selected.getUnitsPerPack();
                    previewLabel.setText("New unit cost: PHP " + String.format("%.4f", newUnitCost));
                } catch (NumberFormatException e) {
                    previewLabel.setText("New unit cost: -");
                }
            }
        });

        Button applyBtn = new Button("Apply Price");
        applyBtn.setId("apply price");
        applyBtn.getStyleClass().add("login-button");
        
        this.inventoryBtns.add(applyBtn);

        HBox priceRow = new HBox(8, priceLabel, priceField, applyBtn);
        priceRow.setAlignment(Pos.CENTER_LEFT);

        VBox form = new VBox(8, title, priceRow, previewLabel);
        form.setPadding(new Insets(16));
        form.getStyleClass().add("summary-card");

        return form;
    }

    /* buildTable method:
     * - constructs the ingredient TableView with all columns */
    private TableView<Ingredient> buildTable() {
        TableView<Ingredient> tableView = new TableView<Ingredient>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getStyleClass().add("table-view");

        TableColumn<Ingredient, String> idCol = new TableColumn<Ingredient, String>("ID");
        idCol.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Ingredient, String>, javafx.beans.value.ObservableValue<String>>() {
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Ingredient, String> data) {
                return new SimpleStringProperty(data.getValue().getIngredientId());
            }
        });

        TableColumn<Ingredient, String> nameCol = new TableColumn<Ingredient, String>("Name");
        nameCol.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Ingredient, String>, javafx.beans.value.ObservableValue<String>>() {
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Ingredient, String> data) {
                return new SimpleStringProperty(data.getValue().getName());
            }
        });

        TableColumn<Ingredient, String> unitCol = new TableColumn<Ingredient, String>("Unit");
        unitCol.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Ingredient, String>, javafx.beans.value.ObservableValue<String>>() {
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Ingredient, String> data) {
                return new SimpleStringProperty(data.getValue().getUnit());
            }
        });

        TableColumn<Ingredient, Number> stockCol = new TableColumn<Ingredient, Number>("Stock");
        stockCol.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Ingredient, Number>, javafx.beans.value.ObservableValue<Number>>() {
            public javafx.beans.value.ObservableValue<Number> call(TableColumn.CellDataFeatures<Ingredient, Number> data) {
                return new SimpleDoubleProperty(data.getValue().getStockQty());
            }
        });

        TableColumn<Ingredient, Number> unitsPerPackCol = new TableColumn<Ingredient, Number>("Units/Pack");
        unitsPerPackCol.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Ingredient, Number>, javafx.beans.value.ObservableValue<Number>>() {
            public javafx.beans.value.ObservableValue<Number> call(TableColumn.CellDataFeatures<Ingredient, Number> data) {
                return new SimpleDoubleProperty(data.getValue().getUnitsPerPack());
            }
        });

        TableColumn<Ingredient, Number> marketPriceCol = new TableColumn<Ingredient, Number>("Market Price (PHP)");
        marketPriceCol.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Ingredient, Number>, javafx.beans.value.ObservableValue<Number>>() {
            public javafx.beans.value.ObservableValue<Number> call(TableColumn.CellDataFeatures<Ingredient, Number> data) {
                return new SimpleDoubleProperty(data.getValue().getMarketPrice());
            }
        });

        TableColumn<Ingredient, Number> unitCostCol = new TableColumn<Ingredient, Number>("Unit Cost");
        unitCostCol.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Ingredient, Number>, javafx.beans.value.ObservableValue<Number>>() {
            public javafx.beans.value.ObservableValue<Number> call(TableColumn.CellDataFeatures<Ingredient, Number> data) {
                return new SimpleDoubleProperty(data.getValue().getUnitCost());
            }
        });

        TableColumn<Ingredient, String> statusCol = new TableColumn<Ingredient, String>("Status");
        statusCol.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Ingredient, String>, javafx.beans.value.ObservableValue<String>>() {
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Ingredient, String> data) {
                if (data.getValue().isLowStock()) {
                    return new SimpleStringProperty("LOW STOCK");
                }
                return new SimpleStringProperty("OK");
            }
        });

        tableView.getColumns().add(idCol);
        tableView.getColumns().add(nameCol);
        tableView.getColumns().add(unitCol);
        tableView.getColumns().add(stockCol);
        tableView.getColumns().add(unitsPerPackCol);
        tableView.getColumns().add(marketPriceCol);
        tableView.getColumns().add(unitCostCol);
        tableView.getColumns().add(statusCol);

        this.populateTable(tableView);

        return tableView;
    }

    /* populateTable method:
     * - fills the table with the given ingredient list */
    public void populateTable() {
    	ArrayList<Ingredient> list = this.system.getIngredients();
        this.table.getItems().clear();
        for (int i = 0; i < list.size(); i++) {
            this.table.getItems().add(list.get(i));
        }
    }
    
    public void populateTable(TableView<Ingredient> tableView) {
    	ArrayList<Ingredient> list = this.system.getIngredients();
        tableView.getItems().clear();
        for (int i = 0; i < list.size(); i++) {
            tableView.getItems().add(list.get(i));
        }
    }
    
    private void populateTable(ArrayList<Ingredient> filtered) {
    	this.table.getItems().clear();
        for (int i = 0; i < filtered.size(); i++) {
            this.table.getItems().add(filtered.get(i));
        }
    }

    /* filterTable method:
     * - filters the table by search query and/or low stock toggle */
    private void filterTable(String query, boolean lowStockOnly) {
        ArrayList<Ingredient> filtered = new ArrayList<Ingredient>();
        for (int i = 0; i < this.system.getIngredients().size(); i++) {
            Ingredient ing = this.system.getIngredients().get(i);
            boolean matchesSearch = query.isEmpty() ||
                ing.getName().toLowerCase().contains(query.toLowerCase()) ||
                ing.getIngredientId().toLowerCase().contains(query.toLowerCase());
            boolean matchesLowStock = !lowStockOnly || ing.isLowStock();
            if (matchesSearch && matchesLowStock) {
                filtered.add(ing);
            }
        }
        this.populateTable(filtered);
    }
    
    public ArrayList<Button> getInventoryBtns() {
    	return this.inventoryBtns;
    }
    
    public TableView<Ingredient> getTable(){
    	return this.table;
    }
    
    public void setStatusLabel(String newLabelText) {
    	this.statusLabel.setText(newLabelText);
    }
    
    public Ingredient getSelectedIngredient() {
    	return this.table.getSelectionModel().getSelectedItem();
    }
    
    public String getQtyInput() {
    	return qtyField.getText().trim();
    }
    
    public String getPackInput() {
    	return packField.getText().trim();
    }
    
    public void clearQtyInput() {
    	this.qtyField.clear();
    }
    
    public void clearPackInput() {
    	this.packField.clear();
    }
    
    public void clearPriceInput() {
    	this.priceField.clear();
    	this.statusLabel.setText("New unit cost: -");
    }
    
    public String getPriceInput() {
    	return this.priceField.getText().trim();
    }
    
    public void refreshTable() {
    	this.populateTable();
    }
    
    /* getView method:
     * - returns the built view node to be placed in the content area */
    public VBox getView() {
        return this.view;
    }

}