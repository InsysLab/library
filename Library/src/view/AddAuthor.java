package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import controller.AddPublicationController;
import business.objects.Author;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;

public class AddAuthor extends PopupWindow {
	public List<Author> authors = new ArrayList<>();
	
	public AddAuthor(AddPublicationController cont) {
		// TODO Auto-generated constructor stub
			
    	try {
    		Parent root = FXMLLoader.load(getClass().getResource("Author.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Add Edit Author");
            stage.setScene(new Scene(root));
           // stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.setAlwaysOnTop(true);
            
         	stage.show();
         	

    	} catch (IOException io) {
    		System.out.println(io.getStackTrace());
    	}
	    
	}
	
}
