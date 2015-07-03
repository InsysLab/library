package controller;

import java.io.IOException;
import java.util.ArrayList;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import business.dataaccess.DataAccess;
import business.dataaccess.DataAccessFacade;
import business.objects.Book;
import business.objects.Periodical;
import business.objects.Publication;

public class SearchPublicationController {
	private final DataAccess dao = new DataAccessFacade();
	@FXML private ComboBox cbPublication;
	@FXML private ComboBox cbTitle;
	@FXML private Label lblSearchStatus;
	@FXML private HBox hbSearchResult;
	@FXML private TextField tfSearchText;
	private boolean isBook;
	private Parent root;
	
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
    	isBook = true;
    	root = cbPublication.getParent();
   }
	
   @FXML protected void onSearchBtnAction(ActionEvent event) {
	   if (cbPublication.getValue().equals("Book")) {
		   isBook = true;
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
			   hbSearchResult.getChildren().add(getBookTable(list, event));
		   } else {
			   lblSearchStatus.setText("Search did not find anything...");
		   }
	   } else {
		   isBook = false;
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
	
	private TableView getBookTable(ArrayList<Book> books, ActionEvent event) {
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
		TableColumn colMax = new TableColumn("Max Checkout Days");
		colMax.setPrefWidth(125);
		colMax.setCellValueFactory(
                new PropertyValueFactory<Book, String>("maxcheckoutlength"));
		
		table.setRowFactory(
			    new Callback<TableView<Book>, TableRow<Book>>() {
			  @Override
			  public TableRow<Book> call(TableView<Book> tableView) {
			    final TableRow<Book> row = new TableRow<>();
			    final ContextMenu rowMenu = new ContextMenu();
			    MenuItem checkoutItem = new MenuItem("Checkout");
			    checkoutItem.setOnAction(new EventHandler<ActionEvent>() {
			      @Override
			      public void handle(ActionEvent event) {
			    	showCheckoutDialog(row.getItem(), event);
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
		
		table.setRowFactory(
			    new Callback<TableView<Periodical>, TableRow<Periodical>>() {
			  @Override
			  public TableRow<Periodical> call(TableView<Periodical> tableView) {
			    final TableRow<Periodical> row = new TableRow<>();
			    final ContextMenu rowMenu = new ContextMenu();
			    MenuItem checkoutItem = new MenuItem("Checkout");
			    checkoutItem.setOnAction(new EventHandler<ActionEvent>() {
			      @Override
			      public void handle(ActionEvent event) {
			    	showCheckoutDialog(row.getItem(), event);
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

		ObservableList<Periodical> data = FXCollections.observableArrayList();
		data.addAll(periodicals);
		table.setItems(data);
		table.getColumns().addAll(colTitle,colIssueNo, colMax);
		
		return table;
	}
	
	public void showCheckoutDialog(Publication pub, ActionEvent event) {
		try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/CheckoutDialog.fxml"));
    		Parent root = loader.load();
    	    Stage dialogStage = new Stage();
    	    dialogStage.setTitle("Checkout Publication");
    	    dialogStage.initModality(Modality.WINDOW_MODAL);
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
