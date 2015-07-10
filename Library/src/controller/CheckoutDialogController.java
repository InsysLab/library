package controller;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import business.dataaccess.DataAccess;
import business.dataaccess.DataAccessFacade;
import business.objects.Book;
import business.objects.CheckoutRecordEntry;
import business.objects.Copy;
import business.objects.LibraryMember;
import business.objects.Periodical;
import business.objects.Publication;

public class CheckoutDialogController {
	private final DataAccess dao = DataAccessFacade.getDAO();

	@FXML private TextField tfMemberID;
	@FXML private TextField tfTitle;
	@FXML private TextField tfNumber;
	@FXML private TextField tfMaxCheckout;
	@FXML private TextField tfName;
	private boolean isCheckout;
	private Stage dialogStage;
	public String publicationType;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

    }

	public void setPublicationType(String publicationType) {
		this.publicationType = publicationType;
	}
	public boolean isCheckout() {
		return isCheckout;
	}

	public Stage getDialogStage() {
		return dialogStage;
	}
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	@FXML protected void handleCheckoutBtnAction(ActionEvent event) {
		int memberId = 0;
		try {
			memberId = Integer.parseInt(tfMemberID.getText());
		} catch (NumberFormatException nfe) {
			Alert alert = new Alert(AlertType.ERROR, "Member ID should be numberic!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.setTitle("Checkout Record");
			alert.show();
			tfMemberID.setText("");
			tfName.setText("");
			return;
		}
		String number = tfNumber.getText();

		LibraryMember member = dao.searchLibraryMemberByID(memberId);
		Publication pub = null;
		
		if( publicationType.equals("Book") ){
			pub = dao.getBookByISBN(number);
		} else {
			pub = dao.searchPeriodicalByIssueNo(number);
		}
		
		//Check if a copy is available
		Copy aCopy = pub.getAvailableCopy();
		if( aCopy == null ){
			Alert alert = new Alert(AlertType.ERROR, "There is no available copy for that item!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.setTitle("Checkout");
			alert.show();
			isCheckout = false;
			return;
		}

		LocalDate dueDate = LocalDate.now().plusDays(pub.getMaxCheckoutLength());
		CheckoutRecordEntry entry = new CheckoutRecordEntry(aCopy, LocalDate.now(), dueDate);
		entry.setMember(member);
		
		dao.saveCheckoutRecordEntry(entry);
		
		//Update publication copy
		pub.checkoutACopy(1);
		
		if(publicationType.equals("Book")){
			dao.saveUpdateBook( (Book)pub );
		} else {
			dao.saveUpdatePeriodical( (Periodical)pub );
		}
	
		Alert alert = new Alert(AlertType.INFORMATION, "Checkout successful!", ButtonType.OK);
		alert.setHeaderText(null);
		alert.setTitle("Checkout");
		alert.show();
		
		isCheckout = true;
		dialogStage.close();
	}
	@FXML protected void handleCancelBtnAction(ActionEvent event) {
		isCheckout = false;
		dialogStage.close();
	}
	@FXML protected void handleOnKeyTyped(ActionEvent event) {
		int memberid = 0;
		try {
			memberid = Integer.parseInt(tfMemberID.getText());
		} catch (NumberFormatException nfe) {
			Alert alert = new Alert(AlertType.ERROR, "Member ID should be numberic!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.setTitle("Checkout Record");
			alert.show();
			tfMemberID.setText("");
			tfName.setText("");
			return;
		}
		LibraryMember member = dao.searchLibraryMemberByID(memberid);
		
		if (member != null) {
			tfName.setText(member.getFirstName() + " " + member.getLastName());
		} else {
			Alert alert = new Alert(AlertType.ERROR, "Member ID " + memberid + " does not exist!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.setTitle("Checkout Record");
			alert.show();
			tfName.setText("");
		}
	}
	public TextField getTfTitle() {
		return tfTitle;
	}

	public TextField getTfNumber() {
		return tfNumber;
	}

	public TextField getTfMaxCheckout() {
		return tfMaxCheckout;
	}
	
	

}
