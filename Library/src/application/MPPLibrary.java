package application;

import java.util.HashMap;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import view.AddLibraryMember;
import view.AddPublication;
import view.SearchLibraryMember;
import view.SearchPublication;

public class MPPLibrary extends Application {
    //Holds the screens to be displayed
    private HashMap<String, Node> screens = new HashMap<>();
    
	@Override
	public void start(Stage primaryStage) {
		try {
			//Arrange nodes vertically
			VBox root = new VBox();
			root.setPrefHeight(600);
			root.setPrefWidth(800);
			//Top section contains the heading
			root.getChildren().add(getTop());
			//Middle section contains the menus
			root.getChildren().add(getMiddle());
			
			Scene scene = new Scene(root,800,600);			
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("MPP Library System");
			primaryStage.setResizable(true);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private Node getTop() {
		HBox top = new HBox();
		top.setAlignment(Pos.CENTER);
		top.prefHeight(50);
		Text heading = new Text("MPP Library");
		heading.setFont(new Font("System Bold", 24));
		top.getChildren().add(heading);
		return top;
	}
	
	private Node getMiddle() {
		HBox hLibMenu = new HBox();
		hLibMenu.setPrefHeight(500);

		SplitPane spMenuContent = new SplitPane();
		spMenuContent.setPrefWidth(200);
		spMenuContent.setPrefHeight(500);
		
		hLibMenu.getChildren().add(spMenuContent);
		
		VBox vbWorkArea = new VBox();
		vbWorkArea.setPrefWidth(500);
		
		hLibMenu.getChildren().add(vbWorkArea);
		
		Accordion menuContent = new Accordion();
		TitledPane menuSearch = new TitledPane();
		menuSearch.setAnimated(false);
		menuSearch.setText("Search");
		
		AnchorPane apSearch = new AnchorPane();
		apSearch.setPrefWidth(200);
		apSearch.setPrefHeight(200);
		
		//Library Member Button
		Button searchLibMem = new Button("Library Member");
		searchLibMem.setPrefWidth(180);
		searchLibMem.setLayoutX(18);
		searchLibMem.setLayoutY(124);
		
		SearchLibraryMember slm = new SearchLibraryMember();
		searchLibMem.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	if (vbWorkArea.getChildren().size() == 1) {
		    		vbWorkArea.getChildren().remove(0);
		    	}
		    	vbWorkArea.getChildren().add(slm);
		    }
		});
		
		apSearch.getChildren().add(searchLibMem);
		
		// Publication Button
		Button searchPub = new Button("Publication");
		searchPub.setPrefWidth(180);
		searchPub.setLayoutX(19);
		searchPub.setLayoutY(91);
		
		SearchPublication sp = new SearchPublication();
		searchPub.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	if (vbWorkArea.getChildren().size() == 1) {
		    		vbWorkArea.getChildren().remove(0);
		    	}
		    	vbWorkArea.getChildren().add(sp);
		    }
		});
		
		apSearch.getChildren().add(searchPub);
        
        menuSearch.setContent(apSearch);
		menuContent.getPanes().add(menuSearch);
		
		TitledPane menuManage = new TitledPane();
		menuManage.setAnimated(false);
		menuManage.setText("Manage");
		
		AnchorPane apManage = new AnchorPane();
		apManage.setPrefWidth(200);
		apManage.setPrefHeight(200);
		
		//Add Member Button
		Button ManageLibMem = new Button("Add Member");
		ManageLibMem.setPrefWidth(180);
		ManageLibMem.setLayoutX(18);
		ManageLibMem.setLayoutY(124);
		
		AddLibraryMember am = new AddLibraryMember();
		ManageLibMem.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	if (vbWorkArea.getChildren().size() == 1) {
		    		vbWorkArea.getChildren().remove(0);
		    	}
		    	vbWorkArea.getChildren().add(am);
		    }
		});
		
		apManage.getChildren().add(ManageLibMem);
		
		Button ManagePub = new Button("Add Publication");
		ManagePub.setPrefWidth(180);
		ManagePub.setLayoutX(19);
		ManagePub.setLayoutY(91);
		
		AddPublication ap = new AddPublication();
		ManageLibMem.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
		    	if (vbWorkArea.getChildren().size() == 1) {
		    		vbWorkArea.getChildren().remove(0);
		    	}
		    	vbWorkArea.getChildren().add(ap);
		    }
		});
		
		apManage.getChildren().add(ManagePub);
		
		menuManage.setContent(apManage);
		
		menuContent.getPanes().add(menuManage);
		
		spMenuContent.getItems().add(menuContent);
		

		
		return hLibMenu;
	}
	
	private Node getBottom() {
		VBox vBottom = new VBox();
		vBottom.setMinHeight(50);
		Button btnSubmit = new Button("Submit");
		btnSubmit.setOnAction(new EventHandler<ActionEvent>() {
		    @Override
		    public void handle(ActionEvent e) {
//		    	System.out.println(userTextField.getText());
//		    	System.out.println(streetTextField.getText());
//		    	System.out.println(cityTextField.getText() + ", " + stateTextField.getText() + " " + zipTextField.getText());
		    	System.out.println("");
		    }
		});
		vBottom.setAlignment(Pos.BOTTOM_CENTER);
		vBottom.getChildren().add(btnSubmit);
		return vBottom;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
