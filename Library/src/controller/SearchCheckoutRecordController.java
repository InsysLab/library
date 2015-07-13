package controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Optional;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Dialog;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import table.objects.CheckoutRecordTable;
import business.dataaccess.DataAccess;
import business.dataaccess.DataAccessFacade;
import business.objects.Address;
import business.objects.Book;
import business.objects.CheckoutRecord;
import business.objects.CheckoutRecordEntry;
import business.objects.Copy;
import business.objects.LibraryMember;
import business.objects.Periodical;
import business.objects.Publication;

public class SearchCheckoutRecordController {
	private final DataAccess dao = DataAccessFacade.getDAO();
	@FXML private TextField tfSearchID;
	@FXML private TextField tfMemberName;
	@FXML private Label lblSearchStatus;
	@FXML private HBox hbSearchResult;
	@FXML private Button btnPrint;
	private Parent root;
	
	TableView table = new TableView();
	
    @FXML // This method is called by the FXMLLoader when initialization is complete
   void initialize() {
    	btnPrint.setVisible(false);
   }
	
   @FXML protected void onSearchBtnAction(ActionEvent event) {
	   
	   if( tfSearchID.getText().isEmpty() ){
		   return;
	   }
	   
	   Alert alert = new Alert(AlertType.ERROR, "Member ID should be numberic", ButtonType.OK);
	   alert.setHeaderText(null);
	   alert.setTitle("Checkout Record");
	   
	   int memberId = -1;
	   try {
		   memberId = Integer.parseInt(tfSearchID.getText());
	   } catch (NumberFormatException nfe) {
		   alert.show();
		   return;
	   }
	   
	   LibraryMember member = dao.searchLibraryMemberByID(memberId);
	   
	   if( member == null ){
			alert.setContentText("Member ID does not exist");
			alert.show();
			
			tfMemberName.clear();
			hbSearchResult.getChildren().clear();
			lblSearchStatus.setText("");
			return;
	   }
	   
	   tfMemberName.setText(member.getFirstName() + " " + member.getLastName());
	   
	   List<CheckoutRecordEntry> checkoutRecord = dao.getCheckoutRecordEntryByMemberID( memberId );
	   
	   if(checkoutRecord == null){
		   if (hbSearchResult.getChildren().size() == 1) {
			   hbSearchResult.getChildren().remove(0);
		   }
		   alert.setContentText("Member has no checkout record");
		   alert.show();		   
		   return;
	   }
	   
	   ArrayList<CheckoutRecordTable> list = this.getTableList(checkoutRecord);

	   if (hbSearchResult.getChildren().size() == 1) {
			hbSearchResult.getChildren().remove(0);
		}
		if (list!=null && list.size() > 0) {
			lblSearchStatus.setText("Search result...");
			hbSearchResult.getChildren().add(getCheckoutTable(list));
			btnPrint.setVisible(true);
		} else {
			lblSearchStatus.setText("Search did not find anything...");
			btnPrint.setVisible(false);
		}
	}
	
