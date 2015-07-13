package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class PreferencesController {
	@FXML private TextField tfDBConnection;
	@FXML private TextField tfDBUser;
	@FXML private TextField tfDBPW;
	@FXML private ToggleGroup tgSourceOptions;
	@FXML private RadioButton rbFile;
	@FXML private RadioButton rbDB;
	//@FXML private TextField tfMaxCheckout;
	//@FXML private TextField tfName;
	private boolean isConfigSave;
	private Stage dialogStage;
	
    @FXML // This method is called by the FXMLLoader when initialization is complete
    protected void initialize() {
    	Properties prop = new Properties();
    	InputStream input = null;
     
    	try {
     
    		input = new FileInputStream("config.properties");
     
    		// load a properties file
    		prop.load(input);
     
    		if(prop.getProperty("Datasource").trim().equals("DB")) {
    			rbDB.setSelected(true);
    		} else {
    			rbFile.setSelected(true);
    		}
    		// get the property value and print it out
    		tfDBConnection.setText(prop.getProperty("database"));
    		tfDBUser.setText(prop.getProperty("dbuser"));
    		tfDBPW.setText(prop.getProperty("dbpassword"));
     
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
    }

	public boolean isConfigSave() {
		return isConfigSave;
	}

	public Stage getDialogStage() {
		return dialogStage;
	}
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	@FXML protected void onhandleSaveBtnAction(ActionEvent event) {
		if (saveConfiguration()) {
			String message = "Configuration is saved.\nRestart application for configuration to take effect...\n";
			Alert alert = new Alert(AlertType.INFORMATION, message, ButtonType.OK);
			alert.setHeaderText(null);
			alert.setTitle("Set Configuration");
			alert.show();
			isConfigSave = true;
		} else {
			String message = "Problem encountered.\nConfiguration is not saved.\n";
			Alert alert = new Alert(AlertType.ERROR, message, ButtonType.OK);
			alert.setHeaderText(null);
			alert.setTitle("Set Configuration");
			alert.show();
			isConfigSave = true;
		}
		dialogStage.close();
	}
	@FXML protected void onhandleCancelBtnAction(ActionEvent event) {
		isConfigSave = false;
		dialogStage.close();
	}

	private boolean saveConfiguration() {
		Properties prop = new Properties();
		OutputStream output = null;
		boolean save = false;
		try {
	 
			output = new FileOutputStream("config.properties");
	 
			// set the properties value
    		if(rbDB.isSelected()) {
    			prop.setProperty("Datasource", "DB");
    		} else {
    			prop.setProperty("Datasource", "FILE");
    		}
			prop.setProperty("database", tfDBConnection.getText().trim());
			prop.setProperty("dbuser", tfDBUser.getText().trim());
			prop.setProperty("dbpassword", tfDBPW.getText().trim());
	 
			// save properties to project root folder
			prop.store(output, null);
			save = true;
		} catch (IOException io) {
			save = false;
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	 
		}
		return save;
	}
}
