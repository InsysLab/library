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
	private final DataAccess dao = DataAccessFacade.getDAO();
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

		try {
			Integer.parseInt(tfMemberID.getText());
		} catch (NumberFormatException nfe) {
			Alert alert = new Alert(AlertType.ERROR, "Member ID should be numberic", ButtonType.OK);
			alert.setHeaderText(null);
			alert.setTitle("Checkout Record");
			alert.show();
			return;
		}
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
			
			Alert alert = new Alert(AlertType.ERROR, "Cannot find member id " + tfMemberID.getText(), ButtonType.OK);
			alert.setHeaderText(null);
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
			alert.setHeaderText(null);
			alert.setTitle("Member Update");
			alert.show();	
			return;
		}		
		
		LibraryMember mem = dao.searchLibraryMemberByID(id);
		
		if (mem != null) {
			Address address = mem.getAddress();
			address.setStreet(tfStreet.getText());
			address.setCity(tfCity.getText());
			address.setState(tfState.getText());
			address.setZip(tfZip.getText());
			LibraryMember member = new LibraryMember(id, tfFirstName.getText(), tfLastName.getText(), tfPhone.getText(), address);
			dao.saveUpdateMember(member);
			
			Alert alert = new Alert(AlertType.INFORMATION, "Member details has been updated!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.setTitle("Update Library Member");
			alert.show();
		} 
	}
}

