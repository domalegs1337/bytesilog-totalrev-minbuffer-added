package controller;

import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.RetailSystem;
import view.SalesView;

public class SalesController {
	
	private RetailSystem system;
	private SalesView salesView;
	
	private ArrayList<Button> salesBtns;
	private ArrayList<TextField> salesInputFields;
	
	public SalesController(RetailSystem system, SalesView salesView) {
		this.system = system;
		this.salesView = salesView;
		this.salesBtns = this.salesView.getSalesBtns();
		this.salesInputFields = this.salesView.getSalesInputFields();
		
	}
	
	public void initializeController() {
		for(Button button:this.salesBtns) {
			String id = button.getId();
			switch(id) {
			case "add order":
				button.setOnAction(e -> handleAddToOrder());
				break;
			case "place order":
				button.setOnAction(e -> handlePlaceOrder());
				break;
			case "clear order":
				button.setOnAction(e -> handleClearOrder());
				break;
			}
		}
		
		for(TextField textField:this.salesInputFields) {
			String id = textField.getId();
			switch(id) {
			case "quantity field":
				textField.setOnKeyReleased(e -> handleQuantityField());
				break;
			case "paid field":
				textField.setOnKeyReleased(e -> handlePaidField());
				break;
			}
		}
	}
	
	private void handleAddToOrder() {
		try {
			MenuItem selected = this.salesView.getSelectedMenuItem();
			if(selected == null) {
				this.salesView.setStatusLabel("Select a menu item first.");
				return;
			}
			
			String input = this.salesView.getQuantityInput();
			if(input.isEmpty()) {
				this.salesView.setStatusLabel("Select a quantity.");
				return;
			}
			
			int qty = Integer.parseInt(input);
			if(qty <= 0) {
				this.salesView.setStatusLabel("Quantity must be at least 1.");
				return;
			}
			
			OrderItem orderItem = new OrderItem(selected,qty);
			this.salesView.updateOrderTable(selected,orderItem,qty);
		}catch(NumberFormatException e) {
			this.salesView.setStatusLabel("Invalid quantity. Enter a whole number.");
		}
		
	}
	
	private void handlePlaceOrder() {
		try {
			Order currentOrder = this.salesView.getCurrentOrder();
			if(currentOrder.getItems().isEmpty()) {
				this.salesView.setStatusLabel("Add items to the order first.");
				return;
			}
			
			String input = this.salesView.getPaidInput();
			if (input.isEmpty()) {
	            this.salesView.setStatusLabel("Enter the amount paid.");
	            return;
	        }
			
			double paid = Double.parseDouble(input);
			if (paid < currentOrder.getTotalAmount()) {
                this.salesView.setStatusLabel("Insufficient payment.");
                return;
            }
			
			this.system.updateStock(currentOrder);
			this.salesView.setStatusLabel("Order placed. Change: PHP " + String.format("%.2f", currentOrder.getChange()));
			this.salesView.startNewOrder();
		}catch(NumberFormatException e) {
			this.salesView.setStatusLabel("Invalid amount. Enter a number.");
		}
	}

	private void handleClearOrder() {
		this.salesView.startNewOrder();
		this.salesView.setStatusLabel("Order cleared.");
	}

	private void handleQuantityField() {
		// TODO Auto-generated method stub
		
	}
	
	private void handlePaidField() {
		try {
			Order currentOrder = this.salesView.getCurrentOrder();
			String input = this.salesView.getPaidInput();
			if(input.isEmpty()) {
				this.salesView.setChangeLabel("Change: PHP 0.00");
				return;
			}
			double paid = Double.parseDouble(input);
			double change = paid - currentOrder.getTotalAmount();
			this.salesView.setChangeLabel("Change: PHP " + String.format("%.2f", change));
		}catch(NumberFormatException e) {
			this.salesView.setChangeLabel("Change: PHP 0.00");
		}
	}
	
	
}
