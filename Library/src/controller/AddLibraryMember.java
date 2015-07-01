package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import business.dataaccess.DataAccess;
import business.dataaccess.DataAccessFacade;
import business.objects.LibraryMember;
import business.objects.Address;

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
		
		dao.saveMember(member);
	}
}
