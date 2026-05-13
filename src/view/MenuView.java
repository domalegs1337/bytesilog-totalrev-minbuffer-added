package view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.MenuItem;
import model.RetailSystem;
import java.util.ArrayList;

import controller.MenuController;

// menu management view - manager only
public class MenuView extends View{

    private RetailSystem system;
    private VBox view;

    private TableView<MenuItem> table; // main menu item table
    private Label statusLabel;         // shows save/error messages
    
    private ArrayList<Button> menuButtonList;
    private ArrayList<TextField> inputFields;
	private TextField profitField;
	private Label profitPreviewLabel;
	private Label laborPreviewLabel;
	private TextField laborField;
	
	private MenuController controller;
	private TextField bulkField;
	
	/* MenuView constructor:
     * - stores references and builds the view */
    public MenuView(RetailSystem system) {
        this.system = system;
        this.buildView();
    }

    /* buildView method:
     * - constructs the full menu layout with all edit forms */
    private void buildView() {
        Label pageTitle = new Label("Menu Management");
        pageTitle.getStyleClass().add("page-title");

        // search bar
        Label searchLabel = new Label("Search");
        searchLabel.getStyleClass().add("field-label");

        TextField searchField = new TextField();
        searchField.setPromptText("Search menu item...");
        searchField.getStyleClass().add("login-input");
        searchField.setMaxWidth(220);

        // category filter
        Label categoryLabel = new Label("Category");
        categoryLabel.getStyleClass().add("field-label");

        ComboBox<String> categoryBox = new ComboBox<String>();
        categoryBox.getItems().addAll("All", "Silog", "Drinks");
        categoryBox.setValue("All");
        categoryBox.getStyleClass().add("login-input");

        HBox searchRow = new HBox(12, searchLabel, searchField, categoryLabel, categoryBox);
        searchRow.setAlignment(Pos.CENTER_LEFT);

        this.statusLabel = new Label("");
        this.statusLabel.getStyleClass().add("info-label");

        // edit forms row
        VBox profitForm = this.buildProfitForm();
        VBox laborForm = this.buildLaborForm();
        VBox bulkForm = this.buildBulkForm();

        HBox formsRow = new HBox(16, profitForm, laborForm, bulkForm);
        HBox.setHgrow(profitForm, Priority.ALWAYS);
        HBox.setHgrow(laborForm, Priority.ALWAYS);
        HBox.setHgrow(bulkForm, Priority.ALWAYS);

        this.table = this.buildTable();

        // filter table as user types
        searchField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                filterTable(searchField.getText().trim(), categoryBox.getValue());
            }
        });

        categoryBox.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                filterTable(searchField.getText().trim(), categoryBox.getValue());
            }
        });

        this.view = new VBox(16, pageTitle, searchRow, formsRow, this.statusLabel, this.table);
        this.view.setPadding(new Insets(24));
        
        this.controller = new MenuController(this.system,this);
        this.controller.initializeController();
    }

    /* buildProfitForm method:
     * - builds the profit percent editor with live price preview */
    private VBox buildProfitForm() {
        Label title = new Label("Edit Profit Percent");
        title.getStyleClass().add("field-label");

        Label profitLabel = new Label("Profit (%)");
        profitLabel.getStyleClass().add("field-label");

        this.profitField = new TextField();
        this.profitField.setId("profit field");
        this.profitField.setPromptText("e.g. 20");
        this.profitField.getStyleClass().add("login-input");
        this.profitField.setMaxWidth(100);
        
        this.inputFields = new ArrayList<>();
        this.inputFields.add(this.profitField);

        this.profitPreviewLabel = new Label("New price: -");
        this.profitPreviewLabel.getStyleClass().add("info-label");
        
        Button applyBtn = new Button("Apply");
        applyBtn.setId("apply profit");
        applyBtn.getStyleClass().add("login-button");
        applyBtn.setMaxWidth(120);
        
        this.menuButtonList = new ArrayList<>();
        this.menuButtonList.add(applyBtn);
        
        HBox row = new HBox(8, profitLabel, profitField, applyBtn);
        row.setAlignment(Pos.CENTER_LEFT);

        VBox form = new VBox(8, title, row, this.profitPreviewLabel);
        form.setPadding(new Insets(16));
        form.getStyleClass().add("summary-card");

        return form;
    }

    /* buildLaborForm method:
     * - builds the labor cost editor */
    private VBox buildLaborForm() {
        Label title = new Label("Edit Labor Cost");
        title.getStyleClass().add("field-label");

        Label laborLabel = new Label("Labor (PHP)");
        laborLabel.getStyleClass().add("field-label");

        this.laborField = new TextField();
        this.laborField.setId("labor field");
        this.laborField.setPromptText("e.g. 5");
        this.laborField.getStyleClass().add("login-input");
        this.laborField.setMaxWidth(100);
        
        this.inputFields.add(this.laborField);

        this.laborPreviewLabel = new Label("New price: -");
        this.laborPreviewLabel.getStyleClass().add("info-label");

        Button applyBtn = new Button("Apply");
        applyBtn.setId("apply labor");
        applyBtn.getStyleClass().add("login-button");
        applyBtn.setMaxWidth(120);
        
        this.menuButtonList.add(applyBtn);

        HBox row = new HBox(8, laborLabel, laborField, applyBtn);
        row.setAlignment(Pos.CENTER_LEFT);

        VBox form = new VBox(8, title, row, this.laborPreviewLabel);
        form.setPadding(new Insets(16));
        form.getStyleClass().add("summary-card");

        return form;
    }

    /* buildBulkForm method:
     * - builds the bulk profit percent update form */
    private VBox buildBulkForm() {
        Label title = new Label("Bulk Update Profit");
        title.getStyleClass().add("field-label");

        Label profitLabel = new Label("Profit (%) for All");
        profitLabel.getStyleClass().add("field-label");

        this.bulkField = new TextField();
        this.bulkField.setPromptText("e.g. 25");
        this.bulkField.getStyleClass().add("login-input");
        this.bulkField.setMaxWidth(100);

        Label infoLabel = new Label("Applies to all menu items");
        infoLabel.getStyleClass().add("info-label");

        Button applyAllBtn = new Button("Apply to All");
        applyAllBtn.setId("apply to all");
        applyAllBtn.getStyleClass().add("secondary-btn");
        applyAllBtn.setMaxWidth(140);
        this.menuButtonList.add(applyAllBtn);
        
        HBox row = new HBox(8, profitLabel, bulkField, applyAllBtn);
        row.setAlignment(Pos.CENTER_LEFT);

        VBox form = new VBox(8, title, row, infoLabel);
        form.setPadding(new Insets(16));
        form.getStyleClass().add("summary-card");

        return form;
    }

    /* buildTable method:
     * - constructs the menu item TableView with all columns */
    private TableView<MenuItem> buildTable() {
    	// reference: https://www.tutorialspoint.com/javafx/javafx_tableview.htm
    	
        TableView<MenuItem> tableView = new TableView<MenuItem>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getStyleClass().add("table-view");

        TableColumn<MenuItem, String> idCol = new TableColumn<MenuItem, String>("ID");
        idCol.setCellValueFactory(id -> new SimpleStringProperty(id.getValue().getItemId()));

        TableColumn<MenuItem, String> nameCol = new TableColumn<MenuItem, String>("Name");
        nameCol.setCellValueFactory(name -> new SimpleStringProperty(name.getValue().getName()));

        TableColumn<MenuItem, String> descCol = new TableColumn<MenuItem, String>("Description");
        descCol.setCellValueFactory(desc -> new SimpleStringProperty(desc.getValue().getDescription()));

        TableColumn<MenuItem, Number> ingredientCostCol = new TableColumn<MenuItem, Number>("Ingredient Cost");
        ingredientCostCol.setCellValueFactory(cost -> new SimpleDoubleProperty(cost.getValue().getRoundedCost()));

        TableColumn<MenuItem, Number> laborCol = new TableColumn<MenuItem, Number>("Labor (PHP)");
        laborCol.setCellValueFactory(labor -> new SimpleDoubleProperty(labor.getValue().getLaborCost()));

        TableColumn<MenuItem, Number> profitCol = new TableColumn<MenuItem, Number>("Profit (%)");
        profitCol.setCellValueFactory(profit -> new SimpleDoubleProperty(profit.getValue().getProfitPercent()));

        TableColumn<MenuItem, Number> priceCol = new TableColumn<MenuItem, Number>("Selling Price (PHP)");
        priceCol.setCellValueFactory(price -> new SimpleDoubleProperty(price.getValue().getSellingPrice()));
        

        tableView.getColumns().add(idCol);
        tableView.getColumns().add(nameCol);
        tableView.getColumns().add(descCol);
        tableView.getColumns().add(ingredientCostCol);
        tableView.getColumns().add(laborCol);
        tableView.getColumns().add(profitCol);
        tableView.getColumns().add(priceCol);

        this.populateTable(tableView, this.system.getMenuItems());

        return tableView;
    }
    
    /* populateTable method:
     * - fills the table with the given menu item list */
    private void populateTable(TableView<MenuItem> tableView, ArrayList<MenuItem> list) {
        tableView.getItems().clear();
        for (int i = 0; i < list.size(); i++) {
            tableView.getItems().add(list.get(i));
        }
    }

    /* filterTable method:
     * - filters the table by search query and/or category */
    private void filterTable(String query, String category) {
        ArrayList<MenuItem> filtered = new ArrayList<MenuItem>();
        for (int i = 0; i < this.system.getMenuItems().size(); i++) {
            MenuItem item = this.system.getMenuItems().get(i);
            boolean matchesSearch = query.isEmpty() ||
                item.getName().toLowerCase().contains(query.toLowerCase()) ||
                item.getItemId().toLowerCase().contains(query.toLowerCase());
            boolean matchesCategory = true;
            if (category.equals("Silog")) {
                matchesCategory = item.getName().toLowerCase().contains("silog");
            } else if (category.equals("Drinks")) {
                matchesCategory = !item.getName().toLowerCase().contains("silog");
            }
            if (matchesSearch && matchesCategory) {
                filtered.add(item);
            }
        }
        this.populateTable(this.table, filtered);
    }

    public void refreshView() {
    	this.table.getItems().clear();
        for (int i = 0; i < this.system.getMenuItems().size(); i++) {
            this.table.getItems().add(this.system.getMenuItems().get(i));
        }
    }
    
    public void refreshProfitField() {
    	this.profitField.clear();
        this.profitPreviewLabel.setText("New price: -");
    }
    
    public void refreshLaborField() {
    	this.laborField.clear();
        this.laborPreviewLabel.setText("New price: -");
    }
    
    public void refreshBulkField() {
    	this.bulkField.clear();
    }
    /* getView method:
     * - returns the built view node to be placed in the content area */
    public VBox getView() {
        return this.view;
    }
    
    public ArrayList<Button> getMenuButtons(){
    	return this.menuButtonList;
    }
    
    public MenuItem getSelectedMenuItem() {
    	return this.table.getSelectionModel().getSelectedItem();
    }
    
    public String getProfitInput() {
    	return this.profitField.getText();
    }
    
    public String getLaborInput() {
    	return this.laborField.getText();
    }
    
    public String getBulkInput() {
    	return this.bulkField.getText();
    }
    
    public ArrayList<TextField> getInputFields(){
    	return this.inputFields;
    }
    
    public void setStatusLabel(String newLabel) {
    	this.statusLabel.setText(newLabel);
    }
    
    public void setProfitPreviewLabel(String newLabel) {
    	this.profitPreviewLabel.setText(newLabel);
    }
    
    public void setLaborPreviewLabel(String newLabel) {
    	this.laborPreviewLabel.setText(newLabel);
    }

}