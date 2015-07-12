package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.AddLibraryMember;
import view.AddPublication;
import view.MemberCheckout;
import view.SearchCheckoutRecord;
import view.SearchLibraryMember;
import view.SearchPublication;

public class MainController {
	private final AddLibraryMember addLibraryMember = new AddLibraryMember();
	private final AddPublication addPublication = new AddPublication();
	private final SearchLibraryMember searchLibraryMember = new SearchLibraryMember();
	private final SearchPublication searchPublication = new SearchPublication();
	private final MemberCheckout memberCheckout = new MemberCheckout();
	private final SearchCheckoutRecord searchCheckoutRecord = new SearchCheckoutRecord();
	private Parent root;
	
	@FXML private VBox vBoxWorkArea;
	
	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		root = vBoxWorkArea.getParent();
	}	
	
	@FXML protected void handleSearchLibraryMemberBtnAction(ActionEvent event) {
		if (vBoxWorkArea.getChildren().size() == 1) {
			vBoxWorkArea.getChildren().remove(0);
    	}
		vBoxWorkArea.getChildren().add(searchLibraryMember);
	}
	
	@FXML protected void handleSearchPublicationBtnAction(ActionEvent event) {
		if (vBoxWorkArea.getChildren().size() == 1) {
			vBoxWorkArea.getChildren().remove(0);
    	}
		vBoxWorkArea.getChildren().add(searchPublication);
	}
	
	@FXML protected void handleSearchCheckoutBtnAction(ActionEvent event) {
		if (vBoxWorkArea.getChildren().size() == 1) {
			vBoxWorkArea.getChildren().remove(0);
    	}
		vBoxWorkArea.getChildren().add(searchCheckoutRecord);
	}

	@FXML protected void handleAddPublicationBtnAction(ActionEvent event) {
		if (vBoxWorkArea.getChildren().size() == 1) {
			vBoxWorkArea.getChildren().remove(0);
    	}
		vBoxWorkArea.getChildren().add(addPublication);
	}
	
	@FXML protected void handleAddMemberBtnAction(ActionEvent event) {
		if (vBoxWorkArea.getChildren().size() == 1) {
			vBoxWorkArea.getChildren().remove(0);
    	}
		vBoxWorkArea.getChildren().add(addLibraryMember);
	}
	
	@FXML protected void handleCheckoutBtnAction(ActionEvent event) {
		if (vBoxWorkArea.getChildren().size() == 1) {
			vBoxWorkArea.getChildren().remove(0);
    	}
		vBoxWorkArea.getChildren().add(memberCheckout);
	}	
	
	@FXML protected void handleAboutBtnAction(ActionEvent event) {
		try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/About.fxml"));
    		Parent root = loader.load();
    	    Stage dialogStage = new Stage();
    	    dialogStage.setTitle("About MPP Library");
    	    dialogStage.initModality(Modality.WINDOW_MODAL);
    	    dialogStage.setResizable(false);
    	    dialogStage.initOwner(this.root.getScene().getWindow());
    	    Scene scene = new Scene(root);
    	    dialogStage.setScene(scene);
    	    dialogStage.showAndWait();
    	} catch (IOException io) {
    		System.out.println(io.getStackTrace());
    	}
	}
}
