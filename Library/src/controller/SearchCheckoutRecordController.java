package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import table.objects.CheckoutRecordTable;
import business.dataaccess.DataAccess;
import business.dataaccess.DataAccessFacade;
import business.objects.Address;
import business.objects.Book;
import business.objects.CheckoutRecord;
import business.objects.CheckoutRecordEntry;
import business.objects.Copy;
import business.objects.LibraryMember;

public class SearchCheckoutRecordController {
	private final DataAccess dao = new DataAccessFacade();
	@FXML private TextField tfSearchID;
	@FXML private TextField tfMemberName;
	@FXML private Label lblSearchStatus;
	@FXML private HBox hbSearchResult;
	
    @FXML // This method is called by the FXMLLoader when initialization is complete
   void initialize() {

   }
	
   @FXML protected void onSearchBtnAction(ActionEvent event) {
/*	   CheckoutRecord cr = new CheckoutRecord();
	   Book pub1 = new Book("123", 1, "Java");
	   Copy copy = new Copy("1", pub1);
	   LocalDate loc = LocalDate.now();
	   CheckoutRecordEntry crEntry = new CheckoutRecordEntry(copy, loc, loc.plusDays(pub1.getMaxcheckoutlength()));
	   LibraryMember member = new LibraryMember(12, "Jesus", "Sadang", "1234", new Address("1","1","1","1"));
	   crEntry.setMember(member);
	   
	   ArrayList<CheckoutRecordTable> list = new ArrayList<CheckoutRecordTable>();
	   CheckoutRecordTable chkRec = new CheckoutRecordTable();
	   chkRec.setTitle(pub1.getTitle());
	   chkRec.setNumber(pub1.getISBN());
	   chkRec.setBorrowedDate(crEntry.getCheckoutDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
	   chkRec.setDueDate(crEntry.getDueDate().format(DateTimeFormatter.ISO_LOCAL_DATE));*/
	   //CheckoutRecordEntry en1 = new CheckoutRecordEntry();
	   
	   CheckoutRecord checkoutRecord = dao.getCheckoutRecord();
	   ArrayList<CheckoutRecordTable> list = new ArrayList<CheckoutRecordTable>();
	   
	   List<CheckoutRecordEntry> entries = checkoutRecord.getEntry();
	   for(CheckoutRecordEntry ce: entries){
		   CheckoutRecordTable chkRec = new CheckoutRecordTable();
		   chkRec.setTitle(ce.getCopy().getPublication().getTitle());
		   chkRec.setNumber(ce.getCopy().getPublication().getNumber());
		   chkRec.setBorrowedDate(ce.getCheckoutDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
		   chkRec.setDueDate(ce.getDueDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
		   chkRec.setMemberName(ce.getMember().getFirstName() + " " + ce.getMember().getLastName());
		   list.add(chkRec);
	   }

	   if (hbSearchResult.getChildren().size() == 1) {
			hbSearchResult.getChildren().remove(0);
		}
		if (list!=null && list.size() > 0) {
			lblSearchStatus.setText("Search result...");
			hbSearchResult.getChildren().add(getCheckoutTable(list));
		} else {
			lblSearchStatus.setText("Search did not find anything...");
		}
	}
	
	
	private TableView getCheckoutTable(ArrayList<CheckoutRecordTable> list) {
		TableView table = new TableView();
		table.setPrefWidth(600);

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
		
		TableColumn colMemberName = new TableColumn("Member");
		colMemberName.setPrefWidth(125);
		colMemberName.setCellValueFactory(
                new PropertyValueFactory<CheckoutRecordTable, String>("memberName"));		

		ObservableList<CheckoutRecordTable> data = FXCollections.observableArrayList();
		data.addAll(list);
		table.setItems(data);
		table.getColumns().addAll(colTitle,colISBN, colBorrowedDate, colDueDate, colMemberName);
		
		return table;
	}
	
}
