package org.SatProp.gui;

import java.io.IOException;

import org.SatProp.propagator.Input;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;

public class terminalController {

	/*
	 * Scene Elements
	 */
	@FXML 
	private Button runButton;
	@FXML 
	private Button clearButton;
	@FXML 
	private TextArea terminalOutput;
	
	
	private configurationTabController TabsController;
	
	
	/**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	// The terminal is not editable, is just for output
    	terminalOutput.setEditable(false);
    }
    
    /**
     * Called when the user clicks on the run button.
     * @throws IOException 
     */
    @FXML
    private void handleRunButton() throws IOException {
        // TO DO
    	// Run the orbit propagator with the current stage of properties
    	
    	// Mockup write something to the text area
    	terminalOutput.appendText("Now I should be running some code\n");
    	terminalOutput.appendText("IT should produce several outputs/n");
    	terminalOutput.appendText("Now I am done\n");
    	
    	Input configuration = new Input();
    	
    	// Print initial values of input
    	System.out.println("Printing file configuration");
    	configuration.printValues();
    	System.out.println("------End of file configuration-----\n");
    	
    	// get new configuaration
    	System.out.println("\nPrinting  new user selected configuration");
    	configuration.updateConfig(TabsController.getParameters());
    	configuration.printValues();
    	System.out.println("------End of user configuration-----\n");
    }
    
    /**
     * Called when the user clicks on the clear button
     */
    @FXML
    private void handleClearButton() {
    	// Clear all the text
    	terminalOutput.clear();
    }
    /*
     * This method gets the instance of the configuration controller
     */
    public void setConfigurationTab (configurationTabController configurationController) {
    	TabsController = configurationController;
    }
    
    
}
