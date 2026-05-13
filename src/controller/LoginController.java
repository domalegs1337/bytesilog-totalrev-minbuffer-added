package controller;

import java.util.ArrayList;

import javafx.scene.control.Button;
import model.RetailSystem;
import view.LoginView;

public class LoginController {

	private RetailSystem system;
	private LoginView loginView;
	
	private ArrayList<Button> loginBtns;

	public LoginController(RetailSystem system, LoginView view) {
		this.system = system;
		this.loginView = view;
		this.loginBtns = this.loginView.getLoginButton();
		
	}
	
	public void initializeController() {
		for(Button button : this.loginBtns) {
			String id = button.getId();
			if(id.equals("login")) {
				button.setOnAction(e -> this.handleLogin());
			}
		}
	}

	private void handleLogin() {
		String username = this.loginView.getUsernameInput();
		String password = this.loginView.getPasswordInput();
		try {
			if(this.checkInputIfNull(username,password)) {
				if(validateInput(username,password)) {
					String role = this.system.getCurrentUser().getRole();
					this.loginView.successfulLogin(role);
				}
			}
		}catch(Exception e) {
			
		}
	}
	
	private boolean checkInputIfNull(String usernameInput, String passwordInput) {
		if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
            this.loginView.setErrorLabel("Please enter your username and password.");
            return false;
        }
		return true;
	}
	
	private boolean validateInput(String username, String password) {
		boolean success = this.system.login(username, password);
		if (!success) {
			this.loginView.setErrorLabel("Invalid username or password.");
			this.loginView.clearPasswordField();
			return success;
        }
		return success;
	}
}
