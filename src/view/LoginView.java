package view;

import java.util.ArrayList;

import controller.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Manager;
import model.RetailSystem;

// login screen — first screen shown on app launch
public class LoginView {

    private Stage stage;
    private RetailSystem system;
    private Scene scene;

    private TextField usernameField;   // username input
    private PasswordField passwordField; // password input (masked)
    private Label errorLabel;          // shown on invalid login
    
    private ArrayList<Button> loginButtonList;
    
    private LoginController controller;

    /* LoginView constructor:
     * - stores stage and system reference
     * - builds the scene */
    public LoginView(Stage stage, RetailSystem system) {
        this.stage = stage;
        this.system = system;
        this.buildScene();
    }

    /* buildScene method:
     * - constructs all UI components and layout for the login screen */
    private void buildScene() {
        // logo
        Image logo = new Image(getClass().getResourceAsStream("/application/assets/Circle_Logo.png"));
        ImageView logoView = new ImageView(logo);
        logoView.setFitWidth(340);
        logoView.setPreserveRatio(true);

        // title
        Label titleLabel = new Label("Welcome");
        titleLabel.getStyleClass().add("login-title");

        // fields
        this.usernameField = new TextField();
        this.usernameField.setPromptText("Username");
        this.usernameField.getStyleClass().add("login-input");

        this.passwordField = new PasswordField();
        this.passwordField.setPromptText("Password");
        this.passwordField.getStyleClass().add("login-input");

        this.errorLabel = new Label("");
        this.errorLabel.getStyleClass().add("error-label");

        // login button
        Button loginBtn = new Button("Login");
        loginBtn.setId("login");
        loginBtn.getStyleClass().add("login-button");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        
        this.loginButtonList = new ArrayList<>();
        this.loginButtonList.add(loginBtn);

        // form container
        VBox form = new VBox(15,
            titleLabel,
            usernameField,
            passwordField,
            errorLabel,
            loginBtn
        );
        form.getStyleClass().add("login-container");
        form.setAlignment(Pos.CENTER);
        form.setPrefWidth(360);

        // center layout — logo left, form right
        HBox center = new HBox(50, logoView, form);
        center.setAlignment(Pos.CENTER);
        center.setPadding(new Insets(40));

        BorderPane root = new BorderPane();
        root.setCenter(center);
        root.getStyleClass().add("general-page");

        this.scene = new Scene(root, 1024, 680);
        this.scene.getStylesheets().add(
            getClass().getResource("/application/application.css").toExternalForm()
        );
        
        this.controller = new LoginController(this.system,this);
        this.controller.initializeController();
    }


    /* show method:
     * - sets the login scene on the stage and displays it */
    public void show() {
        this.stage.setTitle("ByteSilog - Login");
        this.stage.setScene(this.scene);
        this.stage.show();
        this.stage.centerOnScreen();
    }
    
    public void successfulLogin(String role) {
    	 if (role.equals(Manager.ROLE)) {
             ManagerDashboardView managerView = new ManagerDashboardView(this.stage, this.system);
             managerView.show();
         } else {
             CashierDashboardView cashierView = new CashierDashboardView(this.stage, this.system);
             cashierView.show();
         }
    }

	public ArrayList<Button> getLoginButton() {
		return this.loginButtonList;
	}

	public String getUsernameInput() {
		return this.usernameField.getText().trim();
	}

	public String getPasswordInput() {
		return this.passwordField.getText().trim();
	}

	public void setErrorLabel(String newErrorLabel) {
		this.errorLabel.setText(newErrorLabel);
	}

	public void clearPasswordField() {
		this.passwordField.clear();
	}

}