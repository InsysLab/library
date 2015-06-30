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
		
		//BookList booklist = BookList.getInstance();
		
		//booklist.addBook(new Book(perIssueNum.getText(), Integer.parseInt(perMaxCODays.getText()), perTitle.getText()));
		
		//dao.saveBookList(booklist);
	//	dao.saveBook(new Book(perIssueNum.getText(), Integer.parseInt(perMaxCODays.getText()), perTitle.getText()));
	//	System.out.println(perTitle.getText() + " " + perIssueNum.getText() + " " + perMaxCODays.getText());
		
	//	Book book = dao.getBookByTitle(perTitle.getText());
	//	System.out.println(book.toString());
		
		
	//	System.out.println("------------");
		//System.out.println(booklist.toString());
		BookList list = dao.getBookList();
		System.out.println(list.toString());
		
	}
}
