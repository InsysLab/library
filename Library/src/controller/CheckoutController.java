package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import business.dataaccess.DataAccess;
import business.dataaccess.DataAccessFacade;
import business.objects.CheckoutRecord;
import business.objects.CheckoutRecordEntry;
import business.objects.LibraryMember;
import business.objects.Publication;
import business.objects.Book;
import business.objects.BookList;
import business.objects.Periodical;
import business.objects.PeriodicalList;
import business.objects.Copy;
import table.objects.CheckoutRecordTable;

public class CheckoutController {
	private final DataAccess dao = DataAccessFacade.getDAO();

	@FXML private TextField tfMemberID;
	@FXML private TextField tfMemberName;
	@FXML private TextField tfISBN;
	@FXML private TextField tfBookTitle;
	@FXML private TextField tfIssueNumber;
	@FXML private Button btnSearch;
	@FXML private Button btnAddCheckout;
	@FXML private Button btnSubmitCheckout;
	@FXML private HBox hbSearchResult;
	
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {

	}
	
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
			if( books != null && books.size() > 0 ){
				Book b = books.get(0);
				tfISBN.setText(b.getISBN());
				tfBookTitle.setText(b.getTitle());
				tfIssueNumber.clear();		
				found = true;
			} else {
				ArrayList<Periodical> periodicals = dao.wildSearchPeriodicalByTitle(title);
				if( periodicals != null && periodicals.size() > 0 ){
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
		}
		
	}	
	
	@FXML protected void handleAddCheckoutBtnAction(ActionEvent event) {
		int memberId = Integer.parseInt(tfMemberID.getText());
		String isbn = tfISBN.getText();
		String title = tfBookTitle.getText();
		String issue = tfIssueNumber.getText();		

		LibraryMember member = dao.searchLibraryMemberByID(memberId);
		Publication pub = null;
		
		if( ! isbn.isEmpty() ){
			pub = dao.getBookByISBN(isbn);
		} else if( ! issue.isEmpty()  ){
			pub = dao.searchPeriodicalByIssueNo(issue);
		} else {
			return;
		}		
		
		//Check if a copy is available
		Copy aCopy = pub.getAvailableCopy();
		if( aCopy == null ){
			Alert alert = new Alert(AlertType.INFORMATION, "There is no available copy for that item!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.setTitle("Checkout");
			alert.show();
			return;
		}

		LocalDate dueDate = LocalDate.now().plusDays(pub.getMaxCheckoutLength());
		CheckoutRecordEntry entry = new CheckoutRecordEntry(aCopy, LocalDate.now(), dueDate);
		entry.setMember(member);
		
		dao.saveCheckoutRecordEntry(entry);
		
		//Update publication copy
		pub.checkoutACopy(1);
		
		if(pub.getClass().getName().contains("Book")){
			dao.saveUpdateBook( (Book)pub );
		} else {
			dao.saveUpdatePeriodical( (Periodical)pub );
		}
		
		Alert alert = new Alert(AlertType.INFORMATION, "Checkout successful!", ButtonType.OK);
		alert.setHeaderText(null);
		alert.setTitle("Checkout");
		alert.show();
		
		tfISBN.clear();
		tfBookTitle.clear();
		tfIssueNumber.clear();			
	}	
	
	private TableView getCheckoutTable(ArrayList<CheckoutRecordTable> list) {
		TableView table = new TableView();
		table.setPrefWidth(500);
		TableColumn colTitle = new TableColumn("Title");
		colTitle.setPrefWidth(125);
		colTitle.setCellValueFactory(
                new PropertyValueFactory<CheckoutRecordTable, String>("title"));
		
		TableColumn colISBN = new TableColumn("ISBN/Issue No");
		colISBN.setPrefWidth(125);
		colISBN.setCellValueFactory(
                new PropertyValueFactory<CheckoutRecordTable, String>("number"));
		
		TableColumn colBorrowedDate = new TableColumn("Borrowed Date");
		colBorrowedDate.setPrefWidth(125);
		colBorrowedDate.setCellValueFactory(
                new PropertyValueFactory<CheckoutRecordTable, String>("borrowedDate"));
		
		TableColumn colDueDate = new TableColumn("Due Date");
		colDueDate.setPrefWidth(125);
		colDueDate.setCellValueFactory(
                new PropertyValueFactory<CheckoutRecordTable, String>("dueDate"));

		ObservableList<CheckoutRecordTable> data = FXCollections.observableArrayList();
		data.addAll(list);
		table.setItems(data);
		table.getColumns().addAll(colTitle,colISBN, colBorrowedDate, colDueDate);
		
		return table;
	}
	
	@FXML protected void handleSubmitCheckoutBtnAction(ActionEvent event) {
		
	}		
}
