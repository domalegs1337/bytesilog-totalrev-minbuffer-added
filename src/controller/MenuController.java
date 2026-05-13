package controller;

import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.MenuItem;
import model.RetailSystem;
import view.MenuView;

public class MenuController {
	
	private RetailSystem system;
	private MenuView menuView;
	private ArrayList<Button> menuBtns;
	private ArrayList<TextField> inputFields;
	
	public MenuController(RetailSystem system, MenuView menuView) {
		this.system = system;
		this.menuView = menuView;
		this.menuBtns = this.menuView.getMenuButtons();
		this.inputFields = this.menuView.getInputFields();
	}
	
	public void initializeController() {
		for(Button button:this.menuBtns) {
			String id = button.getId();
			
			switch(id) {
			case "apply profit":
				button.setOnAction(e -> handleProfitEdit());
				break;
				
			case "apply labor":
				button.setOnAction(e -> handleLaborEdit());
				break;
				
			case "apply to all":
				button.setOnAction(e -> handleBulkProfitEdit());
				break;
			}
		}
		
		for(TextField textField:this.inputFields) {
			String id = textField.getId();
			
			switch(id) {
			case "profit field":
				textField.setOnKeyReleased(e -> handleProfitField());
				break;
				
			case "labor field":
				textField.setOnKeyReleased(e -> handleLaborField());
				break;	
			}
		}
	}
	
	private void handleProfitEdit() {
		try {
			MenuItem selected = menuView.getSelectedMenuItem();
			String input = menuView.getProfitInput();
			if(runCheckProfit(selected,input)) {
				double profit = Double.parseDouble(input);
				if (checkProfitValidity(profit)) {
					this.system.updateMenuList(selected,profit,"profit");
					this.menuView.refreshProfitField();
					this.menuView.refreshView();
					this.menuView.setStatusLabel("Updated " + selected.getName() + " - new price: PHP " + String.format("%.2f", selected.getSellingPrice()));
	            }
			}
		}catch(NumberFormatException e) {
			this.menuView.setStatusLabel("Invalid value. Enter a number.");
		}
	}
	
	private void handleLaborEdit() {
		try {
			MenuItem selected = menuView.getSelectedMenuItem();
			String input = menuView.getLaborInput();
			if(runCheckProfit(selected,input)) {
				double labor = Double.parseDouble(input);
				if (checkLaborValidity(labor)) {
					this.system.updateMenuList(selected,labor,"labor");
					this.menuView.refreshLaborField();
					this.menuView.refreshView();
					this.menuView.setStatusLabel("Updated " + selected.getName() + " - new price: PHP " + String.format("%.2f", selected.getSellingPrice()));
	            }
			}
		}catch(NumberFormatException e) {
			this.menuView.setStatusLabel("Invalid value. Enter a number.");
		}
	}
	
	private void handleBulkProfitEdit() {
		try {
			MenuItem selected = null;
			String input = menuView.getBulkInput();
			if(runCheckBulk(input)) {
				double profit = Double.parseDouble(input);
				if (checkProfitValidity(profit)) {
					this.system.updateMenuList(selected,profit,"bulk");
					this.menuView.refreshBulkField();
					this.menuView.refreshView();
					this.menuView.setStatusLabel("Applied " + profit + "% profit to all " + this.system.getMenuItems().size() + " menu items.");
	            }
			}
		}catch(NumberFormatException e) {
			this.menuView.setStatusLabel("Invalid value. Enter a number.");
		}
	}
	
	private void handleProfitField() {
		try {
			MenuItem selected = this.menuView.getSelectedMenuItem();
			String input = this.menuView.getProfitInput();
			if(selected == null) {
				this.menuView.setProfitPreviewLabel("Select an item first.");
				return;
			}
			double profit = Double.parseDouble(input);
			double base = selected.getTotalIngredientCost() + selected.getLaborCost();
			double raw = base * (1 + profit / 100);
			double newPrice;
			
			long lower = (long) raw - ((long) raw % 10) - 1;
	        if (lower < 0) lower = 9;
	        long upper = lower + 10;
	        
	        if (Math.abs(raw - lower) <= Math.abs(raw - upper)) {
	            newPrice = lower;
	        } else {
	            newPrice = upper;
	        }
			
//			double newPrice = base * (1 + profit / 100);
			this.menuView.setProfitPreviewLabel("New price: PHP " + String.format("%.2f", newPrice));
		}catch(NumberFormatException e) {
			this.menuView.setProfitPreviewLabel("New price: -");
		}
	}
	private void handleLaborField() {
		try {
			MenuItem selected = this.menuView.getSelectedMenuItem();
			if(selected == null) {
				this.menuView.setLaborPreviewLabel("Select an item first.");
				return;
			}
			double labor = Double.parseDouble(menuView.getLaborInput());
			double base = selected.getTotalIngredientCost() + labor;
			double newPrice = base * (1 + selected.getProfitPercent() / 100);
			this.menuView.setLaborPreviewLabel("New price: PHP " + String.format("%.2f", newPrice));
		}catch(NumberFormatException e) {
			this.menuView.setLaborPreviewLabel("New price: -");
		}
	}
	
	private boolean runCheckProfit(MenuItem selected, String input) {
		if (selected == null) {
            this.menuView.setStatusLabel("Select a menu item from the table first.");
            return false;
        }
        if (input.isEmpty()) {
        	this.menuView.setStatusLabel("Enter a profit percent value.");
            return false;
        }
        
        return true;
	}
	
	private boolean runCheckBulk(String input) {
		if (input.isEmpty()) {
        	this.menuView.setStatusLabel("Enter a profit percent value.");
            return false;
        }
        
        return true;
	}
	
	private boolean checkProfitValidity(Double profit) {
		if (profit < 0) {
			this.menuView.setStatusLabel("Profit percent cannot be negative.");
            return false;
        }
		
		return true;
	}
	
	private boolean checkLaborValidity(Double labor) {
		if (labor < 0) {
			this.menuView.setStatusLabel("Labor cost cannot be negative.");
            return false;
        }
		
		return true;
	}
}
