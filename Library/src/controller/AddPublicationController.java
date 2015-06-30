package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import business.dataaccess.DataAccess;
import business.dataaccess.DataAccessFacade;
import business.objects.Book;

public class AddPublicationController {
	private final DataAccess dao = new DataAccessFacade();

	@FXML private TextField perTitle;
	@FXML private TextField perIssueNum;
	@FXML private TextField perMaxCODays;
	
	@FXML protected void handleSavePerBtnAction(ActionEvent event) {
		//System.out.println(((Button)event.getSource()).getProperties());
		dao.saveBook(new Book(perIssueNum.getText(), Integer.parseInt(perMaxCODays.getText()), perTitle.getText()));
		System.out.println(perTitle.getText() + " " + perIssueNum.getText() + " " + perMaxCODays.getText());
		
		
	}
}
