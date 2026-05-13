package view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.RetailSystem;
import model.Transaction;
import java.util.ArrayList;
import controller.TransactionController;

// cash transaction management view
public class CashTransactionView extends View{

    private RetailSystem system;
    private VBox view;

    private TableView<Transaction> table; // transaction history table
    private Label balanceLabel;           // shows current total balance
    private Label statusLabel;            // shows save/error messages
    private TextField amountField;
    private TextField descField;
    
    private ArrayList<Button> cashTransBtns;
    private TransactionController controller;

    /* CashTransactionView constructor:
     * - stores references and builds the view */
    public CashTransactionView(RetailSystem system) {
        this.system = system;
        this.buildView();
    }

    /* buildView method:
     * - constructs the full cash transaction layout
     * - includes balance display, cash in/out form, and transaction table */
    private void buildView() {
        Label pageTitle = new Label("Cash Transaction Management");
        pageTitle.getStyleClass().add("page-title");

        // balance display
        Label balanceTitle = new Label("Current Balance");
        balanceTitle.getStyleClass().add("field-label");

        this.balanceLabel = new Label("PHP " + String.format("%.2f", this.system.getTotalBalance()));
        this.balanceLabel.getStyleClass().add("balance-label");

        VBox balanceBox = new VBox(4, balanceTitle, this.balanceLabel);
        balanceBox.setPadding(new Insets(16));
        balanceBox.getStyleClass().add("summary-card");

        // cash in/out form
        Label formTitle = new Label("Add Transaction");
        formTitle.getStyleClass().add("field-label");

        Label amountLabel = new Label("Amount (PHP)");
        amountLabel.getStyleClass().add("field-label");

        this.amountField = new TextField();
        amountField.setPromptText("e.g. 500");
        amountField.getStyleClass().add("login-input");
        amountField.setMaxWidth(150);

        Label descLabel = new Label("Description");
        descLabel.getStyleClass().add("field-label");

        this.descField = new TextField();
        descField.setPromptText("e.g. Opening cash");
        descField.getStyleClass().add("login-input");
        descField.setMaxWidth(220);

        this.statusLabel = new Label("");
        this.statusLabel.getStyleClass().add("info-label");

        Button cashInBtn = new Button("Cash In");
        cashInBtn.setId("cash in");
        cashInBtn.getStyleClass().add("login-button");
        cashInBtn.setMaxWidth(120);

        Button cashOutBtn = new Button("Cash Out");
        cashOutBtn.setId("cash out");
        cashOutBtn.getStyleClass().add("secondary-btn");
        cashOutBtn.setMaxWidth(120);
        
        this.cashTransBtns = new ArrayList<>();
        this.cashTransBtns.add(cashInBtn);
        this.cashTransBtns.add(cashOutBtn);
        
        HBox fieldRow = new HBox(12, amountLabel, amountField, descLabel, descField);
        fieldRow.setAlignment(Pos.CENTER_LEFT);

        HBox btnRow = new HBox(12, cashInBtn, cashOutBtn);
        btnRow.setAlignment(Pos.CENTER_LEFT);

        VBox form = new VBox(8, formTitle, fieldRow, btnRow, this.statusLabel);
        form.setPadding(new Insets(16));
        form.getStyleClass().add("summary-card");

        // top row - balance and form side by side
        HBox topRow = new HBox(16, balanceBox, form);
        topRow.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(form, javafx.scene.layout.Priority.ALWAYS);

        // transaction table
        Label tableTitle = new Label("Transaction History");
        tableTitle.getStyleClass().add("field-label");

        this.buildTable();

        this.view = new VBox(16, pageTitle, topRow, tableTitle, this.table);
        this.view.setPadding(new Insets(24));
        
        this.controller = new TransactionController(system,this);
        this.controller.initializeController();
    }

    /* buildTable method:
     * - constructs the transaction history TableView */
    private void buildTable() {
        this.table = new TableView<Transaction>();
        this.table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.table.getStyleClass().add("table-view");

        TableColumn<Transaction, String> idCol = new TableColumn<Transaction, String>("ID");
        idCol.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Transaction, String>, javafx.beans.value.ObservableValue<String>>() {
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Transaction, String> data) {
                return new SimpleStringProperty(data.getValue().getTransactionId());
            }
        });

        TableColumn<Transaction, String> typeCol = new TableColumn<Transaction, String>("Type");
        typeCol.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Transaction, String>, javafx.beans.value.ObservableValue<String>>() {
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Transaction, String> data) {
                return new SimpleStringProperty(data.getValue().getType());
            }
        });

        TableColumn<Transaction, Number> amountCol = new TableColumn<Transaction, Number>("Amount (PHP)");
        amountCol.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Transaction, Number>, javafx.beans.value.ObservableValue<Number>>() {
            public javafx.beans.value.ObservableValue<Number> call(TableColumn.CellDataFeatures<Transaction, Number> data) {
                return new SimpleDoubleProperty(data.getValue().getAmount());
            }
        });

        TableColumn<Transaction, String> descCol = new TableColumn<Transaction, String>("Description");
        descCol.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Transaction, String>, javafx.beans.value.ObservableValue<String>>() {
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Transaction, String> data) {
                return new SimpleStringProperty(data.getValue().getDescription());
            }
        });

        TableColumn<Transaction, String> timestampCol = new TableColumn<Transaction, String>("Timestamp");
        timestampCol.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Transaction, String>, javafx.beans.value.ObservableValue<String>>() {
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Transaction, String> data) {
                return new SimpleStringProperty(data.getValue().getTimestamp());
            }
        });

        this.table.getColumns().add(idCol);
        this.table.getColumns().add(typeCol);
        this.table.getColumns().add(amountCol);
        this.table.getColumns().add(descCol);
        this.table.getColumns().add(timestampCol);

        this.refreshView();
    }

    /* refreshTable method:
     * - clears and repopulates the transaction table */
    public void refreshView() {
        this.table.getItems().clear();
        for (int i = 0; i < this.system.getTransactions().size(); i++) {
            this.table.getItems().add(this.system.getTransactions().get(i));
        }
        Double currentBalance = this.system.getTotalBalance();
        String balanceText = "PHP " + String.format("%.2f", currentBalance);
        this.updateBalanceText(balanceText);
    }

    /* getView method:
     * - returns the built view node to be placed in the content area */
    public VBox getView() {
        return this.view;
    }
    
    public void updateBalanceText(String updatedBalance) {
    	this.balanceLabel.setText(updatedBalance);
    }
    
    public ArrayList<Button> getCashTransBtns(){
    	return this.cashTransBtns;
    }
    
    public String getAmountInput() {
    	return amountField.getText().trim();
    }
    
    public String getDescInput() {
    	return this.descField.getText().trim();
    }
    
    public void setStatusLabel(String newLabel) {
    	this.statusLabel.setText(newLabel);
    }
    
    public void clearAmountField() {
    	this.amountField.clear();
    }
    
    public void clearDescField() {
    	this.descField.clear();
    }

}