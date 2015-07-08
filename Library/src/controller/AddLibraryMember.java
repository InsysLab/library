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
	private final DataAccess dao = new DataAccessFacade();

	@FXML private TextField tfID;
	@FXML private TextField tfFirstname;
	@FXML private TextField tfLastname;
	@FXML private TextField tfStreet;
	@FXML private TextField tfCity;
	@FXML private TextField tfState;
	@FXML private TextField tfZip;
	@FXML private TextField tfPhone;
	
	@FXML protected void handleSaveMemberBtnAction(ActionEvent event) {
		Address address = new Address(tfStreet.getText(), 
									  tfCity.getText(), 
									  tfState.getText(), 
									  tfZip.getText());
		
		LibraryMember member = new LibraryMember( Integer.parseInt(tfID.getText()),
												 tfFirstname.getText(), 
												 tfLastname.getText(), 
												 tfPhone.getText(), 
												 address);
		MemberList mlist = dao.getMemberList();
		if (mlist == null) {
			dao.saveMember(member);
			Alert alert = new Alert(AlertType.INFORMATION, tfFirstname.getText() + " is now saved!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.setTitle("New Member Saved");
			alert.show();
			return;
		}
		List<LibraryMember> memberList = mlist.getMembers();
		
		boolean nonexistmember = true;
		
		for(LibraryMember lm: memberList)
		{
			if(lm.getMemberID() == member.getMemberID()) nonexistmember = false;
		}
		
		if(nonexistmember){
			dao.saveMember(member);
			
			Alert alert = new Alert(AlertType.INFORMATION, tfFirstname.getText() + " is now saved!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.setTitle("New Member Saved");
			alert.show();		
		}
		else {
			Alert alert = new Alert(AlertType.INFORMATION, tfID.getText() + " member already exist!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.setTitle("Duplicated member ID");
			alert.show();	
		}
	}
}
