package controller;

import business.objects.Author;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class AuthorController {
	
@FXML private Pane paneAuthor;
@FXML private ListView<Author> fullListAuthor;
@FXML private ListView<Author> selectedList;

@FXML private TextField firstName;
@FXML private TextField lastName;
@FXML private TextField phone;
@FXML private TextField credentials;
@FXML private TextField street;
@FXML private TextField city;
@FXML private TextField state;
@FXML private TextField zip;

	@FXML protected void handleSetAuthor(ActionEvent event) {
		
	}
	
	@FXML protected void handleAddAuthor(ActionEvent event) {
		
	}

	@FXML protected void handleSaveAuthor(ActionEvent event) {
		
	}
	
	@FXML protected void handleRemoveFromLeft(ActionEvent event) {
		
	}
	
	@FXML protected void handleMoveToRigth(ActionEvent event) {
		
	}	

}
