package controller;


import java.util.ArrayList;
import data.DataManager;
import javafx.scene.control.Button;
import model.Ingredient;
import model.RetailSystem;
import model.Transaction;
import view.InventoryView;

public class InventoryController {
	
	private InventoryView inventoryView;
	private RetailSystem system;
	private ArrayList<Button> inventoryBtns;

	public InventoryController(RetailSystem system, InventoryView inventoryView) {
		this.inventoryView = inventoryView;
		this.system = system;
		this.inventoryBtns = inventoryView.getInventoryBtns();
	}
	
	public void initializeController() {
		for(Button button:this.inventoryBtns) {
			String id = button.getId();
			switch(id) {
			case "by packs":
				button.setOnAction(e -> handleRestockByPack());
				break;
			case "apply price":
				button.setOnAction(e -> handleEditPrice());
				break;
			}
		}
	}
	
	private void handleRestockByPack() {
        try {
        	Ingredient selected = inventoryView.getSelectedIngredient();
            double marketPrice = selected.getMarketPrice();
            String packInput = this.inventoryView.getPackInput();
            int qty = Integer.parseInt(packInput);
            double totalCost = qty * marketPrice;
            
        	if(runChecks(selected,packInput,totalCost,qty)) {
        		double units = qty * selected.getUnitsPerPack();
                selected.addStock(units);
                DataManager.saveIngredients(this.system.getIngredients());
                this.inventoryView.refreshTable();
                this.inventoryView.clearPackInput();
                this.inventoryView.setStatusLabel("Restocked " + selected.getName() + " by " + qty + " pack(s) = " + units + " " + selected.getUnit() + ".");
                
                String transType = Transaction.CASH_OUT;
                String transDesc = "Restocked " + qty + " pack(s) of " + selected.getIngredientId();
                
                this.system.addTransaction(transType,totalCost, transDesc);
        	}
    		
    	}catch(NumberFormatException e) {
    		this.inventoryView.setStatusLabel("Invalid input. Enter a number.");
    	}
        
    }
	
	private void handleEditPrice() {
		try {
			Ingredient selected = this.inventoryView.getSelectedIngredient();
			String input = this.inventoryView.getPriceInput();
			Double newPrice = Double.parseDouble(input);

			if(runChecks(selected,input,newPrice)) {
				this.system.updateMenuList(selected, newPrice);
				
	            this.inventoryView.populateTable();
	            this.inventoryView.setStatusLabel("Updated " + selected.getName() + " market price to PHP " + String.format("%.2f", newPrice) + ". Menu prices recomputed.");
	            this.inventoryView.clearPriceInput();
			}
		}catch(NumberFormatException e) {
			this.inventoryView.setStatusLabel("Invalid input. Enter a number.");
		}
	}
	private boolean runChecks(Ingredient selected, String input, Double price, int qty) {
		if(selected == null) {
			this.inventoryView.setStatusLabel("Select an ingredient from the table first.");
			return false;
		}else if(input.isEmpty()) {
			this.inventoryView.setStatusLabel("Enter number of packs.");
			return false;
		}else if(price > this.system.getTotalBalance()) {
			this.inventoryView.setStatusLabel("Insufficient balance.");
			return false;
		}else if(qty <= 0) {
			this.inventoryView.setStatusLabel("Packs must be greater than zero.");
			return false;
		}
		return true;
	}
	
	private boolean runChecks(Ingredient selected, String input, Double price) {
		if(selected == null) {
			this.inventoryView.setStatusLabel("Select an ingredient from the table first.");
			return false;
		}else if(input.isEmpty()) {
			this.inventoryView.setStatusLabel("Enter a new market price.");
			return false;
		}else if(price <= 0) {
			this.inventoryView.setStatusLabel("Price must be greater than zero.");
			return false;
		}
		return true;
	}
}
