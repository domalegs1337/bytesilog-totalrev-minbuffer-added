package view;

import java.util.ArrayList;

import controller.NavigationController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.RetailSystem;

// manager dashboard — full access to all modules
public class ManagerDashboardView {

    private Stage stage;
    private Scene scene;
    private RetailSystem system;
    private NavigationController controller;
    
    private InventoryView inventoryView;
    private MenuView menuView;
    private SalesHistoryView salesHistoryView;
    private SalesView salesView;
    private CashTransactionView transactionView;
    
    private static int NAV_VIEW_WIDTH = 1440;
    private static int NAV_VIEW_HEIGHT = 900;

    private StackPane contentArea; // swappable center content
    
    private ArrayList<Button> navButtons;

    /* ManagerDashboardView constructor:
     * - stores stage and system reference
     * - builds the scene */
    public ManagerDashboardView(Stage stage, RetailSystem system) {
        this.stage = stage;
        this.system = system;
        this.initializeViews();
        this.buildScene();
    }
    
    private void initializeViews() {
    	this.inventoryView = new InventoryView(system,false);
    	this.menuView = new MenuView(system);
    	this.salesHistoryView = new SalesHistoryView(system);
    	this.salesView = new SalesView(system);
    	this.transactionView = new CashTransactionView(system);
    }

    /* buildScene method:
     * - constructs the full manager dashboard with sidebar and content area */
    private void buildScene() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("general-page");

        this.contentArea = new StackPane();
        this.contentArea.getStyleClass().add("content-area");
        
        VBox sidebar = this.buildSidebar();
        root.setLeft(sidebar);
        
        this.showOverview();
        root.setCenter(this.contentArea);

        this.scene = new Scene(root, this.NAV_VIEW_WIDTH, this.NAV_VIEW_HEIGHT);
        this.scene.getStylesheets().add(
            getClass().getResource("/application/application.css").toExternalForm()
        );
        this.controller = new NavigationController(stage,system,this);
        this.controller.initializeController();
    }

    /* buildSidebar method:
     * - constructs the left navigation sidebar with all module buttons */
    private VBox buildSidebar() {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");
        sidebar.setSpacing(0);

        Label titleLabel = new Label("ByteSilog");
        titleLabel.getStyleClass().add("sidebar-title");
        Label roleLabel = new Label("Manager");
        roleLabel.getStyleClass().add("sidebar-subtitle");

        VBox brand = new VBox(4, titleLabel, roleLabel);
        brand.setPadding(new Insets(24, 20, 24, 20));

        Button overviewBtn = new Button("Overview");
        overviewBtn.setId("overview");
        
        Button inventoryBtn = new Button("Inventory");
        inventoryBtn.setId("inventory");
        
        Button menuBtn = new Button("Menu");
        menuBtn.setId("menu");
        
        Button salesBtn = new Button("Sales");
        salesBtn.setId("sales");
        
        Button salesHistoryBtn = new Button("Sales History");
        salesHistoryBtn.setId("sales history");
        
        Button cashBtn = new Button("Cash Transactions");
        cashBtn.setId("cash transactions");
        
        Button logoutBtn = new Button("Logout");
        logoutBtn.setId("logout");
        
        navButtons = new ArrayList<>();
        navButtons.add(overviewBtn);
        navButtons.add(inventoryBtn);
        navButtons.add(menuBtn);
        navButtons.add(salesBtn);
        navButtons.add(salesHistoryBtn);
        navButtons.add(cashBtn);
        navButtons.add(logoutBtn);
       
        this.setButtonProperties(); // calls method setButtonProperties to apply same style to the group
        
        VBox spacer = new VBox();
        VBox.setVgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        
       
   
       sidebar.getChildren().addAll(
            brand,
            overviewBtn,
            inventoryBtn,
            menuBtn,
            salesBtn,
            salesHistoryBtn,
            cashBtn,
            spacer,
            logoutBtn
        );

       return sidebar;
    }
      

    // applies the same style for the navigation buttons
    private void setButtonProperties() {
    	for(Button button: navButtons) {
    		if(button.getId().equals("logout")) {
    			button.getStyleClass().add("nav-logout-btn");
    			continue;
    		}
    		button.getStyleClass().add("nav-btn");
    	}
    }

    /* showOverview method:
     * - displays summary cards for the manager overview screen
     * - always reads fresh data from system on each call */
    public void showOverview() {
        Label pageTitle = new Label("Overview");
        pageTitle.getStyleClass().add("page-title");

        VBox balanceCard = this.buildCard(
            "Total Balance",
            "PHP " + String.format("%.2f", this.system.getTotalBalance())
        );
        
        VBox reserveCard = this.buildCard(
        	    "Required Reserve",
        	    "PHP " + String.format("%.2f", this.system.getMinimumBalance())
        	);

        VBox ordersCard = this.buildCard(
            "Total Orders",
            String.valueOf(this.system.getOrders().size())
        );

        VBox menuCard = this.buildCard(
            "Menu Items",
            String.valueOf(this.system.getMenuItems().size())
        );

        VBox ingredientsCard = this.buildCard(
            "Ingredients",
            String.valueOf(this.system.getIngredients().size())
        );

        // count low stock ingredients
        int lowStockCount = 0;
        for (int i = 0; i < this.system.getIngredients().size(); i++) {
            if (this.system.getIngredients().get(i).isLowStock()) {
                lowStockCount++;
            }
        }

        VBox lowStockCard = this.buildCard(
            "Low Stock Alerts",
            String.valueOf(lowStockCount)
        );

        HBox cards = new HBox(
        	    16,
        	    balanceCard,
        	    reserveCard,
        	    ordersCard,
        	    menuCard,
        	    ingredientsCard,
        	    lowStockCard
        );
        cards.setPadding(new Insets(16, 0, 0, 0));

        VBox content = new VBox(16, pageTitle, cards);
        content.setPadding(new Insets(24));

        this.contentArea.getChildren().setAll(content);
    }
    
    public void showInventory() {
    	this.contentArea.getChildren().setAll(this.inventoryView.getView());
    }
    
    public void showMenu() {
    	this.system.recomputeAllMenuItems();
    	this.menuView.refreshView();
    	this.contentArea.getChildren().setAll(this.menuView.getView());
    }
    
    public void showSales() {
    	this.salesView.refreshMenuTable();
    	this.contentArea.getChildren().setAll(this.salesView.getView());
    }
    
    public void showSalesHistory() {
    	this.salesHistoryView.refreshView();
    	this.contentArea.getChildren().setAll(this.salesHistoryView.getView());
    }
    
    public void showTransaction() {
    	this.transactionView.refreshView();
    	this.contentArea.getChildren().setAll(this.transactionView.getView());
    }

    /* buildCard method:
     * - builds a simple summary card with a title and value */
    private VBox buildCard(String title, String value) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("info-label");

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("balance-label");

        VBox card = new VBox(8, titleLabel, valueLabel);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setMinWidth(250);
        card.getStyleClass().add("summary-card");
        
        return card;
    }

    /* show method:
     * - sets the manager dashboard scene on the stage */
    public void show() {
    	this.stage.setTitle("ByteSilog - Manager Dashboard");
        this.stage.setScene(this.scene);
        this.stage.show();
        this.stage.centerOnScreen();
    }
    
    public ArrayList<Button> getNavButtons() {
    	return this.navButtons;
    }

}