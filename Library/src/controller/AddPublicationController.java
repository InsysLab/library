package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import business.dataaccess.DataAccess;
import business.dataaccess.DataAccessFacade;
import business.objects.Book;
import business.objects.Periodical;

public class AddPublicationController {
	private final DataAccess dao = new DataAccessFacade();

	@FXML private TextField perTitle;
	@FXML private TextField perIssueNum;
	@FXML private TextField perMaxCODays;
	@FXML private TextField tfBookTitle;
	@FXML private TextField tfISBN;
	@FXML private TextField tfBookMaxCODays;
	
	@FXML protected void handleSavePerBtnAction(ActionEvent event) {
		Periodical periodical = new Periodical(perTitle.getText(), perIssueNum.getText(), Integer.parseInt(perMaxCODays.getText()));
		dao.savePeriodical(periodical);
		
	}
	
	@FXML protected void handleSaveBookBtnAction(ActionEvent event) {
		Book book = new Book(tfISBN.getText(), Integer.parseInt(tfBookMaxCODays.getText()), tfBookTitle.getText());
		dao.saveBook(book);
	}
}
