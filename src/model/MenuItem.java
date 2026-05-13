package model;

import java.util.ArrayList;

// represents a single item on the ByteSilog menu
public class MenuItem {

    private String itemId;              // e.g. ITEM001
    private String name;
    private String description;
    private double totalIngredientCost; // computed from recipes
    private double laborCost;
    private double profitPercent;       // e.g. 20.0 means 20%
    private double sellingPrice;        // computed: (ingredientCost + laborCost) * (1 + profitPercent/100)

    /* MenuItem constructor:
     * - initializes raw inputs only
     * - totalIngredientCost and sellingPrice start at 0 until computed */
    public MenuItem(String itemId, String name, String description,
                    double laborCost, double profitPercent) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.laborCost = laborCost;
        this.profitPercent = profitPercent;
        this.totalIngredientCost = 0;
        this.sellingPrice = 0;
    }

    /* computeTotalIngredientCost method:
     * - calculates total ingredient cost from recipes and ingredient unit costs
     * - called by RetailSystem after loading all data */
    public void computeTotalIngredientCost(ArrayList<Recipe> recipes,
                                    ArrayList<Ingredient> ingredients) {
        this.totalIngredientCost = 0;
        for (int i = 0; i < recipes.size(); i++) {
            Recipe recipe = recipes.get(i);
            if (recipe.getItemId().equals(this.itemId)) {
                for (int j = 0; j < ingredients.size(); j++) {
                    Ingredient ing = ingredients.get(j);
                    if (ing.getIngredientId().equals(recipe.getIngredientId())) {
                        this.totalIngredientCost += ing.getUnitCost() * recipe.getQtyPerServing();
                        break;
                    }
                }
            }
        }
    }

    /* computeSellingPrice method:
     * - recalculates selling price from ingredient cost, labor, and profit percent
     * - formula: (ingredientCost + laborCost) * (1 + profitPercent / 100) */
    public void computeSellingPrice() {
        double base = this.totalIngredientCost + this.laborCost;
        double raw = base * (1 + this.profitPercent / 100);
        
        // round to nearest number ending in 9
        long lower = (long) raw - ((long) raw % 10) - 1;
        if (lower < 0) lower = 9;
        long upper = lower + 10;
        
        if (Math.abs(raw - lower) <= Math.abs(raw - upper)) {
            this.sellingPrice = lower;
        } else {
            this.sellingPrice = upper;
        }
    }
    
    public String getItemId() {
        return this.itemId;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTotalIngredientCost() {
        return this.totalIngredientCost;
    }
    
    public double getRoundedCost() {
    	double raw = this.totalIngredientCost;
    	double rounded = Math.round(raw*100) / 100.0;
    	return rounded;
    }

    public double getLaborCost() {
        return this.laborCost;
    }

    public void setLaborCost(double laborCost) {
        this.laborCost = laborCost;
    }

    public double getProfitPercent() {
        return this.profitPercent;
    }

    public void setProfitPercent(double profitPercent) {
        this.profitPercent = profitPercent;
    }

    public double getSellingPrice() {
        return this.sellingPrice;
    }

}