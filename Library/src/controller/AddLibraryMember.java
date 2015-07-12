package controller;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import business.dataaccess.DataAccess;
import business.dataaccess.DataAccessFacade;
import business.objects.LibraryMember;
import business.objects.Address;
import business.objects.MemberList;

public class AddLibraryMember {
	private final DataAccess dao = DataAccessFacade.getDAO();

	@FXML private TextField tfID;
	@FXML private TextField tfFirstname;
	@FXML private TextField tfLastname;
	@FXML private TextField tfStreet;
	@FXML private TextField tfCity;
	@FXML private TextField tfState;
	@FXML private TextField tfZip;
	@FXML private TextField tfPhone;
	private Parent root;
	
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		root = tfID.getParent();
	}	
	
	@FXML protected void handleSaveMemberBtnAction(ActionEvent event) {

		int memberID = 0;
		try {
			memberID = Integer.parseInt(tfID.getText());
		} catch (NumberFormatException nfe) {
			Alert alert = new Alert(AlertType.ERROR, "", ButtonType.OK);
			alert.setTitle("Add Member");
			alert.setHeaderText(null);
			alert.setContentText("Member ID must be a number!");
			alert.show();
			return;
		}
		
		if(dao.searchLibraryMemberByID(memberID) == null){
			if (!isInputValid()) {
				return;
			}
			Address address = new Address(tfStreet.getText(), 
					  tfCity.getText(), 
					  tfState.getText(), 
					  tfZip.getText());
			
			LibraryMember member = new LibraryMember( memberID,
													 tfFirstname.getText(), 
													 tfLastname.getText(), 
													 tfPhone.getText(), 
													 address);			
			
			dao.saveMember(member);
			Alert alert = new Alert(AlertType.INFORMATION, "", ButtonType.OK);
			alert.setTitle("Add Member");
			alert.setHeaderText(null);
			alert.setContentText(tfFirstname.getText() + " is now saved!");
			alert.show();		
		}
		else {
			Alert alert = new Alert(AlertType.ERROR, "", ButtonType.OK);
			alert.setTitle("Add Member");
			alert.setHeaderText(null);
			alert.setContentText(tfID.getText() + " member already exist!");
			alert.show();	
		}
	}
	
	private boolean isInputValid() {
        String errorMessage = "";
        if (tfFirstname.getText().trim().isEmpty() || tfLastname.getText().trim().isEmpty() || tfStreet.getText().trim().isEmpty() ||
        		tfCity.getText().trim().isEmpty() || tfState.getText().trim().isEmpty()  || tfZip.getText().trim().isEmpty() || tfPhone.getText().trim().isEmpty()) {
			errorMessage += "All fields must be nonempty!\n";
		}
        if (tfID.getLength() > 10) {
        	errorMessage +="First name must not be greater than 10 characters\n";  
		}
        if (!tfFirstname.getText().matches("[a-zA-Z]*")) {
        	errorMessage +="First name field may not contain spaces or characters other than a-z, A-Z\n";  
		}
        if (tfFirstname.getLength() > 30) {
        	errorMessage +="First name must not be greater than 30 characters\n";  
		}        
        if (!tfLastname.getText().matches("[a-zA-Z]*")) {
        	errorMessage +="Last name field may not contain spaces or characters other than a-z, A-Z\n"; 
        }
        if (tfLastname.getLength() > 30) {
        	errorMessage +="Last name must not be greater than 30 characters\n";  
		}
        if (!tfPhone.getText().matches("^[0-9]*")) {
            errorMessage += "No valid a valid phone number!\n"; 
        }
        if (tfPhone.getLength() > 12) {
        	errorMessage +="Phone number must not be greater than 12 characters\n";  
		}
        if (!tfStreet.getText().matches("((?=.*[0-9])(?=.*[a-zA-Z]))")) {
            errorMessage += "Street field can only contain spaces or characters a-z, A-Z and 0-9 \n"; 
        }
        if (tfStreet.getLength() > 30) {
        	errorMessage +="Street must not be greater than 30 characters\n";  
		}
        if (!tfZip.getText().matches("^[0-9]{5,5}")) {
        	errorMessage += "Zip must be numeric with exactly 5 digits\n";  
		} 
        if (!tfCity.getText().matches("[a-zA-Z]*")) {
            errorMessage += "City must consist of Alphabet characters\n"; 
        }
        if (tfCity.getLength() > 30) {
        	errorMessage +="City must not be greater than 30 characters\n";  
		}
        if (!tfState.getText().matches("[a-zA-Z]*")) {
        	errorMessage += "State must consist of Alphabet characters\n";  
		}
        if (tfState.getLength() > 10) {
        	errorMessage +="City must not be greater than 10 characters";  
		}

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(root.getScene().getWindow());
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
	}
}
