package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CheckoutDialogController {
	@FXML private TextField tfMemberID;
	@FXML private TextField tfTitle;
	@FXML private TextField tfNumber;
	@FXML private TextField tfMaxCheckout;
	private boolean isCheckout;

	public boolean isCheckout() {
		return isCheckout;
	}
	private Stage dialogStage;
	public Stage getDialogStage() {
		return dialogStage;
	}
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	@FXML protected void handleCheckoutBtnAction(ActionEvent event) {
		isCheckout = true;
		dialogStage.close();
	}
	@FXML protected void handleCancelBtnAction(ActionEvent event) {
		isCheckout = false;
		dialogStage.close();
	}
}
