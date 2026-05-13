package controller;


import java.util.ArrayList;

import javafx.scene.control.Button;
import model.RetailSystem;
import model.Transaction;
import view.CashTransactionView;

public class TransactionController {
	
	private RetailSystem system;
	private CashTransactionView cashTransView;
	private ArrayList<Button> cashTransBtns;
	
	public TransactionController(RetailSystem system, CashTransactionView cashTransView) { 
		this.system = system;
		this.cashTransView = cashTransView;
		this.cashTransBtns = cashTransView.getCashTransBtns();
	}
	
	public void initializeController() {
		for(Button button:cashTransBtns) {
			String id = button.getId();
			if(id.equals("cash in")) {
				button.setOnAction(e -> handleCashTrans("CASH_IN"));
			}else if(id.equals("cash out")) {
				button.setOnAction(e -> handleCashTrans("CASH_OUT"));
			}
		}
	}
	
	private void handleCashTrans(String type) {
		try {
		String amountInput = cashTransView.getAmountInput();
		String descInput = cashTransView.getDescInput();
		
		if(checkFieldsIfEmpty(amountInput,descInput)) {
			double amount = Double.parseDouble(amountInput);
			
			if(checkAmount(amount,type)) {
				this.system.addTransaction(type, amount, descInput);
				cashTransView.refreshView();
				cashTransView.clearAmountField();
				cashTransView.clearDescField();
				cashTransView.setStatusLabel(type+ " of PHP " + String.format("%.2f", amount) + " recorded.");
				
				
			}
		}
		}catch(NumberFormatException e) {
			cashTransView.setStatusLabel("Invalid amount. Enter a number.");
		}
	}
	
	private boolean checkFieldsIfEmpty(String amountInput, String descInput) {
		if(amountInput.isEmpty()) {
			cashTransView.setStatusLabel("Enter an amount.");
			return false;
		}else if(descInput.isEmpty()) {
			cashTransView.setStatusLabel("Enter a description.");
			return false;
		}
		
		return true;
	}
	
	private boolean checkAmount(Double amount, String type) {

	    if(amount <= 0) {
	        cashTransView.setStatusLabel("Amount must be greater than zero.");
	        return false;
	    }

	    
	    else if(type.equals(Transaction.CASH_OUT)
	            && amount > this.system.getTotalBalance()) {

	        cashTransView.setStatusLabel("Insufficient balance for cash out.");
	        return false;
	    }

	   
	    else if(type.equals(Transaction.CASH_OUT)
	            && !this.system.canAffordExpense(amount)) {

	        cashTransView.setStatusLabel(
	            "Cash out denied. Required reserve balance must remain intact."
	        );

	        return false;
	    }

	    return true;
	}
}
