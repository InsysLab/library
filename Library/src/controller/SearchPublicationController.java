package controller;

import java.io.IOException;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import table.objects.SearchPublicationTable;
import business.dataaccess.DataAccess;
import business.dataaccess.DataAccessFacade;
import business.objects.Book;
import business.objects.Copy;
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
		ArrayList<SearchPublicationTable> tableList = new ArrayList<SearchPublicationTable>();

		for(Book ce: books){
			SearchPublicationTable spt = new SearchPublicationTable();
			spt.setTitle(ce.getTitle());
			spt.setNumber(ce.getISBN());
			spt.setMaxDays(ce.getMaxcheckoutlength() +"");
			spt.setIntCopies(ce.getCopyList().size() + "");
			int count = 0;
			for (Copy copy: ce.getCopyList()) {
				count += copy.isAvailable() ? 1 : 0;
			}
			spt.setAvailableCopies(count + "");
			tableList.add(spt);
		}

		return getSearchTable(tableList);
	}
	
	private TableView getPeriodicalTable(ArrayList<Periodical> periodicals) {
		ArrayList<SearchPublicationTable> tableList = new ArrayList<SearchPublicationTable>();
		 
		for(Periodical ce: periodicals){
			SearchPublicationTable spt = new SearchPublicationTable();
			spt.setTitle(ce.getTitle());
			spt.setNumber(ce.getIssueNo());
			spt.setMaxDays(ce.getMaxcheckoutlength() +"");
			spt.setIntCopies(ce.getCopyList().size() + "");
			int count = 0;
			for (Copy copy: ce.getCopyList()) {
				count += copy.isAvailable() ? 1 : 0;
			}
			spt.setAvailableCopies(count + "");
			tableList.add(spt);
		}
		
		return getSearchTable(tableList);
	}
	
	private TableView getSearchTable(ArrayList<SearchPublicationTable> list) {		
		TableView table = new TableView();
		table.setPrefWidth(600);
		TableColumn colTitle = new TableColumn("Title");
		colTitle.setPrefWidth(150);
		colTitle.setCellValueFactory(
                new PropertyValueFactory<SearchPublicationTable, String>("title"));
		TableColumn colIssueNo = new TableColumn(isBook ? "ISBN" : "Issue No");
		colIssueNo.setPrefWidth(90);
		colIssueNo.setCellValueFactory(
                new PropertyValueFactory<SearchPublicationTable, String>("number"));
		TableColumn colMax = new TableColumn("Max Checkout");
		colMax.setPrefWidth(90);
		colMax.setCellValueFactory(
                new PropertyValueFactory<SearchPublicationTable, String>("maxDays"));
		TableColumn numCopies = new TableColumn("Copy");
		numCopies.setPrefWidth(90);
		numCopies.setCellValueFactory(
                new PropertyValueFactory<SearchPublicationTable, String>("intCopies"));

		TableColumn avlCopies = new TableColumn("Available Copy");
		avlCopies.setPrefWidth(90);
		avlCopies.setCellValueFactory(
                new PropertyValueFactory<SearchPublicationTable, String>("availableCopies"));
		
		table.setRowFactory(
			    new Callback<TableView<SearchPublicationTable>, TableRow<SearchPublicationTable>>() {
			  @Override
			  public TableRow<SearchPublicationTable> call(TableView<SearchPublicationTable> tableView) {
			    final TableRow<SearchPublicationTable> row = new TableRow<>();
			    final ContextMenu rowMenu = new ContextMenu();
			    MenuItem checkoutItem = new MenuItem("Checkout");
			    checkoutItem.setOnAction(new EventHandler<ActionEvent>() {
			      @Override
			      public void handle(ActionEvent event) {
			    	if (showCheckoutDialog(isBook?dao.getBookByISBN(row.getItem().getNumber()):dao.searchPeriodicalByIssueNo(row.getItem().getNumber()), event)) {
			    		row.getItem().setAvailableCopies((Integer.parseInt(row.getItem().getAvailableCopies()) - 1) + "");
			    		tableView.getColumns().get(4).setVisible(false);
			    		tableView.getColumns().get(4).setVisible(true);
			    	}
			        //table.getItems().remove(row.getItem());
			      }
			    });
			    MenuItem addCopy = new MenuItem("Add Copy");
			    addCopy.setOnAction(new EventHandler<ActionEvent>() {
			      @Override
			      public void handle(ActionEvent event) {
			    	  int copy = showAddCopy(row.getItem().getTitle(), row.getItem().getNumber());
			    	  if (copy > 0) {
			    		  row.getItem().setIntCopies((Integer.parseInt(row.getItem().getIntCopies()) + copy) + "");
			    		  tableView.getColumns().get(3).setVisible(false);
			    		  tableView.getColumns().get(3).setVisible(true);
			    	  }
			        //table.getItems().remove(row.getItem());
			      }
			    });
			    rowMenu.getItems().addAll(checkoutItem, addCopy);

			    // only display context menu for non-null items:
			    row.contextMenuProperty().bind(
			      Bindings.when(Bindings.isNotNull(row.itemProperty()))
			      .then(rowMenu)
			      .otherwise((ContextMenu)null));
			    return row;
			  }
			});

		ObservableList<SearchPublicationTable> data = FXCollections.observableArrayList();
		data.addAll(list);
		table.setItems(data);
		table.getColumns().addAll(colTitle,colIssueNo, colMax, numCopies, avlCopies);
		
		return table;
	}
	
	public boolean showCheckoutDialog(Publication pub, ActionEvent event) {
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
    	    return controller.isCheckout();
    	} catch (IOException io) {
    		System.out.println(io.getStackTrace());
    	}
		return false;
	}
	
	public int showAddCopy(String title, String number) {
		int copy = 0;
		TextInputDialog dialog = new TextInputDialog("1");
		dialog.setTitle("Add copy");
		dialog.setHeaderText("Add Copy to " + title);
		dialog.setContentText("Enter number of copies:");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
		    try {
		    	copy = Integer.parseInt(result.get());
		    	if (copy < 1) {
					Alert alert = new Alert(AlertType.ERROR, "Number of copy should be greater than zero!", ButtonType.OK);
					alert.setHeaderText(null);
					alert.setTitle("Add Copy");
					alert.show();
		    	} else {
		    		if (isBook) {
		    			Book book = dao.getBookByISBN(number);
		    			for (int i = 0; i < copy; i++)
		    				book.addCopy();
		    			//SaveUpdate Book
		    			dao.saveUpdateBook(book);
		    		} else {
		    			Periodical per = dao.searchPeriodicalByIssueNo(number);
		    			for (int i = 0; i < copy; i++)
		    				per.addCopy();
		    			//SaveUpdate Periodical
		    		}
		    	}
		    	
		    } catch (NumberFormatException nfe) {
				Alert alert = new Alert(AlertType.ERROR, "Number of copy should be numberic!", ButtonType.OK);
				alert.setHeaderText(null);
				alert.setTitle("Add Copy");
				alert.show();
		    }
		}
		return copy;
	}
}
