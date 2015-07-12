package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import business.dataaccess.DataAccess;
import business.dataaccess.DataAccessFacade;
import business.objects.Address;
import business.objects.Author;

public class AuthorController implements Initializable{
	
	private final DataAccess dao = DataAccessFacade.getDAO();
	
@FXML private Pane paneAuthor;
@FXML private ListView<Author> fullListAuthor;
@FXML private ListView<Author> selectedList;

@FXML private TextField firstName;
@FXML private TextField lastName;
@FXML private TextField phone;
@FXML private TextField bio;
@FXML private TextField street;
@FXML private TextField city;
@FXML private TextField state;
@FXML private TextField zip;
@FXML private Button SetAuthor;

	@FXML protected void handleSetAuthor(ActionEvent event) {
		dialogStage.close();
	}
	
	@FXML protected void handleKeyReleasedCredentials(ActionEvent event) {
//		
//		if(!checkExist(bio.getText()))
//		{
//			Alert alert = new Alert(AlertType.INFORMATION);
//			alert.setHeaderText(null);
//			alert.setTitle("Duplicated Author Information");
//			alert.setContentText("Author has bio: " + bio.getText() + " already exist !");
//			alert.show();
//		}
	}
	
	@FXML protected void handleAddAuthor(ActionEvent event) {
		paneAuthor.setVisible(true);
		clearFields();
	}

	@FXML protected void handleSaveAuthor(ActionEvent event) {
		Address addr = null;
		Author author = null;
		
		if(isInputValid()){
		
				addr = new Address(street.getText(), city.getText(), state.getText(), zip.getText());
				author = new Author(firstName.getText(), lastName.getText(), phone.getText(), bio.getText(),addr);
								
				if(checkExist(author)) {
					dao.saveAuthor(author);
					reflist();				
					paneAuthor.setVisible(false);
				}
				
		}
		
	}
	
	private boolean checkExist(Author value)
	{
		boolean nonExistUser = true;
		for(Author a: fullListAuthor.getItems())
		{
			if(value.getFirstName().toUpperCase().equals(a.getFirstName().toUpperCase()) && 
			   value.getLastName().toUpperCase().equals(a.getLastName().toUpperCase())) {
				nonExistUser =false;
				break;
			}
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
		ArrayList<Author> aa = dao.getAuthorList();
		data.addAll(aa);
		fullListAuthor.setItems(data);
	}
	private void clearFields()
	{
		firstName.clear();
		lastName.clear();
		phone.clear();
		bio.clear();
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
	
	private boolean isInputValid() {
        String errorMessage = "";
        if (firstName.getText().trim().isEmpty() || lastName.getText().trim().isEmpty() || phone.getText().trim().isEmpty() ||
        		bio.getText().trim().isEmpty() || street.getText().trim().isEmpty()  || city.getText().trim().isEmpty() || state.getText().trim().isEmpty() || zip.getText().trim().isEmpty()) {
			errorMessage += "All fields must be nonempty!\n";
		}

        if (!firstName.getText().matches("[a-zA-Z]*")) {
        	errorMessage +="First name field may not contain spaces or characters other than a-z, A-Z\n";  
		}
        if (!lastName.getText().matches("[a-zA-Z]*")) {
        	errorMessage +="Last name field may not contain spaces or characters other than a-z, A-Z\n"; 
        }
        if (!phone.getText().matches("^[0-9]*")) {
            errorMessage += "No valid first name!\n"; 
        }
        if (!bio.getText().matches("[a-zA-Z]*")) {
            errorMessage += "No valid bio!\n"; 
        }
        
        if (!street.getText().matches("((?=.*[0-9])(?=.*[a-zA-Z]))")) {
            errorMessage += "Street field can contain spaces or characters a-z, A-Z and 0-9 \n"; 
        }

        if (!zip.getText().matches("^[0-9]{5,5}")) {
        	errorMessage += "Zip must be numeric with exactly 5 digits!\n";  
		} 

        if (!city.getText().matches("[a-zA-Z]*")) {
            errorMessage += "No valid city!\n"; 
        }

        if (!state.getText().matches("^[A-Z][A-Z]$")) {
        	errorMessage += "State must have exactly two characters in the range A-Z!\n";  
		}

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
	}
}
