package view;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AddLibraryMember extends VBox {
	
	public AddLibraryMember() {
    	try {
    		Parent root = FXMLLoader.load(getClass().getResource("AddLibraryMember.fxml"));
    		this.getChildren().add(root);
    	} catch (IOException io) {
    		System.out.println(io.getStackTrace());
    	}		
	}
	
}
