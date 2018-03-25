package org.SatProp.gui;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.SatProp.propagator.Input;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class rootLayoutController {
	
	@FXML
	private MenuItem LoadFileItem;
	@FXML 
	private MenuItem ReloadItem;
	
	private Stage primaryStage;
	
	private configurationTabController configurationController;
	
	public void setConfigurationController(configurationTabController configurationController) {
		this.configurationController = configurationController;
	}

	@FXML
    private void initialize() {
    	
    }
	
	public void setStage (Stage ownerStage) {
		primaryStage = ownerStage;
	}
	
	public void LoadFile() throws IOException {
		// Open a file chooser dialog to load a new file
		
		final FileChooser fileChooser = new FileChooser();
		
		File configuration_file = fileChooser.showOpenDialog(primaryStage);
		
		if (configuration_file != null) {
			// Call input method to get new parameters
			Properties newProperties = new Input(configuration_file.toString()).getParameters();
			
			configurationController.loadConfigurationState(newProperties);

		}
		
	}
}
