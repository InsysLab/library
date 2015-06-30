package controller;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import business.dataaccess.DataAccess;
import business.dataaccess.DataAccessFacade;
import business.objects.Book;
import business.objects.BookList;

public class AddPublicationController {
	private final DataAccess dao = new DataAccessFacade();

	@FXML private TextField perTitle;
	@FXML private TextField perIssueNum;
	@FXML private TextField perMaxCODays;
	
	@FXML protected void handleSavePerBtnAction(ActionEvent event) {
		//System.out.println(((Button)event.getSource()).getProperties());
		System.out.println("Location of 'user.dir':\n  "+DataAccessFacade.OUTPUT_DIR);
		dao.saveBook(new Book(perIssueNum.getText(), Integer.parseInt(perMaxCODays.getText()), perTitle.getText()));
		System.out.println(perTitle.getText() + " " + perIssueNum.getText() + " " + perMaxCODays.getText());
		
		Book book = dao.getBookByTitle(perTitle.getText());
		book.toString();
		
		BookList bookList = dao.getBookList();
		System.out.println("------------");
		System.out.println(dao);
		for (Book bk: (ArrayList<Book>) bookList.getBooks()) {
			bk.toString();
		}
	}
}
