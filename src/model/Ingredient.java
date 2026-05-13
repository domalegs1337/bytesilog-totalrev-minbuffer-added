package model;

// represents a single ingredient tracked in the inventory
public class Ingredient {

    private String ingredientId; // e.g. ING001
    private String name;
    private String unit;         // grams, pcs, mL, oz
    private double marketPrice;  // price per pack
    private double unitsPerPack;
    private double unitCost;     // computed: marketPrice / unitsPerPack
    private double stockQty;     // current stock in inventory

    /* Ingredient constructor:
     * - initializes all fields
     * - computes unitCost from marketPrice and unitsPerPack */
    public Ingredient(String ingredientId, String name, String unit,
                      double marketPrice, double unitsPerPack, double stockQty) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.unit = unit;
        this.marketPrice = marketPrice;
        this.unitsPerPack = unitsPerPack;
        this.unitCost = marketPrice / unitsPerPack;
        this.stockQty = stockQty;
    }

    /* deductStock method:
     * - subtracts the given quantity from current stock
     * - does not go below zero */
    public void deductStock(double qty) {
        this.stockQty -= qty;
        if (this.stockQty < 0) {
            this.stockQty = 0;
        }
    }

    /* addStock method:
     * - adds the given quantity to current stock */
    public void addStock(double qty) {
        this.stockQty += qty;
    }

    /* isLowStock method:
     * - returns true if stock is at or below one pack's worth */
    public boolean isLowStock() {
        return this.stockQty <= this.unitsPerPack;
    }

    public String getIngredientId() {
        return this.ingredientId;
    }

    public String getName() {
        return this.name;
    }

    public String getUnit() {
        return this.unit;
    }

    public double getMarketPrice() {
        return this.marketPrice;
    }

    /* setMarketPrice method:
     * - updates market price and recomputes unit cost */
    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
        this.unitCost = this.marketPrice / this.unitsPerPack;
    }

    public double getUnitsPerPack() {
        return this.unitsPerPack;
    }

    public double getUnitCost() {
        return this.unitCost;
    }

    public double getStockQty() {
        return this.stockQty;
    }

    public void setStockQty(double stockQty) {
        this.stockQty = stockQty;
    }

}