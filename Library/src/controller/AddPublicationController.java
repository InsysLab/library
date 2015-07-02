package controller;

import java.io.IOException;

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
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.AddAuthor;
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
		Periodical periodical = new Periodical(perTitle.getText(), perIssueNum.getText(), Integer.parseInt(perMaxCODays.getText()));
		Copy aCopy = new Copy("1", periodical);
		aCopy.setAvailable(true);
		periodical.addCopy(aCopy);		
		
		dao.savePeriodical(periodical);
		Alert alert = new Alert(AlertType.INFORMATION, perTitle.getText() + " is now saved!", ButtonType.OK);
		alert.setTitle("Periodicals Saved");
		alert.show();
	}

	@FXML protected void handleSaveBookBtnAction(ActionEvent event) {
		Book book = new Book(tfISBN.getText(), Integer.parseInt(tfBookMaxCODays.getText()), tfBookTitle.getText());
		Copy aCopy = new Copy("1", book);
		aCopy.setAvailable(true);
		book.addCopy(aCopy);	
		
		dao.saveBook(book);
		
		Alert alert = new Alert(AlertType.INFORMATION, tfBookTitle.getText() + " is now saved!", ButtonType.OK);
		alert.setTitle("Book Saved");
		alert.show();
	}
	
	@FXML protected void handlebtnEditAuthor(ActionEvent event) {
		//Call Author window and
		
		final AddAuthor addAuthor = new AddAuthor(this);		
		
	}
	
	public void setAuthorList(ObservableList<Author> value)
	{
		System.out.println("print List: " + value);
		authorList.setItems(value);
	}
	
	@FXML protected void handleAddAuthPerBtnAction(ActionEvent event) {
		ObservableList<String> data = FXCollections.observableArrayList();
		data.add(showAuthorDialog("Test", event));
		//lvAuthors.setItems(data);
	}
	
	public String showAuthorDialog(String input, ActionEvent event) {
		try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("AddAuthorTest.fxml"));
    		Parent root = loader.load();
    	    Stage dialogStage = new Stage();
    	    dialogStage.setTitle("Select Author");
    	    dialogStage.initModality(Modality.WINDOW_MODAL);
    	    dialogStage.initOwner(((Node)event.getTarget()).getScene().getWindow());
    	    Scene scene = new Scene(root);
    	    dialogStage.setScene(scene);

    	    // Set the person into the controller
    	    AddAuthorController controller = loader.getController();
    	    controller.setDialogStage(dialogStage);
    	    controller.getTfName().setText("Testing");

    	    // Show the dialog and wait until the user closes it
    	    dialogStage.showAndWait();
    	    return controller.getTfName().getText();

    	} catch (IOException io) {
    		System.out.println(io.getStackTrace());
    	}
		return null;
	}
}
