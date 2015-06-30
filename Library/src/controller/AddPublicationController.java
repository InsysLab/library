package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddPublicationController {

	@FXML private TextField perTitle;
	@FXML private TextField perIssueNum;
	@FXML private TextField perMaxCODays;
	
	@FXML protected void handleSavePerBtnAction(ActionEvent event) {
		//System.out.println(((Button)event.getSource()).getProperties());
		
		System.out.println(perTitle.getText() + " " + perIssueNum.getText() + " " + perMaxCODays.getText());
	}
}
