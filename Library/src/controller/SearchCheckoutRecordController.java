package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

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
	private final DataAccess dao = new DataAccessFacade();
	@FXML private TextField tfSearchID;
	@FXML private TextField tfMemberName;
	@FXML private Label lblSearchStatus;
	@FXML private HBox hbSearchResult;
	private Parent root;
	
    @FXML // This method is called by the FXMLLoader when initialization is complete
   void initialize() {

   }
	
   @FXML protected void onSearchBtnAction(ActionEvent event) {
	   
	   if( tfSearchID.getText().isEmpty() ){
		   return;
	   }
	   
	   int memberId = -1;
	   try {
		   memberId = Integer.parseInt(tfSearchID.getText());
	   } catch (NumberFormatException nfe) {
		   Alert alert = new Alert(AlertType.ERROR, "Member ID should be numberic", ButtonType.OK);
		   alert.setHeaderText(null);
		   alert.setTitle("Checkout Record");
		   alert.show();
		   return;
	   }
	   
	   LibraryMember member = dao.searchLibraryMemberByID(memberId);
	   
	   if( member == null ){
			Alert alert = new Alert(AlertType.ERROR, "Member ID does not exist", ButtonType.OK);
			alert.setHeaderText(null);
			alert.setTitle("Checkout Record");
			alert.show();
			
			tfMemberName.clear();
			hbSearchResult.getChildren().clear();
			lblSearchStatus.setText("");
			return;
	   }
	   
	   tfMemberName.setText(member.getFirstName() + " " + member.getLastName());
	   
	   List<CheckoutRecordEntry> checkoutRecord = dao.getCheckoutRecordEntryByMemberID( memberId );
	   
	   if(checkoutRecord == null){
			Alert alert = new Alert(AlertType.ERROR, "Member has no checkout record", ButtonType.OK);
			alert.setHeaderText(null);
			alert.setTitle("Checkout Record");
			alert.show();		   
			return;
	   }
	   
	   ArrayList<CheckoutRecordTable> list = new ArrayList<CheckoutRecordTable>();
	 
	   for(CheckoutRecordEntry ce: checkoutRecord){
		   CheckoutRecordTable chkRec = new CheckoutRecordTable();
		   chkRec.setTitle(ce.getCopy().getPublication().getTitle());
		   chkRec.setNumber(ce.getCopy().getPublication().getNumber());
		   chkRec.setBorrowedDate(ce.getCheckoutDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
		   chkRec.setDueDate(ce.getDueDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
		   
		   if(ce.getDueDate().isBefore(LocalDate.now())){
			    Period betweenDates = Period.between(ce.getCheckoutDate(), ce.getDueDate());
			    chkRec.setStatus(betweenDates.getDays() + " day(s) overdue");
		   } else {
			   chkRec.setStatus("");
		   }
		   
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
		
		TableColumn colISBN = new TableColumn("ISBN/Issue #");
		colISBN.setPrefWidth(100);
		colISBN.setCellValueFactory(
                new PropertyValueFactory<CheckoutRecordTable, String>("number"));
		
		TableColumn colBorrowedDate = new TableColumn("Borrowed");
		colBorrowedDate.setPrefWidth(70);
		colBorrowedDate.setCellValueFactory(
                new PropertyValueFactory<CheckoutRecordTable, String>("borrowedDate"));
		
		TableColumn colDueDate = new TableColumn("Due");
		colDueDate.setPrefWidth(70);
		colDueDate.setCellValueFactory(
                new PropertyValueFactory<CheckoutRecordTable, String>("dueDate"));	
		
		TableColumn colStatus = new TableColumn("Status");
		colStatus.setPrefWidth(125);
		colStatus.setCellValueFactory(
                new PropertyValueFactory<CheckoutRecordTable, String>("status"));			

		table.setRowFactory(
			    new Callback<TableView<Book>, TableRow<Book>>() {
			    	
			  @Override
			  public TableRow<Book> call(TableView<Book> tableView) {
			    final TableRow<Book> row = new TableRow<>();
			    final ContextMenu rowMenu = new ContextMenu();
			    MenuItem checkoutItem = new MenuItem("Return");
			    checkoutItem.setOnAction(new EventHandler<ActionEvent>() {
			      @Override
			      public void handle(ActionEvent event) {
			    	showReturnDialog(row.getItem(), event);
			        //table.getItems().remove(row.getItem());
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
		table.getColumns().addAll(colTitle,colISBN, colBorrowedDate, colDueDate, colStatus);
		
		return table;
	}

	public void showReturnDialog(Publication pub, ActionEvent event) {
		try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/CheckoutDialog.fxml"));
    		Parent root = loader.load();
    	    Stage dialogStage = new Stage();
    	    dialogStage.setTitle("Checkout Publication");
    	    dialogStage.initModality(Modality.WINDOW_MODAL);
    	    dialogStage.setResizable(false);
    	    dialogStage.initOwner(this.root.getScene().getWindow());
    	    Scene scene = new Scene(root);
    	    dialogStage.setScene(scene);

    	    // Set the Publication into the controller
    	    CheckoutDialogController controller = loader.getController();
    	    controller.setDialogStage(dialogStage);
    	    if (pub instanceof Book) {
    	    	controller.setPublicationType("Book");
    	    	controller.getTfTitle().setText(pub.getTitle());
    	    	controller.getTfNumber().setText(((Book) pub).getISBN());
    	    	controller.getTfMaxCheckout().setText(((Book) pub).getMaxcheckoutlength()+"");
    	    } else {
    	    	controller.setPublicationType("Periodical");
    	    	controller.getTfTitle().setText(pub.getTitle());
    	    	controller.getTfNumber().setText(((Periodical) pub).getIssueNo());
    	    	controller.getTfMaxCheckout().setText(((Periodical) pub).getMaxcheckoutlength()+"");
    	    }
    	    // Show the dialog and wait until the user closes it
    	    dialogStage.showAndWait();
    	} catch (IOException io) {
    		System.out.println(io.getStackTrace());
    	}
	}	
}
