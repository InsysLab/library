package view;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AddPublication extends VBox {
	
	public AddPublication() {
    	try {
    		Parent root = FXMLLoader.load(getClass().getResource("AddPublication.fxml"));
    		this.getChildren().add(root);
    	} catch (IOException io) {
    		System.out.println(io.getStackTrace());
    	}
	}
	
}
