package controller;

import java.util.List;

import validator.AddressValidator;
import validator.MemberValidator;
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
		if (!isInputValid()) {
			return;
		}
		int memberID = Integer.parseInt(tfID.getText());;
		
		if(dao.searchLibraryMemberByID(memberID) == null){
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
        errorMessage+=MemberValidator.validateMember(tfID.getText(), tfFirstname.getText(), tfLastname.getText(), tfPhone.getText());
        errorMessage+=AddressValidator.validateAddress(tfStreet.getText(), tfCity.getText(), tfState.getText(), tfZip.getText());        

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            //alert.initOwner(root.getScene().getWindow());
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
	}
}
