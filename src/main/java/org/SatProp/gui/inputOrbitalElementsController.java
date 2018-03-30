package org.SatProp.gui;

import java.util.List;
import java.util.Properties;

import org.SatProp.util.DateUtil;
import org.SatProp.util.Validator;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;

/*
 * controller of the input method using 
 * classical orbital elements
 */
public class inputOrbitalElementsController {
	@FXML
	private Label epochLabel;
	@FXML
	private TextField EpochText;
	@FXML
	private TextField SemiMajorAxisText;
	@FXML
	private TextField EccentricityText;
	@FXML
	private TextField InclinationText;
	@FXML
	private TextField RAANText;
	@FXML
	private TextField ArgPText;
	@FXML
	private TextField TrueAnomalyText;
	@FXML
	private TextField AtoMText;
	@FXML
	private TextField CDText;
	@FXML
	private TextField CRText;
	
	/**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	// tooltip
    	epochLabel.setTooltip(new Tooltip("Format can be:\n- Julian Date (float)\n- isot (YYYY-MM-DDThh:mm:ss.sss)"));
    }
    
    public boolean ValidateInput() {
    	
    	String errorMessage = "";
    	
    	if (EpochText.getText() ==null || !DateUtil.isaDate(EpochText.getText() )) {
    		errorMessage +=" Accepted formats for the Epoch date are isot (YYYY-MM-DDThh:mm:ss.sss) or JD\n";
    	}
    	if (!Validator.isDouble(SemiMajorAxisText.getText())) {
    		errorMessage += "Semi-major axis must be a positive number\n";
    	}
    	
    	if (!Validator.isDouble(EccentricityText.getText())) {
    		errorMessage += "Eccentricity must be a number \n";
    	}
    	if (!Validator.isDoublebetween(InclinationText.getText(), 180., 0.)) {
    		errorMessage += "Inclination must be a number in degrees between 0 and 180 \n";
    	}
    	if (!Validator.isAngleDeg(RAANText.getText())) {
    		errorMessage += "RAAN must be a number in degrees between 0 and 360 \n";
    	}
    	if (!Validator.isAngleDeg(ArgPText.getText())) {
    		errorMessage += "Argument of Perigee must be a number in degrees between 0 and 360 \n";
    	}
    	if (!Validator.isAngleDeg(TrueAnomalyText.getText())) {
    		errorMessage += "True Anomaly of Perigee must be a number in degrees between 0 and 360 \n";
    	}
    	if (!Validator.isPositiveDouble(AtoMText.getText())) {
    		errorMessage += "Area to Mass Ratio must be a positive number \n";
    	}
    	if (!Validator.isPositiveDouble(CDText.getText())) {
    		errorMessage += "Drag Coefficient must be a positive number \n";
    	}
    	if (!Validator.isPositiveDouble(CRText.getText())) {
    		errorMessage += "Reflectivity Coefficient must be a positive number \n";
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
		String Elements = SemiMajorAxisText.getText() + " " + EccentricityText.getText() + " "
				+ InclinationText.getText() + " " + RAANText.getText() + " " + ArgPText.getText() + " "
				+ TrueAnomalyText.getText();
    					  
    	orbitalProperties.setProperty("Orbital_elements", Elements);
    	orbitalProperties.setProperty("Start_Epoch", EpochText.getText());
    	orbitalProperties.setProperty("CD", CDText.getText());
    	orbitalProperties.setProperty("CR", CRText.getText());
    	orbitalProperties.setProperty("atoM", AtoMText.getText());
    	
    	// SGP only valid for TLE defined objects
    	orbitalProperties.setProperty("SGP4_propagator", "0");
    	return orbitalProperties;
    }
    public void CleanFields () {
    	EpochText.setText("");
    	AtoMText.setText("");
    	CDText.setText("");
    	CRText.setText("");
    	SemiMajorAxisText.setText("");
    	EccentricityText.setText("");
    	InclinationText.setText("");
    	RAANText.setText("");
    	ArgPText.setText("");
    	TrueAnomalyText.setText("");
    }
	
    public void loadConfigurationState (Properties InputParams) {
    	// clean previous values
    	CleanFields();
    	// GET EPOCH
    	EpochText.setText(InputParams.getProperty("Start_Epoch"));
    	
    	// Get position and velocity
    	if (Validator.is6DVector(InputParams.getProperty("Orbital_Elements"))) {
    		List<Double> components = Validator.getdoubleComponents(InputParams.getProperty("Orbital_Elements"));
    		SemiMajorAxisText.setText(components.get(0).toString());
        	EccentricityText.setText(components.get(1).toString());
        	InclinationText.setText(components.get(2).toString());
        	RAANText.setText(components.get(3).toString());
        	ArgPText.setText(components.get(4).toString());
        	TrueAnomalyText.setText(components.get(5).toString());
    	}
    	
    	// Get dynamic model properties
    	AtoMText.setText(InputParams.getProperty("atoM"));
    	CDText.setText(InputParams.getProperty("CD"));
    	CRText.setText(InputParams.getProperty("CR"));
    }
}
