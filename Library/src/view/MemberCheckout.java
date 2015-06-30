package view;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class MemberCheckout extends VBox {
	
	public MemberCheckout() {
    	try {
    		Parent root = FXMLLoader.load(getClass().getResource("MemberCheckout.fxml"));
    		this.getChildren().add(root);
    	} catch (IOException io) {
    		System.out.println(io.getStackTrace());
    	}		
	}
	
}
