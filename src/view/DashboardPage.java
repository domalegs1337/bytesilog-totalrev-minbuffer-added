package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class DashboardPage extends BorderPane {

	public DashboardPage() {
//		this.setTop(new HeaderBar());
		this.getStyleClass().add("general-page");

		Label label = new Label("This is a dashboard page");

		VBox center = new VBox(15);
		center.setAlignment(Pos.CENTER);
//		center.getChildren().addAll(logoView, label);
		center.getChildren().add(label);
		

		this.setCenter(center);
//		this.setBottom(new FooterBar());
	}
}