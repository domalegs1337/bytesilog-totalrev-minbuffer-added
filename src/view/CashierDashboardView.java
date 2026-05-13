package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

// cashier dashboard — limited access focused on sales and cash transactions
public class CashierDashboardView {

    private Stage stage;
    private RetailSystem system;
    private Scene scene;

    private StackPane contentArea; // swappable center content

    /* CashierDashboardView constructor:
     * - stores stage and system reference
     * - builds the scene */
    public CashierDashboardView(Stage stage, RetailSystem system) {
        this.stage = stage;
        this.system = system;
        this.buildScene();
    }

    /* buildScene method:
     * - constructs the full cashier dashboard with sidebar and content area */
    private void buildScene() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("general-page");

        VBox sidebar = this.buildSidebar();
        root.setLeft(sidebar);

        this.contentArea = new StackPane();
        this.contentArea.getStyleClass().add("content-area");
        this.showOverview();
        root.setCenter(this.contentArea);

        this.scene = new Scene(root, 1024, 680);
        this.scene.getStylesheets().add(
            getClass().getResource("/application/application.css").toExternalForm()
        );
    }

    /* buildSidebar method:
     * - constructs the left navigation sidebar with cashier-allowed module buttons */
    private VBox buildSidebar() {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");
        sidebar.setSpacing(0);

        Label titleLabel = new Label("ByteSilog");
        titleLabel.getStyleClass().add("sidebar-title");
        Label roleLabel = new Label("Cashier");
        roleLabel.getStyleClass().add("sidebar-subtitle");

        VBox brand = new VBox(4, titleLabel, roleLabel);
        brand.setPadding(new Insets(24, 20, 24, 20));

        Button overviewBtn = new Button("Overview");
        overviewBtn.getStyleClass().add("nav-btn");
        overviewBtn.setMaxWidth(Double.MAX_VALUE);

        Button salesBtn = new Button("Sales");
        salesBtn.getStyleClass().add("nav-btn");
        salesBtn.setMaxWidth(Double.MAX_VALUE);

        Button inventoryBtn = new Button("Inventory");
        inventoryBtn.getStyleClass().add("nav-btn");
        inventoryBtn.setMaxWidth(Double.MAX_VALUE);

        Button cashBtn = new Button("Cash Transactions");
        cashBtn.getStyleClass().add("nav-btn");
        cashBtn.setMaxWidth(Double.MAX_VALUE);

        Button logoutBtn = new Button("Logout");
        logoutBtn.getStyleClass().add("nav-logout-btn");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);

        VBox spacer = new VBox();
        VBox.setVgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        overviewBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                showOverview();
            }
        });

        salesBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                SalesView view = new SalesView(system);
                contentArea.getChildren().setAll(view.getView());
            }
        });

        inventoryBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                InventoryView view = new InventoryView(system, true);
                contentArea.getChildren().setAll(view.getView());
            }
        });

        cashBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                CashTransactionView view = new CashTransactionView(system);
                contentArea.getChildren().setAll(view.getView());
            }
        });

        logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                system.logout();
                LoginView loginView = new LoginView(stage, system);
                loginView.show();
            }
        });

        sidebar.getChildren().addAll(
            brand,
            overviewBtn,
            salesBtn,
            inventoryBtn,
            cashBtn,
            spacer,
            logoutBtn
        );

        return sidebar;
    }

    /* showOverview method:
     * - displays summary cards for the cashier overview screen */
    private void showOverview() {
        Label pageTitle = new Label("Overview");
        pageTitle.getStyleClass().add("page-title");

        VBox ordersCard = this.buildCard(
            "Total Orders",
            String.valueOf(this.system.getOrders().size())
        );

        VBox balanceCard = this.buildCard(
            "Total Balance",
            "PHP " + String.format("%.2f", this.system.getTotalBalance())
        );

        HBox cards = new HBox(16, ordersCard, balanceCard);
        cards.setPadding(new Insets(16, 0, 0, 0));

        VBox content = new VBox(16, pageTitle, cards);
        content.setPadding(new Insets(24));

        this.contentArea.getChildren().setAll(content);
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
        card.setMinWidth(180);
        card.getStyleClass().add("summary-card");

        return card;
    }

    /* show method:
     * - sets the cashier dashboard scene on the stage */
    public void show() {
        this.stage.setTitle("ByteSilog - Cashier Dashboard");
        this.stage.setScene(this.scene);
        this.stage.show();
        this.stage.centerOnScreen();
    }

}