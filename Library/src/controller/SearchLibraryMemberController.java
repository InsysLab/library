package controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import table.objects.CheckoutRecordTable;
import validator.AddressValidator;
import validator.MemberValidator;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import business.dataaccess.DataAccess;
import business.dataaccess.DataAccessFacade;
import business.objects.Book;
import business.objects.CheckoutRecordEntry;
import business.objects.LibraryMember;
import business.objects.Address;
import business.objects.Periodical;
import business.objects.Publication;

public class SearchLibraryMemberController {
	private final DataAccess dao = DataAccessFacade.getDAO();
	private int memberId;
	
	@FXML private TextField tfMemberID;
	@FXML private TextField tfFirstName;
	@FXML private TextField tfLastName;
	@FXML private TextField tfStreet;
	@FXML private TextField tfCity;
	@FXML private TextField tfState;
	@FXML private TextField tfZip;
	@FXML private TextField tfPhone;
	@FXML private Button btnUpdate;
	@FXML private HBox hbSearchResult;
	
	@FXML protected void handleSearchMemberBtnAction(ActionEvent event) {
		String emessage = MemberValidator.id(tfMemberID.getText());
		if (!emessage.equals("")) {
			Alert alert = new Alert(AlertType.ERROR, emessage, ButtonType.OK);
			alert.setHeaderText(null);
			alert.setTitle("Checkout Record");
			alert.show();
			return;
		}

		LibraryMember member = dao.searchLibraryMemberByID(Integer.parseInt(tfMemberID.getText()));
		
		if (member != null) {
			this.memberId = member.getMemberID();
					
			tfMemberID.setText(""+ this.memberId);
			tfFirstName.setText(member.getFirstName());
			tfLastName.setText(member.getLastName());
			tfPhone.setText(member.getPhone());
			
			Address address = member.getAddress();
			
			if (address!=null) {
				tfStreet.setText(address.getStreet());
				tfCity.setText(address.getCity());
				tfState.setText(address.getState());
				tfZip.setText(address.getZip());
			}
			
			btnUpdate.setDisable(false);
			processShowCheckoutRecord();
		} else {
			this.memberId = 0;
			
			Alert alert = new Alert(AlertType.ERROR, "Cannot find member id " + tfMemberID.getText(), ButtonType.OK);
			alert.setHeaderText(null);
			alert.setTitle("Search Library Member");
			alert.show();

			tfFirstName.clear();
			tfLastName.clear();
			tfPhone.clear();
			tfStreet.clear();
			tfCity.clear();
			tfState.clear();
			tfZip.clear();
			
			btnUpdate.setDisable(true);
		}
	}
	
	@FXML protected void handleUpdateMemberBtnAction(ActionEvent event) {
		if (!isInputValid()) {
			return;
		}
		int id = Integer.parseInt(tfMemberID.getText());
		
		if( this.memberId != id ){
			Alert alert = new Alert(AlertType.ERROR, "Member ID is not editable", ButtonType.OK);
			alert.setHeaderText(null);
			alert.setTitle("Member Update");
			alert.show();	
			return;
		}		
		
		LibraryMember mem = dao.searchLibraryMemberByID(id);
		
		if (mem != null) {
			Address address = mem.getAddress();
			address.setStreet(tfStreet.getText());
			address.setCity(tfCity.getText());
			address.setState(tfState.getText());
			address.setZip(tfZip.getText());
			LibraryMember member = new LibraryMember(id, tfFirstName.getText(), tfLastName.getText(), tfPhone.getText(), address);
			dao.saveUpdateMember(member);
			
			Alert alert = new Alert(AlertType.INFORMATION, "Member details has been updated!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.setTitle("Update Library Member");
			alert.show();
		} 
	}
	
	private boolean isInputValid() {
        String errorMessage = "";
        if (tfFirstName.getText().trim().isEmpty() || tfLastName.getText().trim().isEmpty() || tfStreet.getText().trim().isEmpty() ||
        		tfCity.getText().trim().isEmpty() || tfState.getText().trim().isEmpty()  || tfZip.getText().trim().isEmpty() || tfPhone.getText().trim().isEmpty()) {
			errorMessage += "All fields must be nonempty!\n";
		}
        errorMessage+=MemberValidator.validateMember(tfMemberID.getText(), tfFirstName.getText(), tfLastName.getText(), tfPhone.getText());
        errorMessage+=AddressValidator.validateAddress(tfStreet.getText(), tfCity.getText(), tfState.getText(), tfZip.getText());        

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            //alert.initOwner(root.getScene().getWindow());
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
	}
	
	private void processShowCheckoutRecord() {
		   List<CheckoutRecordEntry> checkoutRecord = dao.getCheckoutRecordEntryByMemberID( memberId );
		   ArrayList<CheckoutRecordTable> list = new ArrayList<CheckoutRecordTable>();
		   if (checkoutRecord != null) {		 
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
					   chkRec.setStatus(dayDiff + " day(s) overdue");
				   } else {
					   chkRec.setStatus("");
				   }
				   if (ce.getCopy().getPublication() instanceof Book) {
					   chkRec.setType("book");
				   } else {
					   chkRec.setType("periodical");
				   }
				   list.add(chkRec);
			   }
		   }
		   if (hbSearchResult.getChildren().size() == 1) {
				hbSearchResult.getChildren().remove(0);
		   }
			hbSearchResult.getChildren().add(getCheckoutTable(list));
	}
	
	private TableView getCheckoutTable(ArrayList<CheckoutRecordTable> list) {
		TableView table = new TableView();
		table.setPrefWidth(600);

		TableColumn colTitle = new TableColumn("Title");
		colTitle.setPrefWidth(125);
		colTitle.setCellValueFactory(
                new PropertyValueFactory<CheckoutRecordTable, String>("title"));
		
		TableColumn colISBN = new TableColumn("ISBN/Issue #");
		colISBN.setPrefWidth(80);
		colISBN.setCellValueFactory(
                new PropertyValueFactory<CheckoutRecordTable, String>("number"));
		
		TableColumn colCopy = new TableColumn("Copy Number");
		colCopy.setPrefWidth(55);
		colCopy.setCellValueFactory(
                new PropertyValueFactory<CheckoutRecordTable, String>("copyNum"));

		TableColumn colType = new TableColumn("Type");
		colType.setPrefWidth(60);
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
			    		  int memberId = Integer.parseInt(tfMemberID.getText());
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
}

