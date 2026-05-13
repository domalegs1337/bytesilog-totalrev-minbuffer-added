package model;

import java.util.ArrayList;

// represents a single customer order with one or more items
public class Order {

    private String orderId;
    private ArrayList<OrderItem> items;
    private double totalAmount;
    private double amountPaid;
    private double change;
    private String timestamp; // plain string e.g. "2026-04-24 14:30"

    /* Order constructor:
     * - initializes a new empty order with no items */
    public Order(String orderId, String timestamp) {
        this.orderId = orderId;
        this.timestamp = timestamp;
        this.items = new ArrayList<OrderItem>();
        this.totalAmount = 0;
        this.amountPaid = 0;
        this.change = 0;
    }

    /* addItem method:
     * - adds an OrderItem to this order */
    public void addItem(OrderItem item) {
        this.items.add(item);
    }

    /* computeTotal method:
     * - sums all item subtotals to get the total amount */
    public void computeTotal() {
        this.totalAmount = 0;
        for (int i = 0; i < this.items.size(); i++) {
            this.totalAmount += this.items.get(i).getSubtotal();
        }
    }

    /* computeChange method:
     * - computes change from amount paid minus total */
    public void computeChange() {
        this.change = this.amountPaid - this.totalAmount;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public ArrayList<OrderItem> getItems() {
        return this.items;
    }

    public double getTotalAmount() {
        return this.totalAmount;
    }

    public double getAmountPaid() {
        return this.amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }
    
    public void setTotalAmount(double totalAmount) {
    	this.totalAmount = totalAmount;
    }
    
    public void setChange(double change) {
    	this.change = change;
    }

    public double getChange() {
        return this.change;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

}