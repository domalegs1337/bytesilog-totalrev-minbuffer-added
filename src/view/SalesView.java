package view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.RetailSystem;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import controller.SalesController;

// sales management view
public class SalesView extends View{

    private RetailSystem system;
    private VBox view;

    private TableView<MenuItem> menuTable;   // left: menu items
    private TableView<OrderItem> orderTable; // right: current order
    private Order currentOrder;             // the order being built
    private Label totalLabel;               // shows current order total
    private Label changeLabel;              // shows change after payment
    private Label statusLabel;              // shows success/error messages
    
    private ArrayList<Button> salesBtns;
    private ArrayList<TextField> salesInputFields;
    
    private TextField qtyField;
    private TextField paidField;
    
    private SalesController controller;

    /* SalesView constructor:
     * - stores references and builds the view */
    public SalesView(RetailSystem system) {
        this.system = system;
        this.currentOrder = new Order(
            this.system.generateOrderId(),
            new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())
        );
        this.buildView();
    }

    /* buildView method:
     * - constructs the full sales layout
     * - left panel: menu items, right panel: current order */
    private void buildView() {
        Label pageTitle = new Label("Sales Management");
        pageTitle.getStyleClass().add("page-title");

        // left panel - menu selection
        Label menuTitle = new Label("Menu Items");
        menuTitle.getStyleClass().add("field-label");

        this.menuTable = this.buildMenuTable();

        Label qtyLabel = new Label("Quantity");
        qtyLabel.getStyleClass().add("field-label");

        this.qtyField = new TextField();
        this.qtyField.setId("quantity field");
        this.qtyField.setPromptText("e.g. 1");
        this.qtyField.getStyleClass().add("login-input");
        this.qtyField.setMaxWidth(80);
        
        this.salesInputFields = new ArrayList<>();
        this.salesInputFields.add(qtyField);

        Button addBtn = new Button("Add to Order");
        addBtn.setId("add order");
        addBtn.getStyleClass().add("login-button");
        
        this.salesBtns = new ArrayList<>();
        this.salesBtns.add(addBtn);

        HBox addRow = new HBox(12, qtyLabel, qtyField, addBtn);
        addRow.setAlignment(Pos.CENTER_LEFT);

        VBox leftPanel = new VBox(12, menuTitle, this.menuTable, addRow);
        leftPanel.setPadding(new Insets(16));
        leftPanel.getStyleClass().add("summary-card");
        HBox.setHgrow(leftPanel, Priority.ALWAYS);

        // right panel - current order
        Label orderTitle = new Label("Current Order");
        orderTitle.getStyleClass().add("field-label");

        this.orderTable = this.buildOrderTable();

        Label totalTitle = new Label("Total");
        totalTitle.getStyleClass().add("field-label");

        this.totalLabel = new Label("PHP 0.00");
        this.totalLabel.getStyleClass().add("balance-label");

        Label paidLabel = new Label("Amount Paid (PHP)");
        paidLabel.getStyleClass().add("field-label");

        this.paidField = new TextField();
        this.paidField.setId("paid field");
        this.paidField.setPromptText("e.g. 100");
        this.paidField.getStyleClass().add("login-input");
        this.paidField.setMaxWidth(150);
        
        
        this.salesInputFields.add(paidField);

        this.changeLabel = new Label("Change: PHP 0.00");
        this.changeLabel.getStyleClass().add("info-label");

        this.statusLabel = new Label("");
        this.statusLabel.getStyleClass().add("info-label");

        Button placeOrderBtn = new Button("Place Order");
        placeOrderBtn.setId("place order");
        placeOrderBtn.getStyleClass().add("login-button");
        placeOrderBtn.setMaxWidth(Double.MAX_VALUE);

        Button clearBtn = new Button("Clear Order");
        clearBtn.setId("clear order");
        clearBtn.getStyleClass().add("secondary-btn");
        clearBtn.setMaxWidth(Double.MAX_VALUE);
        
        this.salesBtns.add(placeOrderBtn);
        this.salesBtns.add(clearBtn);

        HBox paidRow = new HBox(12, paidLabel, paidField);
        paidRow.setAlignment(Pos.CENTER_LEFT);

        HBox actionRow = new HBox(12, placeOrderBtn, clearBtn);

        VBox rightPanel = new VBox(12,
            orderTitle,
            this.orderTable,
            totalTitle,
            this.totalLabel,
            paidRow,
            this.changeLabel,
            actionRow,
            this.statusLabel
        );
        rightPanel.setPadding(new Insets(16));
        rightPanel.getStyleClass().add("summary-card");
        rightPanel.setMinWidth(320);

        HBox mainRow = new HBox(16, leftPanel, rightPanel);
        mainRow.setAlignment(Pos.TOP_LEFT);

        this.view = new VBox(16, pageTitle, mainRow);
        this.view.setPadding(new Insets(24));
        
        this.controller = new SalesController(this.system,this);
        this.controller.initializeController();
    }

    /* buildMenuTable method:
     * - constructs the menu item selection table */
    private TableView<MenuItem> buildMenuTable() {
        TableView<MenuItem> tableView = new TableView<MenuItem>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getStyleClass().add("table-view");

        TableColumn<MenuItem, String> nameCol = new TableColumn<MenuItem, String>("Item");
        nameCol.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<MenuItem, String>, javafx.beans.value.ObservableValue<String>>() {
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<MenuItem, String> data) {
                return new SimpleStringProperty(data.getValue().getName());
            }
        });

        TableColumn<MenuItem, Number> priceCol = new TableColumn<MenuItem, Number>("Price (PHP)");
        priceCol.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<MenuItem, Number>, javafx.beans.value.ObservableValue<Number>>() {
            public javafx.beans.value.ObservableValue<Number> call(TableColumn.CellDataFeatures<MenuItem, Number> data) {
                return new SimpleDoubleProperty(data.getValue().getSellingPrice());
            }
        });

        tableView.getColumns().add(nameCol);
        tableView.getColumns().add(priceCol);

        for (int i = 0; i < this.system.getMenuItems().size(); i++) {
            tableView.getItems().add(this.system.getMenuItems().get(i));
        }

        return tableView;
    }

    /* buildOrderTable method:
     * - constructs the current order summary table */
    private TableView<OrderItem> buildOrderTable() {
        TableView<OrderItem> tableView = new TableView<OrderItem>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getStyleClass().add("table-view");

        TableColumn<OrderItem, String> nameCol = new TableColumn<OrderItem, String>("Item");
        nameCol.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<OrderItem, String>, javafx.beans.value.ObservableValue<String>>() {
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<OrderItem, String> data) {
                return new SimpleStringProperty(data.getValue().getMenuItem().getName());
            }
        });

        TableColumn<OrderItem, Number> qtyCol = new TableColumn<OrderItem, Number>("Qty");
        qtyCol.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<OrderItem, Number>, javafx.beans.value.ObservableValue<Number>>() {
            public javafx.beans.value.ObservableValue<Number> call(TableColumn.CellDataFeatures<OrderItem, Number> data) {
                return new SimpleIntegerProperty(data.getValue().getQuantity());
            }
        });

        TableColumn<OrderItem, Number> subtotalCol = new TableColumn<OrderItem, Number>("Subtotal (PHP)");
        subtotalCol.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<OrderItem, Number>, javafx.beans.value.ObservableValue<Number>>() {
            public javafx.beans.value.ObservableValue<Number> call(TableColumn.CellDataFeatures<OrderItem, Number> data) {
                return new SimpleDoubleProperty(data.getValue().getSubtotal());
            }
        });

        tableView.getColumns().add(nameCol);
        tableView.getColumns().add(qtyCol);
        tableView.getColumns().add(subtotalCol);

        return tableView;
    }

    public void refreshMenuTable() {
    	this.menuTable.getItems().clear();
    	for (int i = 0; i < this.system.getMenuItems().size(); i++) {
    		this.menuTable.getItems().add(this.system.getMenuItems().get(i));
        }
    }
    
    public void updateOrderTable(MenuItem selected, OrderItem orderItem, int qty) {
    	this.currentOrder.addItem(orderItem);
        this.currentOrder.computeTotal();

        this.orderTable.getItems().add(orderItem);
        this.totalLabel.setText("PHP " + String.format("%.2f", this.currentOrder.getTotalAmount()));

        qtyField.clear();
        this.statusLabel.setText("Added " + qty + "x " + selected.getName() + ".");
    }
    
    public void startNewOrder() {
    	// start a new order
        this.currentOrder = new Order(
            this.system.generateOrderId(),
            new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())
        );

        this.orderTable.getItems().clear();
        this.totalLabel.setText("PHP 0.00");
        this.changeLabel.setText("Change: PHP 0.00");
        paidField.clear();
    }

    /* getView method:
     * - returns the built view node to be placed in the content area */
    public VBox getView() {
        return this.view;
    }
    
    public ArrayList<Button> getSalesBtns(){
    	return this.salesBtns;
    }
    
    public ArrayList<TextField> getSalesInputFields(){
    	return this.salesInputFields;
    }
    
    public MenuItem getSelectedMenuItem() {
    	return this.menuTable.getSelectionModel().getSelectedItem();
    }
    
    public String getQuantityInput() {
    	return this.qtyField.getText().trim();
    }
    
    public String getPaidInput() {
    	return this.paidField.getText().trim();
    }
    
    public Order getCurrentOrder(){
    	return this.currentOrder;
    }
    
    public void setStatusLabel(String newLabel) {
    	this.statusLabel.setText(newLabel);
    }
    public void setChangeLabel(String newLabel) {
    	this.changeLabel.setText(newLabel);
    }
}