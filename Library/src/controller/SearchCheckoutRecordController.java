package controller;

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
import business.dataaccess.DataAccess;
import business.dataaccess.DataAccessFacade;
import business.objects.Book;
import business.objects.CheckoutRecord;
import business.objects.CheckoutRecordEntry;

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
	   //CheckoutRecord cr = new CheckoutRecord();
	   //CheckoutRecordEntry en1 = new CheckoutRecordEntry();
	}
	
	
	private TableView getBookTable(ArrayList<Book> books) {
		TableView table = new TableView();
		table.setPrefWidth(500);
		TableColumn colTitle = new TableColumn("Title");
		colTitle.setPrefWidth(125);
		colTitle.setCellValueFactory(
                new PropertyValueFactory<Book, String>("title"));
		TableColumn colISBN = new TableColumn("ISBN");
		colISBN.setPrefWidth(125);
		colISBN.setCellValueFactory(
                new PropertyValueFactory<Book, String>("ISBN"));
		TableColumn colMax = new TableColumn("Max Checkout");
		colMax.setPrefWidth(125);
		colMax.setCellValueFactory(
                new PropertyValueFactory<Book, String>("maxcheckoutlength"));

		ObservableList<Book> data = FXCollections.observableArrayList();
		data.addAll(books);
		table.setItems(data);
		table.getColumns().addAll(colTitle,colISBN, colMax);
		
		return table;
	}
	
}
