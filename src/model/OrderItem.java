package model;

// represents one line in an order: a menu item and its quantity
public class OrderItem {

    private MenuItem menuItem;
    private int quantity;
    private double subtotal; // sellingPrice * quantity

    /* OrderItem constructor:
     * - initializes item and quantity
     * - computes subtotal immediately */
    public OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.subtotal = menuItem.getSellingPrice() * quantity;
    }

    public MenuItem getMenuItem() {
        return this.menuItem;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public double getSubtotal() {
        return this.subtotal;
    }

}