package controller;

import java.time.LocalDate;

import business.dataaccess.DataAccess;
import business.dataaccess.DataAccessFacade;
import business.objects.CheckoutRecordEntry;
import business.objects.Copy;
import business.objects.LibraryMember;
import business.objects.Publication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class CheckoutDialogController {
	private final DataAccess dao = new DataAccessFacade();
	@FXML private TextField tfMemberID;
	@FXML private TextField tfTitle;
	@FXML private TextField tfNumber;
	@FXML private TextField tfMaxCheckout;
	private boolean isCheckout;
	private Stage dialogStage;
	
	public String publicationType;

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
		int memberId = Integer.parseInt(tfMemberID.getText());
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
			Alert alert = new Alert(AlertType.INFORMATION, "There is no available copy for that item!", ButtonType.OK);
			alert.setTitle("Checkout");
			alert.show();
			return;
		}

		LocalDate dueDate = LocalDate.now().plusDays(pub.getMaxCheckoutLength());
		CheckoutRecordEntry entry = new CheckoutRecordEntry(aCopy, LocalDate.now(), dueDate);
		entry.setMember(member);
		
		dao.saveCheckoutRecordEntry(entry);
	
		Alert alert = new Alert(AlertType.INFORMATION, "Checkout successful!", ButtonType.OK);
		alert.setTitle("Checkout");
		alert.show();
		
		isCheckout = true;
		dialogStage.close();
	}
	@FXML protected void handleCancelBtnAction(ActionEvent event) {
		isCheckout = false;
		dialogStage.close();
	}
}
