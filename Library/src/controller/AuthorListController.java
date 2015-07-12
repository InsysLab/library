package controller;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import table.objects.SearchPublicationTable;
import business.objects.Author;

public class AuthorListController {
	private List<Author> authors;	
	@FXML private HBox tableSpace;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
    	
    }
    
    public void loadTable(List<Author> authors) {
    	this.authors = authors;
		TableView authorTable = new TableView();
    	//authorTable.setPrefWidth(600);
		TableColumn colFname = new TableColumn("First Name");
		colFname.setPrefWidth(150);
		colFname.setCellValueFactory(
                new PropertyValueFactory<SearchPublicationTable, String>("firstName"));
		TableColumn colLname = new TableColumn("Last Name");
		colLname.setPrefWidth(150);
		colLname.setCellValueFactory(
                new PropertyValueFactory<SearchPublicationTable, String>("lastName"));
		TableColumn colPhone = new TableColumn("Phone");
		colPhone.setPrefWidth(90);
		colPhone.setCellValueFactory(
                new PropertyValueFactory<SearchPublicationTable, String>("phone"));
		ObservableList<Author> data = FXCollections.observableArrayList();
		data.addAll(authors);
		authorTable.setItems(data);
		authorTable.getColumns().addAll(colFname,colLname, colPhone);
		authorTable.setVisible(true);
		tableSpace.getChildren().add(authorTable);
    }
}