    private ArrayList<CheckoutRecordTable> getTableList(List<CheckoutRecordEntry> checkoutRecord){
    	ArrayList<CheckoutRecordTable> list = new ArrayList<CheckoutRecordTable>();
   	 
 	   for(CheckoutRecordEntry ce: checkoutRecord){
 		   CheckoutRecordTable chkRec = new CheckoutRecordTable();
 		   chkRec.setTitle(ce.getCopy().getPublication().getTitle());
 		   chkRec.setNumber(ce.getCopy().getPublication().getNumber());
 		   chkRec.setBorrowedDate(ce.getCheckoutDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
 		   chkRec.setDueDate(ce.getDueDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
 		   chkRec.setCopyNum(ce.getCopy().getCopyNo());
 		   
 		   if(ce.getDueDate().isBefore(LocalDate.now())){
 			    //Period betweenDates = Period.between(ce.getDueDate(), LocalDate.now());
 			   long dayDiff = ChronoUnit.DAYS.between(ce.getDueDate(), LocalDate.now());
 			    chkRec.setStatus(dayDiff + " OD "); // Overdue days
 		   } else {
 			   chkRec.setStatus("");
 		   }
 		   
 		   if (ce.getCopy().getPublication() instanceof Book) {
 			   chkRec.setType("B");
 		   } else {
 			   chkRec.setType("P");
 		   }
 		   
 		   list.add(chkRec);
 	   }
 	   
 	   return list;
    }
	
	private TableView getCheckoutTable(ArrayList<CheckoutRecordTable> list) {
		table.getColumns().clear();
		table.setPrefWidth(800);

		TableColumn colTitle = new TableColumn("Title");
		colTitle.setPrefWidth(125);
		colTitle.setCellValueFactory(
                new PropertyValueFactory<CheckoutRecordTable, String>("title"));
		
		TableColumn colISBN = new TableColumn("ISBN/Issue #");
		colISBN.setPrefWidth(80);
		colISBN.setCellValueFactory(
                new PropertyValueFactory<CheckoutRecordTable, String>("number"));
		
		TableColumn colCopy = new TableColumn("Copy #");
		colCopy.setPrefWidth(50);
		colCopy.setCellValueFactory(
                new PropertyValueFactory<CheckoutRecordTable, String>("copyNum"));

		TableColumn colType = new TableColumn("Type");
		colType.setPrefWidth(40);
		colType.setCellValueFactory(
                new PropertyValueFactory<CheckoutRecordTable, String>("type"));
		
		TableColumn colBorrowedDate = new TableColumn("Borrowed");
		colBorrowedDate.setPrefWidth(80);
		colBorrowedDate.setCellValueFactory(
                new PropertyValueFactory<CheckoutRecordTable, String>("borrowedDate"));
		
		TableColumn colDueDate = new TableColumn("Due");
		colDueDate.setPrefWidth(80);
		colDueDate.setCellValueFactory(
                new PropertyValueFactory<CheckoutRecordTable, String>("dueDate"));	
		
		TableColumn colStatus = new TableColumn("Status");
		colStatus.setPrefWidth(100);
		colStatus.setCellValueFactory(
                new PropertyValueFactory<CheckoutRecordTable, String>("status"));					
		
		table.setRowFactory(
			    new Callback<TableView<CheckoutRecordTable>, TableRow<CheckoutRecordTable>>() {
			    	
			  @Override
			  public TableRow<CheckoutRecordTable> call(TableView<CheckoutRecordTable> tableView) {
			    final TableRow<CheckoutRecordTable> row = new TableRow<>();
			    final ContextMenu rowMenu = new ContextMenu();
			    MenuItem checkoutItem = new MenuItem("Return");
			    checkoutItem.setOnAction(new EventHandler<ActionEvent>() {
			      @Override
			      public void handle(ActionEvent event) {
			    	  Alert alert = new Alert(AlertType.CONFIRMATION);
			    	  alert.setContentText("Confirm return item.");
			    	  
			    	  Optional<ButtonType> result = alert.showAndWait();
			    	  if (result.isPresent() && result.get() == ButtonType.OK) {
			    		  int memberId = Integer.parseInt(tfSearchID.getText());
			    		  CheckoutRecordEntry checkoutRecordEntry = dao.getCheckoutRecordEntryById(memberId, 
			    				  row.getItem().getType(), row.getItem().getNumber(), row.getItem().getCopyNum());
			    		  
			    		  Publication pub = checkoutRecordEntry.getCopy().getPublication();
			    		  dao.removeCheckoutRecordEntry(memberId, pub, checkoutRecordEntry.getCopy());

			    		  pub.returnACopy();
			    		  if(pub.getClass().getName().contains("Book")){
			    			  dao.saveUpdateBook( (Book)pub );
			    		  } else {
			    			  dao.saveUpdatePeriodical( (Periodical)pub );
			    		  }
			    		  
			    		  row.setPrefHeight(0);
			    	  }
			      }
			    });
			    rowMenu.getItems().addAll(checkoutItem);

			    // only display context menu for non-null items:
			    row.contextMenuProperty().bind(
			      Bindings.when(Bindings.isNotNull(row.itemProperty()))
			      .then(rowMenu)
			      .otherwise((ContextMenu)null));
			    return row;
			  }
			});		
		
		ObservableList<CheckoutRecordTable> data = FXCollections.observableArrayList();
		data.addAll(list);
		table.setItems(data);
		table.getColumns().addAll(colTitle, colType, colISBN, colCopy,colBorrowedDate, colDueDate, colStatus);
		
		return table;
	}

	@FXML protected void onPrintBtnAction(ActionEvent event) {
		int memberId = Integer.parseInt(tfSearchID.getText());
		List<CheckoutRecordEntry> checkoutRecord = dao.getCheckoutRecordEntryByMemberID( memberId );
		
		if(checkoutRecord == null) return;
	
		ArrayList<CheckoutRecordTable> list = this.getTableList(checkoutRecord);
		
		char[] chars = new char[110];
		Arrays.fill(chars, '-');
		String hBar = new String(chars);			
		
		System.out.println();
		System.out.println("Member Checkout Record");
		System.out.println("ID #: " + memberId);
		System.out.println("Name: " + tfMemberName.getText());
		System.out.println(hBar);
		
		System.out.format("%-30s | %-10s | %-15s | %-10s | %-10s | %-10s | %-15s", 
							"Title", 
							"Type",
							"ISBN/Issue #", 
							"Copy #",
							"Borrowed",
							"Due",
							"Status");
		
		System.out.println();
		System.out.println(hBar);
		
		for(CheckoutRecordTable entry: list){
			System.out.println();
			System.out.format("%-30s | %-10s | %-15s | %-10s | %-10s | %-10s | %-15s", 
							entry.getTitle(), 
							entry.getType(), 
							entry.getNumber(),
							entry.getCopyNum(),
							entry.getBorrowedDate(),
							entry.getDueDate(),
							entry.getStatus());
		}
		
		System.out.println();
		System.out.println(hBar);
		
	    Alert alert = new Alert(AlertType.INFORMATION, "Done printing!", ButtonType.OK);
	    alert.setHeaderText(null);
	    alert.setTitle("Print Checkout Record");		
	    alert.show();
	}

}
