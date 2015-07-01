package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import business.dataaccess.DataAccess;
import business.dataaccess.DataAccessFacade;
import business.objects.CheckoutRecord;
import business.objects.LibraryMember;

public class CheckoutController {
	private final DataAccess dao = new DataAccessFacade();
	
	@FXML private TextField tfMemberID;
	@FXML private TextField tfMemberName;
	@FXML private TextField tfISBN;
	@FXML private TextField tfBookTitle;
	@FXML private TextField tfIssueNumber;
	@FXML private Button btnSearch;
	@FXML private Button btnAddCheckout;
	@FXML private Button btnSubmitCheckout;
	//@FXML private TableView tblCheckout;
	
	@FXML protected void handleSearchBtnAction(ActionEvent event) {
		int memberId = Integer.parseInt(tfMemberID.getText());
		String isbn = tfISBN.getText();
		String title = tfBookTitle.getText();
		String issue = tfIssueNumber.getText();
		
		LibraryMember member = dao.searchLibraryMemberByID(memberId);
		Alert alert = new Alert(AlertType.INFORMATION, "Cannot find member id " + memberId, ButtonType.OK);
		alert.setTitle("Checkout");
		
		if( member == null ){
			alert.show();			
			return;
		}
		
		tfMemberName.setText(member.getFirstName() + " " + member.getLastName());
		
		if( ! isbn.isEmpty() ){
			
		}
	}	
	
	@FXML protected void handleAddCheckoutBtnAction(ActionEvent event) {
		
	}	
	
	@FXML protected void handleSubmitCheckoutBtnAction(ActionEvent event) {
		
	}		
}
