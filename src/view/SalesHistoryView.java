package view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import model.Order;
import model.RetailSystem;

// sales history view - manager only
// displays all past orders with totals and timestamps
public class SalesHistoryView extends View{

    private RetailSystem system;
    private VBox view;
    
    private TableView<Order> table;
    private Label totalOrdersLabel;
    private Label totalRevenueLabel;
    
    /* SalesHistoryView constructor:
     * - stores references and builds the view */
    public SalesHistoryView(RetailSystem system) {
        this.system = system;
        this.buildView();
    }

    /* buildView method:
     * - constructs the sales history layout with order table */
    private void buildView() {
        Label pageTitle = new Label("Sales History");
        pageTitle.getStyleClass().add("page-title");

        // summary
        this.totalOrdersLabel = new Label();
        this.totalOrdersLabel.getStyleClass().add("info-label");

        this.totalRevenueLabel = new Label();
        this.totalRevenueLabel.getStyleClass().add("info-label");

        this.refreshSummaryLabels();
        VBox summaryBox = new VBox(4, totalOrdersLabel, totalRevenueLabel);
        summaryBox.setPadding(new Insets(16));
        summaryBox.getStyleClass().add("summary-card");

        this.table = this.buildTable();

        this.view = new VBox(16, pageTitle, summaryBox, this.table);
        this.view.setPadding(new Insets(24));
    }

    /* buildTable method:
     * - constructs the order history TableView */
    private TableView<Order> buildTable() {
        TableView<Order> tableView = new TableView<Order>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getStyleClass().add("table-view");

        TableColumn<Order, String> idCol = new TableColumn<Order, String>("Order ID");
        idCol.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Order, String>, javafx.beans.value.ObservableValue<String>>() {
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> data) {
                return new SimpleStringProperty(data.getValue().getOrderId());
            }
        });

        TableColumn<Order, String> timestampCol = new TableColumn<Order, String>("Timestamp");
        timestampCol.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Order, String>, javafx.beans.value.ObservableValue<String>>() {
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> data) {
                return new SimpleStringProperty(data.getValue().getTimestamp());
            }
        });

        TableColumn<Order, Number> totalCol = new TableColumn<Order, Number>("Total (PHP)");
        totalCol.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Order, Number>, javafx.beans.value.ObservableValue<Number>>() {
            public javafx.beans.value.ObservableValue<Number> call(TableColumn.CellDataFeatures<Order, Number> data) {
                return new SimpleDoubleProperty(data.getValue().getTotalAmount());
            }
        });

        TableColumn<Order, Number> paidCol = new TableColumn<Order, Number>("Amount Paid (PHP)");
        paidCol.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Order, Number>, javafx.beans.value.ObservableValue<Number>>() {
            public javafx.beans.value.ObservableValue<Number> call(TableColumn.CellDataFeatures<Order, Number> data) {
                return new SimpleDoubleProperty(data.getValue().getAmountPaid());
            }
        });

        TableColumn<Order, Number> changeCol = new TableColumn<Order, Number>("Change (PHP)");
        changeCol.setCellValueFactory(new javafx.util.Callback<TableColumn.CellDataFeatures<Order, Number>, javafx.beans.value.ObservableValue<Number>>() {
            public javafx.beans.value.ObservableValue<Number> call(TableColumn.CellDataFeatures<Order, Number> data) {
                return new SimpleDoubleProperty(data.getValue().getChange());
            }
        });

        tableView.getColumns().add(idCol);
        tableView.getColumns().add(timestampCol);
        tableView.getColumns().add(totalCol);
        tableView.getColumns().add(paidCol);
        tableView.getColumns().add(changeCol);

        for (int i = 0; i < this.system.getOrders().size(); i++) {
            tableView.getItems().add(this.system.getOrders().get(i));
        }

        return tableView;
    }
    private void refreshSummaryLabels() {

        this.totalOrdersLabel.setText(
            "Total Orders: " + this.system.getOrders().size()
        );

        double totalRevenue = 0;

        for (int i = 0; i < this.system.getOrders().size(); i++) {
            totalRevenue += this.system.getOrders().get(i).getTotalAmount();
        }

        this.totalRevenueLabel.setText(
            "Total Revenue: PHP " + String.format("%.2f", totalRevenue)
        );
    }
    
    public void refreshView() {

        this.table.getItems().clear();

        for (int i = 0; i < this.system.getOrders().size(); i++) {
            this.table.getItems().add(this.system.getOrders().get(i));
        }

        this.refreshSummaryLabels();
    }

    /* getView method:
     * - returns the built view node to be placed in the content area */
    public VBox getView() {
        return this.view;
    }

}