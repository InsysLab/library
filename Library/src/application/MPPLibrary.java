package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MPPLibrary extends Application {
    
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../view/LibraryMainView.fxml"));
			Scene scene = new Scene(root,800,600);			
			scene.getStylesheets().add(getClass().getResource("../view/application.css").toExternalForm());
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
