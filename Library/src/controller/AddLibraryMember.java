package controller;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
		
		Address address = new Address(tfStreet.getText(), 
				  tfCity.getText(), 
				  tfState.getText(), 
				  tfZip.getText());
		
		LibraryMember member = new LibraryMember( memberID,
												 tfFirstname.getText(), 
												 tfLastname.getText(), 
												 tfPhone.getText(), 
												 address);
		
		if(dao.searchLibraryMemberByID(memberID) == null){
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
}
