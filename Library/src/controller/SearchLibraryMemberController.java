package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import business.dataaccess.DataAccess;
import business.dataaccess.DataAccessFacade;
import business.objects.LibraryMember;
import business.objects.Address;

public class SearchLibraryMemberController {
	private final DataAccess dao = new DataAccessFacade();

	@FXML private TextField tfID;
	
	@FXML protected void handleSearchMemberBtnAction(ActionEvent event) {
		
		//dao.saveMember(member);
	}
}
