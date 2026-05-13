package model;

// links a menu item to one of its ingredients with a quantity per serving
// used to deduct stock when an order is placed
public class Recipe {

    private String itemId;        // matches MenuItem itemId
    private String ingredientId;  // matches Ingredient ingredientId
    private double qtyPerServing; // how much of the ingredient is used per serving

    /* Recipe constructor:
     * - initializes the link between a menu item and an ingredient */
    public Recipe(String itemId, String ingredientId, double qtyPerServing) {
        this.itemId = itemId;
        this.ingredientId = ingredientId;
        this.qtyPerServing = qtyPerServing;
    }

    public String getItemId() {
        return this.itemId;
    }

    public String getIngredientId() {
        return this.ingredientId;
    }

    public double getQtyPerServing() {
        return this.qtyPerServing;
    }

}