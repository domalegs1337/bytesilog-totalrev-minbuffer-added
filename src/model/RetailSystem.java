package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import data.DataManager;
import model.Transaction;

// central state holder for the ByteSilog system
// holds all data lists and the currently logged-in user
// passed by reference to all views
public class RetailSystem {

    private ArrayList<User> users;               // all registered users
    private ArrayList<Ingredient> ingredients;   // all ingredients in inventory
    private ArrayList<MenuItem> menuItems;       // all menu items
    private ArrayList<Recipe> recipes;           // ingredient links per menu item
    private ArrayList<Order> orders;             // all placed orders
    private ArrayList<Transaction> transactions; // all cash transaction records
    private User currentUser;                    // currently logged-in user
    private double monthlyRent = 1500;
    private double payroll = 2500;
    private double emergencyBuffer = 1000;
    /* RetailSystem constructor:
     * - initializes all data lists
     * - loads data from files via DataManager
     * - computes ingredient costs and selling prices for all menu items */
    public RetailSystem() {
        this.users = new ArrayList<User>();
        this.ingredients = new ArrayList<Ingredient>();
        this.menuItems = new ArrayList<MenuItem>();
        this.recipes = new ArrayList<Recipe>();
        this.orders = new ArrayList<Order>();
        this.transactions = new ArrayList<Transaction>();
        this.currentUser = null;

        DataManager.loadUsers(this.users);
        DataManager.loadIngredients(this.ingredients);
        DataManager.loadMenuItems(this.menuItems);
        DataManager.loadRecipes(this.recipes);
        DataManager.loadOrders(this.orders);
        DataManager.loadTransactions(this.transactions);

        this.recomputeAllMenuItems();
    }

    /* recomputeAllMenuItems method:
     * - recomputes totalIngredientCost and sellingPrice for every menu item
     * - called on startup and whenever ingredient market prices change */
    public void recomputeAllMenuItems() {
        for (int i = 0; i < this.menuItems.size(); i++) {
            MenuItem item = this.menuItems.get(i);
            item.computeTotalIngredientCost(this.recipes, this.ingredients);
            item.computeSellingPrice();
        }
    }

    /* login method:
     * - checks credentials against the user list
     * - sets currentUser if a match is found
     * - returns true if login succeeded */
    public boolean login(String username, String password) {
        for (int i = 0; i < this.users.size(); i++) {
            User u = this.users.get(i);
            if (u.getUsername().equals(username) && u.checkPassword(password)) {
                this.currentUser = u;
                return true;
            }
        }
        return false;
    }

    /* logout method:
     * - clears the currently logged-in user */
    public void logout() {
        this.currentUser = null;
    }
    
    public void addTransaction(String type, Double amount, String desc) {
    	String transId = this.generateTransactionId();
    	String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
    	Transaction t = new Transaction(transId,type,amount,desc,timestamp);
    	this.transactions.add(t);
    	DataManager.saveTransactions(transactions);
    }
    
    public void addTransaction(Transaction t) {
    	this.transactions.add(t);
    	DataManager.saveTransactions(transactions);
    }

    /* getTotalBalance method:
     * - computes net cash balance from all transactions
     * - CASH_IN and SALE add to balance, CASH_OUT subtracts */
    public double getTotalBalance() {
        double balance = 0;
        for (int i = 0; i < this.transactions.size(); i++) {
            Transaction t = this.transactions.get(i);
            if (t.getType().equals(Transaction.CASH_OUT)) {
                balance -= t.getAmount();
            } else {
                balance += t.getAmount();
            }
        }
        return balance;
    }
    public double getMinimumBalance() {
        return this.monthlyRent + this.payroll + this.emergencyBuffer;
    }

    // checks if expense can be afforded safely
    public boolean canAffordExpense(double amount) {
        return (this.getTotalBalance() - amount) >= this.getMinimumBalance();
    }

