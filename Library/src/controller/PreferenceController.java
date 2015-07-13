package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;

public class PreferenceController implements Initializable {

	@FXML private Pane fieldPane;

	@FXML private TextField dbuser;
	@FXML private TextField contStr;
	@FXML private TextField dbpass;
	@FXML private Button saveBtn;
	@FXML private RadioButton radioFile;
	@FXML private RadioButton radioDB;
	private static Properties properties = new Properties();
	private static final String propFileName = "./config.properties";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		InputStream input = null;
		 
		try {	 
			input = new FileInputStream(propFileName);
	 
			// load a properties file
			properties.load(input);
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// TODO Auto-generated method stub
		if(properties.getProperty("Datasource").equals("FILE"))
		{
			fieldPane.setDisable(true);
			radioDB.setSelected(false);
			radioFile.setSelected(true);
		}
		else 
		{
			System.out.println(properties.getProperty("dbpassword"));
			fieldPane.setDisable(false);
			radioDB.setSelected(true);
			radioFile.setSelected(false);
			
		}
		dbpass.setText(properties.getProperty("dbpassword"));
		dbuser.setText(properties.getProperty("dbuser"));
		contStr.setText(properties.getProperty("database"));
		
	}

	@FXML protected void handleSetDB(ActionEvent event) {
		fieldPane.setDisable(false);
		radioFile.setSelected(false);
		
	}
	@FXML protected void handleSetFile(ActionEvent event) {
		fieldPane.setDisable(true);
		radioDB.setSelected(false);
	}
	@FXML protected void handleSaveSettings(ActionEvent event) {
		OutputStream out = null;
		 
		try {	 
			out = new FileOutputStream(propFileName);
	 
			// load a properties file
			if(radioDB.isSelected())
			{
				properties.setProperty("Datasource","DB");
			}
			else properties.setProperty("Datasource","FILE");
			
			properties.setProperty("database",contStr.getText());
			properties.setProperty("dbuser",dbuser.getText());
			properties.setProperty("dbpassword",dbpass.getText());
			
			properties.store(out, "Settings updated :");
			
			Alert alert = new Alert(AlertType.INFORMATION, "", ButtonType.OK);
			alert.setTitle("System Settings");
			alert.setHeaderText(null);
			alert.setContentText("System settings updated!");
			alert.show();
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
		}
	}
}
