package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import business.dataaccess.DataAccess;
import business.dataaccess.DataAccessFacade;
import business.objects.Author;
import business.objects.Book;
import business.objects.Copy;
import business.objects.Periodical;

public class AddPublicationController {
	private final DataAccess dao = new DataAccessFacade();
	
	
	@FXML private TextField perTitle;
	@FXML private TextField perIssueNum;
	@FXML private TextField perMaxCODays;
	@FXML private TextField tfBookTitle;
	@FXML private TextField tfISBN;
	@FXML private TextField tfBookMaxCODays;
	@FXML private ListView<Author> authorList;
	
	@FXML protected void handleSavePerBtnAction(ActionEvent event) {
		Periodical per = dao.searchPeriodicalByIssueNo(perIssueNum.getText());
		if (per != null) {
			Alert alert = new Alert(AlertType.ERROR, "Periodical issue number already exist!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.setTitle("Add Periodical Error");
			alert.show();
			return;
		}
		Periodical periodical = new Periodical(perTitle.getText(), perIssueNum.getText(), Integer.parseInt(perMaxCODays.getText()));
		periodical.addCopy();		
		
		dao.savePeriodical(periodical);
		Alert alert = new Alert(AlertType.INFORMATION, perTitle.getText() + " is now saved!", ButtonType.OK);
		alert.setHeaderText(null);
		alert.setTitle("Periodicals Saved");
		alert.show();
	}

	@FXML protected void handleSaveBookBtnAction(ActionEvent event) {
		Book bk = dao.getBookByISBN(tfISBN.getText());
		if (bk != null) {
			Alert alert = new Alert(AlertType.ERROR, "Book ISBN number already exist!", ButtonType.OK);
			alert.setHeaderText(null);
			alert.setTitle("Add Book Error");
			alert.show();
			return;
		}
		Book book = new Book(tfISBN.getText(), Integer.parseInt(tfBookMaxCODays.getText()), tfBookTitle.getText());
		List<Author> alist = new ArrayList<>();
		for(Author a: authorList.getItems())		
		{
			alist.add(a);
		}
		//System.out.println(alist);
		book.setAuthorlist(alist);
		book.addCopy();	
		
		dao.saveBook(book);
		
		Alert alert = new Alert(AlertType.INFORMATION, tfBookTitle.getText() + " is now saved!", ButtonType.OK);
		alert.setHeaderText(null);
		alert.setTitle("Book Saved");
		alert.show();
	}
	
	@FXML protected void handlebtnEditAuthor(ActionEvent event) {
		//Call Author window and
		ObservableList<Author> data = FXCollections.observableArrayList();
		data.addAll(showAuthorDialog(authorList.getItems(), event));
	}
	
	public ObservableList<Author> showAuthorDialog(ObservableList<Author> list, ActionEvent event) {
		try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Author.fxml"));
    		Parent root = loader.load();
    	    Stage dialogStage = new Stage();
    	    dialogStage.setTitle("Select Author");
    	    dialogStage.initModality(Modality.WINDOW_MODAL);
    	    dialogStage.initOwner(((Node)event.getTarget()).getScene().getWindow());
    	    dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("../view/author.png")));
    	    
    	    Scene scene = new Scene(root);
    	    dialogStage.setScene(scene);

    	    // Set the person into the controller
    	    AuthorController controller = loader.getController();
    	    controller.setDialogStage(dialogStage);
    	    controller.setSelectedList(list);

    	    // Show the dialog and wait until the user closes it
    	    dialogStage.showAndWait();
    	    return controller.getSelectedList().getItems();

    	} catch (IOException io) {
    		System.out.println(io.getStackTrace());
    	}
		return null;
	}
}
