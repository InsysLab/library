package controller;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import business.dataaccess.DataAccess;
import business.dataaccess.DataAccessFacade;
import business.objects.CheckoutRecord;
import business.objects.LibraryMember;
import business.objects.Publication;
import business.objects.Book;
import business.objects.BookList;
import business.objects.Periodical;
import business.objects.PeriodicalList;

public class CheckoutController {
	private final DataAccess dao = new DataAccessFacade();
	private ArrayList<Publication> publications = null;

	@FXML private TextField tfMemberID;
	@FXML private TextField tfMemberName;
	@FXML private TextField tfISBN;
	@FXML private TextField tfBookTitle;
	@FXML private TextField tfIssueNumber;
	@FXML private Button btnSearch;
	@FXML private Button btnAddCheckout;
	@FXML private Button btnSubmitCheckout;
	@FXML private TableView tblCheckout;
	
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
			tfMemberName.clear();		
			return;
		}
		
		tfMemberName.setText(member.getFirstName() + " " + member.getLastName());
		Boolean found = false;
		
		if( ! isbn.isEmpty() ){
			Book bk = dao.getBookByISBN(isbn);
			if( bk != null ){
				tfBookTitle.setText(bk.getTitle());
				found = true;
			}
		} else if( ! issue.isEmpty() ){
			Periodical periodical = dao.searchPeriodicalByIssueNo(issue);
			if( periodical != null ){
				tfBookTitle.setText(periodical.getTitle());
				found = true;
			}		
		} else if( ! title.isEmpty() ) {
			ArrayList<Book> books = dao.wildSearchBookByTitle(title);
			if( books != null ){
				Book b = books.get(0);
				tfISBN.setText(b.getISBN());
				tfBookTitle.setText(b.getTitle());
				tfIssueNumber.clear();		
				found = true;
			} else {
				ArrayList<Periodical> periodicals = dao.wildSearchPeriodicalByTitle(title);
				if( periodicals != null ){
					Periodical p = periodicals.get(0);
					tfBookTitle.setText(p.getTitle());
					tfIssueNumber.setText(p.getIssueNo());
					tfISBN.clear();
					found = true;
				}
			}			
		}
		
		if( found ){
			btnAddCheckout.setDisable(false);
		} else {
			alert.setContentText("Cannot find the publication.");
			alert.show();
			btnAddCheckout.setDisable(true);
			
			//tfISBN.clear();
			//tfBookTitle.clear();
			//tfIssueNumber.clear();
		}
		
	}	
	
	@FXML protected void handleAddCheckoutBtnAction(ActionEvent event) {
		int memberId = Integer.parseInt(tfMemberID.getText());
		String isbn = tfISBN.getText();
		String title = tfBookTitle.getText();
		String issue = tfIssueNumber.getText();		
		
		LibraryMember member = dao.searchLibraryMemberByID(memberId);
		
		if( ! isbn.isEmpty() ){
			Book bk = dao.getBookByISBN(isbn);
			
		} else if( ! issue.isEmpty()  ){
			Periodical pe = dao.searchPeriodicalByIssueNo(issue);
			
		}		
		
		
		/*
		ObservableList<Publication> data = FXCollections.observableArrayList();
		data.addAll(publications);		
		tblCheckout.setItems(data);
        */
        //tblCheckout.getColumns().addAll(col);
	}	
	
	@FXML protected void handleSubmitCheckoutBtnAction(ActionEvent event) {
		
	}		
}
