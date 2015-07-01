package controller;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import business.dataaccess.DataAccess;
import business.dataaccess.DataAccessFacade;
import business.objects.Book;
import business.objects.BookList;

public class SearchPublicationController {
	private final DataAccess dao = new DataAccessFacade();
	@FXML private Label lblSearchStatus;
	@FXML private HBox hbSearchResult;
	
	@FXML protected void onSearchBtnAction(ActionEvent event) {
		BookList list = dao.getBookList();

		if (hbSearchResult.getChildren().size() == 1) {
			hbSearchResult.getChildren().remove(0);
		}
		if (list.getBooks().size() > 0) {
			hbSearchResult.getChildren().add(getBookTable((ArrayList<Book>)list.getBooks()));
		} else {
			lblSearchStatus.setText("Search did not find anything...");
		}
	}
	
	private TableView getBookTable(ArrayList<Book> books) {
		TableView table = new TableView();
		TableColumn colTitle = new TableColumn("Title");
		colTitle.setCellValueFactory(
                new PropertyValueFactory<Book, String>("title"));
		TableColumn colISBN = new TableColumn("ISBN");
		colISBN.setCellValueFactory(
                new PropertyValueFactory<Book, String>("ISBN"));
		TableColumn colMax = new TableColumn("Max Checkout");
		colMax.setCellValueFactory(
                new PropertyValueFactory<Book, String>("maxcheckoutlength"));

		ObservableList<Book> data = FXCollections.observableArrayList();
		data.addAll(books);
		table.setItems(data);
		table.getColumns().addAll(colTitle,colISBN, colMax);
		
		return table;
	}
}
