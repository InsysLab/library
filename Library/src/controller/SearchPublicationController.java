package controller;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import business.dataaccess.DataAccess;
import business.dataaccess.DataAccessFacade;
import business.objects.Book;
import business.objects.Periodical;

public class SearchPublicationController {
	private final DataAccess dao = new DataAccessFacade();
	@FXML private ComboBox cbPublication;
	@FXML private ComboBox cbTitle;
	@FXML private Label lblSearchStatus;
	@FXML private HBox hbSearchResult;
	@FXML private TextField tfSearchText;
	
    @FXML // This method is called by the FXMLLoader when initialization is complete
   void initialize() {
    	cbPublication.getItems().clear();
    	cbPublication.getItems().addAll("Book","Periodicals");
    	cbPublication.setValue("Book");
    	cbPublication.setPrefWidth(100);
    	cbTitle.getItems().clear();
    	cbTitle.getItems().addAll("Title","ISBN");
    	cbTitle.setValue("Title");
    	cbTitle.setPrefWidth(100);
   }
	
   @FXML protected void onSearchBtnAction(ActionEvent event) {
	   if (cbPublication.getValue().equals("Book")) {
		    ArrayList<Book> list = null;
			if (cbTitle.getValue().equals("Title")) {
				list = dao.wildSearchBookByTitle(tfSearchText.getText());
			} else if (cbTitle.getValue().equals("ISBN")) {
				list = dao.wildSearchBookByISBN(tfSearchText.getText());
			}

			if (hbSearchResult.getChildren().size() == 1) {
				hbSearchResult.getChildren().remove(0);
			}
			if (list!=null && list.size() > 0) {
				lblSearchStatus.setText("Search result...");
				hbSearchResult.getChildren().add(getBookTable(list));
			} else {
				lblSearchStatus.setText("Search did not find anything...");
			}
	   } else {
		    ArrayList<Periodical> list = null;
			if (cbTitle.getValue().equals("Title")) {
				list = dao.wildSearchPeriodicalByTitle(tfSearchText.getText());
			} else if (cbTitle.getValue().equals("Issue No")) {
				list = dao.wildSearchPeriodicalByIssueNo(tfSearchText.getText());
			}

			if (hbSearchResult.getChildren().size() == 1) {
				hbSearchResult.getChildren().remove(0);
			}
			if (list!=null && list.size() > 0) {
				lblSearchStatus.setText("Search result...");
				hbSearchResult.getChildren().add(getPeriodicalTable(list));
			} else {
				lblSearchStatus.setText("Search did not find anything...");
			}
	   }
	}
	@FXML protected void onSearchComboPubAction(ActionEvent event) {
		if (cbPublication.getValue().equals("Book")) {
	    	cbTitle.getItems().clear();
	    	cbTitle.getItems().addAll("Title","ISBN");
	    	cbTitle.setValue("Title");
		} else {
	    	cbTitle.getItems().clear();
	    	cbTitle.getItems().addAll("Title","Issue No");
	    	cbTitle.setValue("Title");
		}
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
	
	private TableView getPeriodicalTable(ArrayList<Periodical> periodicals) {
		TableView table = new TableView();
		table.setPrefWidth(500);
		TableColumn colTitle = new TableColumn("Title");
		colTitle.setPrefWidth(125);
		colTitle.setCellValueFactory(
                new PropertyValueFactory<Periodical, String>("title"));
		TableColumn colIssueNo = new TableColumn("Issue No");
		colIssueNo.setPrefWidth(125);
		colIssueNo.setCellValueFactory(
                new PropertyValueFactory<Periodical, String>("issueNo"));
		TableColumn colMax = new TableColumn("Max Checkout");
		colMax.setPrefWidth(125);
		colMax.setCellValueFactory(
                new PropertyValueFactory<Periodical, String>("maxcheckoutlength"));

		ObservableList<Periodical> data = FXCollections.observableArrayList();
		data.addAll(periodicals);
		table.setItems(data);
		table.getColumns().addAll(colTitle,colIssueNo, colMax);
		
		return table;
	}
}
