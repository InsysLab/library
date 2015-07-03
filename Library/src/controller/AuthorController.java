package controller;

import java.net.URL;
import java.util.ResourceBundle;

import business.dataaccess.DataAccess;
import business.dataaccess.DataAccessFacade;
import business.objects.Address;
import business.objects.Author;
import business.objects.AuthorList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AuthorController implements Initializable{
	
	private final DataAccess dao = new DataAccessFacade();
	
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
@FXML private Button SetAuthor;

	@FXML protected void handleSetAuthor(ActionEvent event) {
		dialogStage.close();
	}
	
	@FXML protected void handleKeyReleasedCredentials(ActionEvent event) {
		
		if(!checkExist(credentials.getText()))
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setTitle("Duplicated Author Information");
			alert.setContentText("Author has credentials: " + credentials.getText() + " already exist !");
			alert.show();
		}
	}
	
	@FXML protected void handleAddAuthor(ActionEvent event) {
		paneAuthor.setVisible(true);
		clearFields();
	}

	@FXML protected void handleSaveAuthor(ActionEvent event) {
		Address addr = null;
		Author author = null;
		
				
		if(!street.getText().equals("")  && !city.getText().equals("") && !state.getText().equals("") && !zip.getText().equals(""))
		{			
			addr = new Address(street.getText(), city.getText(), state.getText(), zip.getText());
			
			if(firstName.getText() != " " && lastName.getText() != " " && phone.getText() != " " && credentials.getText() != " ")
			{
				author = new Author(firstName.getText(), lastName.getText(), phone.getText(), credentials.getText(),addr);
								
				if(checkExist(author.getCredentials())) {
					dao.saveAuthor(author);
					reflist();				
					paneAuthor.setVisible(false);
				}
				else 
				{
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText(null);
					alert.setTitle("Duplicated Author Information");
					alert.setContentText("Author has credentials: " + credentials.getText() + " already exist !");
					alert.show();
				}
			}
			else 
			{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(null);
				alert.setContentText("User info missing !");
				alert.show();
			}
		}
		else 
		{			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText("Address info missing !");
			alert.show();
		}
		
	}
	
	private boolean checkExist(String value)
	{
		boolean nonExistUser = true;
		for(Author a: fullListAuthor.getItems())
		{
			if(a.getCredentials().equals(value)) nonExistUser =false;
		}
		return nonExistUser;
	}
	
	@FXML protected void handleRemoveFromLeft(ActionEvent event) {
		ObservableList<Author> aa =  selectedList.getItems();
		Author author = selectedList.getSelectionModel().getSelectedItem();
		if(aa.contains(author) && author != null ) aa.remove(author);
		else
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText("Please select Author first!");
			alert.show();
		}
		
		selectedList.setItems(aa);
	}
	
	@FXML protected void handleMoveToRigth(ActionEvent event) {
		ObservableList<Author> aa =  selectedList.getItems();
		Author author = fullListAuthor.getSelectionModel().getSelectedItem();
		if(!aa.contains(author) && author != null )aa.add(author);
		else
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText("Please select Author first !");
			alert.show();
		}
		selectedList.setItems(aa);
	}	
	
	public void setSelectedList(ObservableList<Author> list)
	{
		selectedList.setItems(list);
	}
	
	private void reflist()
	{
		ObservableList<Author> data = FXCollections.observableArrayList();
		AuthorList aa = dao.getAuthorList();
		data.addAll(aa.getAuthors());
		fullListAuthor.setItems(data);
	}
	private void clearFields()
	{
		firstName.clear();
		lastName.clear();
		phone.clear();
		credentials.clear();
		street.clear();
		city.clear();
		state.clear();
		zip.clear();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		reflist();
	}
	
	private Stage dialogStage;

	public Stage getDialogStage() {
		return dialogStage;
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public ListView<Author> getSelectedList() {
		return selectedList;
	}

	public void setSelectedList(ListView<Author> selectedList) {
		this.selectedList = selectedList;
	}
	
	
}
