package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import view.AddLibraryMember;
import view.AddPublication;
import view.SearchLibraryMember;
import view.SearchPublication;

public class MainController {
	private final AddLibraryMember addLibraryMember = new AddLibraryMember();
	private final AddPublication addPublication = new AddPublication();
	private final SearchLibraryMember searchLibraryMember = new SearchLibraryMember();
	private final SearchPublication searchPublication = new SearchPublication();
	
	@FXML private VBox vBoxWorkArea;
	
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
}