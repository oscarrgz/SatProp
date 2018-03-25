/**
 * 
 */
package org.SatProp.gui;


import java.util.List;
import java.util.Properties;

import org.SatProp.util.DateUtil;
import org.SatProp.util.Validator;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;

/**
 * @author oscar
 *
 */
public class inputTLEController {
	
	@FXML 
	private TextField TLE_Line1_Text;
	@FXML 
	private TextField TLE_Line2_Text;
	@FXML
	private TextField AtoMText;
	@FXML
	private TextField CDText;
	@FXML
	private TextField CRText;
	@FXML
	private CheckBox UseSGP4Box;
	
	@FXML
	private Label SGP4Label;
	
	private String LastCR;
	private String LastCD;
	private String LastAtoM;
	

	/**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	
    	SGP4Label.setTooltip(new Tooltip("IF checked no numerical propagation using the user-selected force model is performed. Only SGP4 theory is used"));
    	// SGP4 model
    	UseSGP4Box.selectedProperty().addListener( (observable, oldValue, newValue) -> handleSGP4button() );
    }
    
    public boolean ValidateInput() {
    	
    	String errorMessage = "";
    	
    	if (!Validator.isTLE(TLE_Line1_Text.getText(), TLE_Line2_Text.getText())) {
    		errorMessage += "TLE format is not correct";
    	}
    	
    	if (!UseSGP4Box.isSelected()) {
    		if (!Validator.isPositiveDouble(AtoMText.getText())) {
    			errorMessage += "Area to Mass Ratio must be a positive number \n";
    		}
    		if (!Validator.isPositiveDouble(CDText.getText())) {
    			errorMessage += "Drag Coefficient must be a positive number \n";
    		}
    		if (!Validator.isPositiveDouble(CRText.getText())) {
    			errorMessage += "Reflectivity Coefficient must be a positive number \n";
    		}
    	}
    	if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}

    }
    public Properties getValues() {
    	Properties orbitalProperties = new Properties();
		
    					  
    	orbitalProperties.setProperty("TLE_1", TLE_Line1_Text.getText());
    	orbitalProperties.setProperty("TLE_2", TLE_Line2_Text.getText());
    	orbitalProperties.setProperty("CD", CDText.getText());
    	orbitalProperties.setProperty("CR", CRText.getText());
    	orbitalProperties.setProperty("atoM", AtoMText.getText());
    	
    	// SGP only valid for TLE defined objects
    	if (UseSGP4Box.isSelected()) {
    		orbitalProperties.setProperty("SGP4_propagator", "1");
    	} else {
    		orbitalProperties.setProperty("SGP4_propagator", "0");
    	}
    	return orbitalProperties;
    }
    
    public void CleanFields () {
    	TLE_Line1_Text.setText("");
    	TLE_Line2_Text.setText("");
    	AtoMText.setText("");
    	CDText.setText("");
    	CRText.setText("");
    }
    
    public void loadConfigurationState (Properties InputParams) {
    	// clean previous values
    	CleanFields();
    	
    	TLE_Line1_Text.setText(InputParams.getProperty("TLE_1"));
    	TLE_Line2_Text.setText(InputParams.getProperty("TLE_2"));
    	
    	// Get dynamic model properties
    	AtoMText.setText(InputParams.getProperty("atoM"));
    	CDText.setText(InputParams.getProperty("CD"));
    	CRText.setText(InputParams.getProperty("CR"));
    }
    
    public void handleSGP4button () {
    	if (UseSGP4Box.isSelected()) {
    		// Save current values of CD, CR and AtoM
    		// and disable fields
    		LastAtoM = AtoMText.getText();
    		LastCR = CRText.getText();
    		LastCD = CDText.getText();
    		
    		AtoMText.setText("");
        	CDText.setText("");
        	CRText.setText("");
        	
        	AtoMText.setDisable(true);
        	CDText.setDisable(true);
        	CRText.setDisable(true);
    	} else {
    		// Write the last saved value of 
    		// CR, CD and AtoM and enable the fields
    		AtoMText.setText(LastAtoM);
        	CDText.setText(LastCD);
        	CRText.setText(LastCR);
        	AtoMText.setDisable(false);
        	CDText.setDisable(false);
        	CRText.setDisable(false);
        	
    	}
    	
    }
}
    
    
