package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import business.dataaccess.DataAccess;
import business.dataaccess.DataAccessFacade;
import business.objects.Address;
import business.objects.Author;
import business.objects.AuthorList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AuthorController implements Initializable{
	
	private final DataAccess dao = new DataAccessFacade();
	private AddPublicationController pc1= null;	
	
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
//		ObservableList<Author> olist= selectedList.getItems();
//		pc1.setAuthorList(olist);
		dialogStage.close();
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
				dao.saveAuthor(author);
				reflist();
				
				paneAuthor.setVisible(false);
			}
			else 
			{
				
			}
		}
		else 
		{			
			
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		reflist();
	}

	public void initData(AddPublicationController pc) {
	    	this.pc1 = pc;
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
