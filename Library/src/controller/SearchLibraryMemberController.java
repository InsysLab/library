package controller;

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

public class SearchLibraryMemberController {
	private final DataAccess dao = new DataAccessFacade();

	@FXML private TextField tfMemberID;
	@FXML private TextField tfFirstName;
	@FXML private TextField tfLastName;
	@FXML private TextField tfStreet;
	@FXML private TextField tfCity;
	@FXML private TextField tfState;
	@FXML private TextField tfZip;
	@FXML private TextField tfPhone;
	
	@FXML protected void handleSearchMemberBtnAction(ActionEvent event) {
		
		LibraryMember member = dao.searchLibraryMemberByID(Integer.parseInt(tfMemberID.getText()));
		if (member != null) {
			tfMemberID.setText(""+member.getMemberID());
			tfFirstName.setText(member.getFirstName());
			tfLastName.setText(member.getLastName());
			tfPhone.setText(member.getPhone());
			Address address = member.getAddress();
			if (address!=null) {
				tfStreet.setText(address.getStreet());
				tfCity.setText(address.getCity());
				tfState.setText(address.getState());
				tfZip.setText(address.getZip());
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR, "Cannot find member id " + tfMemberID.getText(), ButtonType.OK);
			alert.setTitle("Search Library Member");
			alert.setContentText("Error");
			alert.show();
		}
		

	}
}

