package controller;

import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.RetailSystem;
import view.LoginView;
import view.ManagerDashboardView;

public class NavigationController {
	
	private ManagerDashboardView managerDashboard;
	private RetailSystem system;
	private LoginView loginView;
	private ArrayList<Button> buttonList;
	
	public NavigationController(Stage stage,RetailSystem system,ManagerDashboardView managerDashboard) {
		this.managerDashboard = managerDashboard;
		this.system = system;
		this.loginView = new LoginView(stage, system);
		this.buttonList = managerDashboard.getNavButtons();
	}
	
	// handles switching of views
	public void initializeController() {
		try {	
			for(Button button: buttonList) {			
				String id = button.getId();
				switch(id.toLowerCase()) {
				case "overview":
					button.setOnAction(e -> managerDashboard.showOverview());
					break;
				case "inventory":
					button.setOnAction(e -> managerDashboard.showInventory());
					break;
				case "menu":
					button.setOnAction(e -> managerDashboard.showMenu());
					break;
				case "sales":
					button.setOnAction(e -> managerDashboard.showSales());
					break;
				case "sales history":
					button.setOnAction(e -> managerDashboard.showSalesHistory());
					break;
				case "cash transactions":
					button.setOnAction(e -> managerDashboard.showTransaction());
					break;
				case "logout":
					this.system.logout();
					button.setOnAction(e -> loginView.show());
					break;
				default: 
					System.out.println("Failed to set button event handler.");
					break;
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		
	}
}
