package application;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.RetailSystem;
import view.LoginView;

// entry point for the ByteSilog Retail Management System
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /* start method:
     * - loads custom fonts
     * - initializes the retail system
     * - launches the login screen */
    public void start(Stage stage) {
    	Font.loadFont(getClass().getResourceAsStream("/application/assets/Blueberry Personal Use Only.ttf"), 24);
        Font.loadFont(getClass().getResourceAsStream("/application/assets/genty-sans-regular.ttf"), 24);
        
        System.out.println(System.getProperty("user.dir"));
        
        RetailSystem system = new RetailSystem();
        LoginView loginView = new LoginView(stage, system);
        loginView.show();
    }

}
