package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import business.dataaccess.DataAccess;
import business.dataaccess.DataAccessFacade;
import business.objects.LibraryMember;
import business.objects.Address;

public class SearchLibraryMemberController {
	private final DataAccess dao = new DataAccessFacade();
	private int memberId;
	
	@FXML private TextField tfMemberID;
	@FXML private TextField tfFirstName;
	@FXML private TextField tfLastName;
	@FXML private TextField tfStreet;
	@FXML private TextField tfCity;
	@FXML private TextField tfState;
	@FXML private TextField tfZip;
	@FXML private TextField tfPhone;
	@FXML private Button btnUpdate;
	
	@FXML protected void handleSearchMemberBtnAction(ActionEvent event) {

		LibraryMember member = dao.searchLibraryMemberByID(Integer.parseInt(tfMemberID.getText()));
		
		if (member != null) {
			this.memberId = member.getMemberID();
					
			tfMemberID.setText(""+ this.memberId);
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
			
			btnUpdate.setDisable(false);
		} else {
			this.memberId = 0;
			
			Alert alert = new Alert(AlertType.INFORMATION, "Cannot find member id " + tfMemberID.getText(), ButtonType.OK);
			alert.setTitle("Search Library Member");
			alert.show();

			tfFirstName.clear();
			tfLastName.clear();
			tfPhone.clear();
			tfStreet.clear();
			tfCity.clear();
			tfState.clear();
			tfZip.clear();
			
			btnUpdate.setDisable(true);
		}
	}
	
	@FXML protected void handleUpdateMemberBtnAction(ActionEvent event) {
		int id = -1;
		try {
			 id = Integer.parseInt(tfMemberID.getText());
		} catch (NumberFormatException nfe) {
			return;
		}
		
		if( this.memberId != id ){
			Alert alert = new Alert(AlertType.ERROR, "Member ID is not editable", ButtonType.OK);
			alert.setTitle("Member Update");
			alert.show();	
			return;
		}		
		
		LibraryMember mem = dao.searchLibraryMemberByID(id);
		
		if (mem != null) {
			Address address = new Address(tfStreet.getText(), tfCity.getText(), tfState.getText(), tfZip.getText());
			LibraryMember member = new LibraryMember(id, tfFirstName.getText(), tfLastName.getText(), tfPhone.getText(), address);
			dao.saveUpdateMember(member);
			
			Alert alert = new Alert(AlertType.INFORMATION, "Member details has been updated!", ButtonType.OK);
			alert.setTitle("Update Library Member");
			alert.show();
		} 
	}
}

