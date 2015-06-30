package application;

import java.io.IOException;
import java.util.HashMap;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
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
			Parent root = FXMLLoader.load(getClass().getResource("LibraryMainView.fxml"));
			Scene scene = new Scene(root,800,600);			
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("MPP Library System");
			primaryStage.setResizable(true);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException io) {
			System.out.println(io.getStackTrace());
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
