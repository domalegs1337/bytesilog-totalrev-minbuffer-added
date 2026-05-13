package data;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import model.Cashier;
import model.Ingredient;
import model.Manager;
import model.MenuItem;
import model.Order;
import model.Recipe;
import model.Transaction;
import model.User;

// handles all file reading and writing for the system
// uses txt files with | as the field delimiter
//
// file formats:
//   users.txt        -> role|username|password
//   ingredients.txt  -> id|name|unit|marketPrice|unitsPerPack|stockQty
//   menu_items.txt   -> id|name|description|laborCost|profitPercent
//   recipes.txt      -> itemId|ingredientId|qtyPerServing
//   orders.txt       -> orderId|timestamp|totalAmount|amountPaid|change
//   transactions.txt -> id|type|amount|description|timestamp
public class DataManager {

    private static final String USERS_FILE = "users.txt";
    private static final String INGREDIENTS_FILE = "ingredients.txt";
    private static final String MENU_ITEMS_FILE = "menu_items.txt";
    private static final String RECIPES_FILE = "recipes.txt";
    private static final String ORDERS_FILE = "orders.txt";
    private static final String TRANSACTIONS_FILE = "transactions.txt";

    /* loadUsers method:
     * - reads users.txt and populates the given list
     * - creates the file with default accounts if it does not exist */
    public static void loadUsers(ArrayList<User> users) {
        try {
            if (!Files.exists(Paths.get(USERS_FILE))) {
                DataManager.createDefaultUsers();
            }
            BufferedReader reader = Files.newBufferedReader(Paths.get(USERS_FILE));
            String line = reader.readLine();
            while (line != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    String role = parts[0];
                    String username = parts[1];
                    String password = parts[2];
                    if (role.equals(Manager.ROLE)) {
                        users.add(new Manager(username, password));
                    } else {
                        users.add(new Cashier(username, password));
                    }
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    /* createDefaultUsers method:
     * - writes a default manager and cashier account to users.txt */
    private static void createDefaultUsers() {
        try {
            PrintWriter writer = new PrintWriter(Paths.get(USERS_FILE).toFile());
            writer.println("MANAGER|admin|admin123");
            writer.println("CASHIER|cashier|cashier123");
            writer.close();
        } catch (Exception e) {
            System.out.println("Error creating default users: " + e.getMessage());
        }
    }

    /* loadIngredients method:
     * - reads ingredients.txt and populates the given list
     * - unitCost is computed by the Ingredient constructor */
    public static void loadIngredients(ArrayList<Ingredient> ingredients) {
        try {
            if (!Files.exists(Paths.get(INGREDIENTS_FILE))) {
                return;
            }
            BufferedReader reader = Files.newBufferedReader(Paths.get(INGREDIENTS_FILE));
            String line = reader.readLine();
            while (line != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 6) {
                    ingredients.add(new Ingredient(
                        parts[0],
                        parts[1],
                        parts[2],
                        Double.parseDouble(parts[3]),
                        Double.parseDouble(parts[4]),
                        Double.parseDouble(parts[5])
                    ));
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error loading ingredients: " + e.getMessage());
        }
    }

    /* saveIngredients method:
     * - writes all ingredients to ingredients.txt
     * - does not save unitCost as it is computed on load */
    public static void saveIngredients(ArrayList<Ingredient> ingredients) {
        try {
            PrintWriter writer = new PrintWriter(Paths.get(INGREDIENTS_FILE).toFile());
            for (int i = 0; i < ingredients.size(); i++) {
                Ingredient ing = ingredients.get(i);
                writer.println(
                    ing.getIngredientId() + "|" +
                    ing.getName() + "|" +
                    ing.getUnit() + "|" +
                    ing.getMarketPrice() + "|" +
                    ing.getUnitsPerPack() + "|" +
                    ing.getStockQty()
                );
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error saving ingredients: " + e.getMessage());
        }
    }

    /* loadMenuItems method:
     * - reads menu_items.txt and populates the given list
     * - totalIngredientCost and sellingPrice are computed after loading recipes */
    public static void loadMenuItems(ArrayList<MenuItem> menuItems) {
        try {
            if (!Files.exists(Paths.get(MENU_ITEMS_FILE))) {
                return;
            }
            BufferedReader reader = Files.newBufferedReader(Paths.get(MENU_ITEMS_FILE));
            String line = reader.readLine();
            while (line != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    menuItems.add(new MenuItem(
                        parts[0],
                        parts[1],
                        parts[2],
                        Double.parseDouble(parts[3]),
                        Double.parseDouble(parts[4])
                    ));
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error loading menu items: " + e.getMessage());
        }
    }

    /* saveMenuItems method:
     * - writes raw inputs only to menu_items.txt
     * - does not save totalIngredientCost or sellingPrice as they are computed */
    public static void saveMenuItems(ArrayList<MenuItem> menuItems) {
        try {
            PrintWriter writer = new PrintWriter(Paths.get(MENU_ITEMS_FILE).toFile());
            for (int i = 0; i < menuItems.size(); i++) {
                MenuItem item = menuItems.get(i);
                writer.println(
                    item.getItemId() + "|" +
                    item.getName() + "|" +
                    item.getDescription() + "|" +
                    item.getLaborCost() + "|" +
                    item.getProfitPercent()
                );
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error saving menu items: " + e.getMessage());
        }
    }

    /* loadRecipes method:
     * - reads recipes.txt and populates the given list */
    public static void loadRecipes(ArrayList<Recipe> recipes) {
        try {
            if (!Files.exists(Paths.get(RECIPES_FILE))) {
                return;
            }
            BufferedReader reader = Files.newBufferedReader(Paths.get(RECIPES_FILE));
            String line = reader.readLine();
            while (line != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    recipes.add(new Recipe(
                        parts[0],
                        parts[1],
                        Double.parseDouble(parts[2])
                    ));
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error loading recipes: " + e.getMessage());
        }
    }

    /* loadOrders method:
     * - reads orders.txt and populates the given list */
    public static void loadOrders(ArrayList<Order> orders) {
        try {
            if (!Files.exists(Paths.get(ORDERS_FILE))) {
                return;
            }
            BufferedReader reader = Files.newBufferedReader(Paths.get(ORDERS_FILE));
            String line = reader.readLine();
            while (line != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    Order order = new Order(parts[0], parts[1]);
                    order.setTotalAmount(Double.parseDouble(parts[2]));
                    order.setAmountPaid(Double.parseDouble(parts[3]));
                    order.setChange(Double.parseDouble(parts[4]));
                    orders.add(order);
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error loading orders: " + e.getMessage());
        }
    }

    /* saveOrders method:
     * - writes all orders to orders.txt */
    public static void saveOrders(ArrayList<Order> orders) {
        try {
            PrintWriter writer = new PrintWriter(Paths.get(ORDERS_FILE).toFile());
            for (int i = 0; i < orders.size(); i++) {
                Order order = orders.get(i);
                writer.println(
                    order.getOrderId() + "|" +
                    order.getTimestamp() + "|" +
                    order.getTotalAmount() + "|" +
                    order.getAmountPaid() + "|" +
                    order.getChange()
                );
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error saving orders: " + e.getMessage());
        }
    }

    /* loadTransactions method:
     * - reads transactions.txt and populates the given list */
    public static void loadTransactions(ArrayList<Transaction> transactions) {
        try {
            if (!Files.exists(Paths.get(TRANSACTIONS_FILE))) {
                return;
            }
            BufferedReader reader = Files.newBufferedReader(Paths.get(TRANSACTIONS_FILE));
            String line = reader.readLine();
            while (line != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    transactions.add(new Transaction(
                        parts[0],
                        parts[1],
                        Double.parseDouble(parts[2]),
                        parts[3],
                        parts[4]
                    ));
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }
    }

    /* saveTransactions method:
     * - writes all transactions to transactions.txt */
    public static void saveTransactions(ArrayList<Transaction> transactions) {
        try {
            PrintWriter writer = new PrintWriter(Paths.get(TRANSACTIONS_FILE).toFile());
            for (int i = 0; i < transactions.size(); i++) {
                Transaction t = transactions.get(i);
                writer.println(
                    t.getTransactionId() + "|" +
                    t.getType() + "|" +
                    t.getAmount() + "|" +
                    t.getDescription() + "|" +
                    t.getTimestamp()
                );
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }

}