    public double getMonthlyRent() {
        return this.monthlyRent;
    }

    public double getPayroll() {
        return this.payroll;
    }

    public double getEmergencyBuffer() {
        return this.emergencyBuffer;
    }

    /* generateOrderId method:
     * - generates the next order ID based on current order count */
    public String generateOrderId() {
        int next = this.orders.size() + 1;
        if (next < 10) return "ORD00" + next;
        if (next < 100) return "ORD0" + next;
        return "ORD" + next;
    }

    /* generateTransactionId method:
     * - generates the next transaction ID based on current transaction count */
    public String generateTransactionId() {
        int next = this.transactions.size() + 1;
        if (next < 10) return "TXN00" + next;
        if (next < 100) return "TXN0" + next;
        return "TXN" + next;
    }
    
    public void updateMenuList(Ingredient selected, Double newPrice) {
    	selected.setMarketPrice(newPrice);
    	DataManager.saveIngredients(this.ingredients);
    	
    	this.recomputeAllMenuItems();
    	DataManager.saveMenuItems(this.menuItems);
    }
    
    public void updateMenuList(MenuItem selected, Double newAmount, String editType) {
    	switch(editType) {
    	case "profit":
    		selected.setProfitPercent(newAmount);
            selected.computeSellingPrice();
    		break;
    		
    	case "labor":
    		selected.setLaborCost(newAmount);
    		selected.computeSellingPrice();
    		break;
    		
    	case "bulk":
    		for(int i = 0; i < this.menuItems.size(); i++) {
    			MenuItem item = this.menuItems.get(i);
    			item.setProfitPercent(newAmount);
    			item.computeSellingPrice();
    		}
    		break;
    	}
    	
        DataManager.saveMenuItems(this.menuItems);
    	
    	this.recomputeAllMenuItems();
    	DataManager.saveMenuItems(this.menuItems);
    }
    
    public void updateStock(Order currentOrder) {
    	// deduct stock for each order item based on recipes
        ArrayList<OrderItem> items = currentOrder.getItems();
        ArrayList<Recipe> recipes = this.recipes;
        ArrayList<Ingredient> ingredients = this.ingredients;

        for (int i = 0; i < items.size(); i++) {
            OrderItem orderItem = items.get(i);
            String itemId = orderItem.getMenuItem().getItemId();
            int qty = orderItem.getQuantity();

            for (int j = 0; j < recipes.size(); j++) {
                Recipe recipe = recipes.get(j);
                if (recipe.getItemId().equals(itemId)) {
                    for (int k = 0; k < ingredients.size(); k++) {
                        Ingredient ing = ingredients.get(k);
                        if (ing.getIngredientId().equals(recipe.getIngredientId())) {
                            ing.deductStock(recipe.getQtyPerServing() * qty);
                            break;
                        }
                    }
                }
            }
        }

        // save updated stock
        DataManager.saveIngredients(this.ingredients);
    	
        // record sale transaction
        String txnId = this.generateTransactionId();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        Transaction saleTxn = new Transaction(
            txnId,
            Transaction.SALE,
            currentOrder.getTotalAmount(),
            "Order " + currentOrder.getOrderId(),
            timestamp
        );
        this.transactions.add(saleTxn);
        DataManager.saveTransactions(this.transactions);

        // save order
        this.orders.add(currentOrder);
        DataManager.saveOrders(this.orders);

        // start a new order
        currentOrder = new Order(
            this.generateOrderId(),
            new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())
        );
    }

    public ArrayList<User> getUsers() {
        return this.users;
    }

    public ArrayList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public ArrayList<MenuItem> getMenuItems() {
        return this.menuItems;
    }

    public ArrayList<Recipe> getRecipes() {
        return this.recipes;
    }

    public ArrayList<Order> getOrders() {
        return this.orders;
    }

    public ArrayList<Transaction> getTransactions() {
        return this.transactions;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

}