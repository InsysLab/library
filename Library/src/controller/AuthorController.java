package controller;

import business.objects.Address;
import business.objects.Author;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
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
		paneAuthor.setVisible(true);
	}

	@FXML protected void handleSaveAuthor(ActionEvent event) {
		Address addr = null;
		Author author = null;
		
				
		if(!street.getText().equals("")  && !city.getText().equals("") && !state.getText().equals("") && !zip.getText().equals(""))
		{
			System.out.println(":" + street.getText() + ":");
			
			addr = new Address(street.getText(), city.getText(), state.getText(), zip.getText());
			
			if(firstName.getText() != " " && lastName.getText() != " " && phone.getText() != " " && credentials.getText() != " ")
			{
				author = new Author(firstName.getText(), lastName.getText(), phone.getText(), credentials.getText(),addr);
			}
			else 
			{
				Alert alert = new Alert(AlertType.INFORMATION, "Author Information missing !", ButtonType.OK);
				alert.setTitle(null);
				alert.show();
			}
		}
		else 
		{			
			Alert alert = new Alert(AlertType.INFORMATION, "Address Information missing !", ButtonType.OK);
			alert.setTitle(null);
			
			alert.show();
		}
		
	}
	
	@FXML protected void handleRemoveFromLeft(ActionEvent event) {
		ObservableList<Author> aa =  selectedList.getItems();
		Author author = selectedList.getSelectionModel().getSelectedItem();
		if(aa.contains(author)) aa.remove(author);
		
		selectedList.setItems(aa);
	}
	
	@FXML protected void handleMoveToRigth(ActionEvent event) {
		ObservableList<Author> aa =  selectedList.getItems();
		Author author = fullListAuthor.getSelectionModel().getSelectedItem();
		if(!aa.contains(author))aa.add(author);		
		
		selectedList.setItems(aa);
		
	}	

}
