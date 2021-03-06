package org.SatProp.gui;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.SatProp.propagator.Input;
import org.SatProp.util.DateUtil;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class configurationTabController {
	
	/**
	 * Force Model Tab
	 */
	@FXML 
	private RadioButton LunarGravOn;
	@FXML 
	private RadioButton LunarGravOff;
	@FXML 
	private RadioButton SolarGravOn;
	@FXML 
	private RadioButton SolarGravOff;
	@FXML 
	private RadioButton SRPOn;
	@FXML 
	private RadioButton SRPOff;
	@FXML 
	private ToggleGroup Drag;
	@FXML 
	private RadioButton DragOn;
	@FXML 
	private RadioButton DragOff;
	@FXML 
	private RadioButton PlanetGravAll;
	@FXML 
	private RadioButton PlanetGravOff;
	
	@FXML 
	private CheckBox Mercury;
	@FXML 
	private CheckBox Venus;
	@FXML 
	private CheckBox Mars;
	@FXML 
	private CheckBox Jupiter;
	@FXML 
	private CheckBox Saturn;
	@FXML 
	private CheckBox Neptune;
	@FXML 
	private CheckBox Uranus;
	@FXML 
	private CheckBox Pluto;
	
	@FXML
	private ChoiceBox<String> gravModel;
	
	@FXML 
	private ChoiceBox<String> Atmosphere;
	
	@FXML 
	private TextField order;
	@FXML 
	private TextField degree;
	
	
	/**
	 * Configuration Tab
	 */
	@FXML
	private ChoiceBox<String> OutputFrame;
	
	@FXML 
	private RadioButton Final_date;
	@FXML
	private RadioButton PropagationinDays;
	@FXML
	private RadioButton PropagationinSeconds;
	@FXML 
	private TextField DataFolder;
	@FXML
	private TextField PropagationTime;
	@FXML
	private TextField Output_TimeStep;
	@FXML
	private TextField OutputFileName;
	
	
	/**
	 * State vector Tab
	 */
	@FXML
	private ChoiceBox<String>InputFrame;
	
	@FXML
	private BorderPane OrbitInputPane;
	
	private inputOrbitalElementsController OrbitController;
	
	private inputStateVectorController StateController;
	
	private inputTLEController TLEController;
	
	private Properties Parameters;
	
	/*
	 * Initialization Methods
	 */
	
	/**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	// Dummy so far
    	try {
			Parameters = new Input().getParameters();
		} catch (IOException e) {
			Parameters = new Properties();
		}
    	
    	initializeForceModelTab();
    	initializeConfigurationTab();
    	initializeOrbitInputTab();
    	
    	// Load the initial configuration as defined in parameters
    	loadConfigurationState(Parameters);
    }
	
    
    private void initializeForceModelTab() {
    	// Set initial value
    	PlanetGravAll.setSelected(true);
    	handlePlanetaryGrav();
    	
    	// Choice boxes
    	gravModel.setItems(FXCollections.observableArrayList("Two Body", "EGM96", "GGM02C","GGM02S"));
    	gravModel.getSelectionModel().selectFirst();
    	gravModel.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> handleGravityModel(newValue, oldValue) );
    	Atmosphere.setItems(FXCollections.observableArrayList("Harris-Priester"));
    	Atmosphere.getSelectionModel().selectFirst();
  
    	// initial Values Temporal
    	PlanetGravAll.setSelected(true);
    	handlePlanetaryGrav();
    	LunarGravOn.setSelected(true);
    	SolarGravOn.setSelected(true);
    	DragOn.setSelected(true);
    	SRPOn.setSelected(true);
    }
    
    private void initializeConfigurationTab() {
    	// Choice box 
    	OutputFrame.setItems(FXCollections.observableArrayList("GCRF", "EME2000", "ITRF","TEME"));
    	OutputFrame.getSelectionModel().selectFirst();
    	
    	// tooltip
    	Final_date.setTooltip(new Tooltip("Format can be:\n- Julian Date (float)\n- isot (YYYY-MM-DDThh:mm:ss.sss)"));
    }
    
    private void initializeOrbitInputTab() {
    	// Choice box 
    	InputFrame.setItems(FXCollections.observableArrayList("Orbital Elements","GCRF", "EME2000", "ITRF","TLE"));
    	InputFrame.getSelectionModel().selectFirst();
    	InputFrame.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> handleInputModeChange(oldValue, newValue) );
    	// show the correct view
    	showInputMode ();
    }
    

    /**
     * Sets the values of the different fields to match the current configuration
     * @param InputParams
     */
    public void loadConfigurationState (Properties InputParams) {
    	
    	// Save new parameters
    	Parameters = InputParams;
    	
    	// clean Previously existing state
    	CleanFields();
    	
    	
    	//Earth gravity
    	gravModel.getSelectionModel().selectFirst();
		int GravModel = Integer.parseInt(InputParams.getProperty("Earth_gravity").trim());
		if (GravModel == 0 ) {
			System.out.println("Using two-body model for Earth gravity");
			gravModel.getSelectionModel().selectFirst();
		} else {
			if (GravModel ==1 ) {
				// EGM96 gravity model
				gravModel.getSelectionModel().select(2);
			} else if (GravModel ==2) {
				// GGM0C2C gravity model
				gravModel.getSelectionModel().select(3);
			} else if (GravModel ==3 ) {
				// GGM0C2S gravity model
				gravModel.getSelectionModel().select(4);
			}
			order.setText(InputParams.getProperty("Earth_gravity_order").trim());
			degree.setText(InputParams.getProperty("Earth_gravity_degree").trim());
		}
    			
		// Sun Gravity
		int SunGravity = Integer.parseInt(InputParams.getProperty("Solar_gravity").trim());
		if (SunGravity == 1) {
			SolarGravOn.setSelected(true);
		} else {
			SolarGravOff.setSelected(true);
		}
    			
		// Lunar Gravity
		int MoonGravity = Integer.parseInt(InputParams.getProperty("Lunar_gravity").trim());
		if (MoonGravity == 1) {
			LunarGravOn.setSelected(true);
		} else {
			LunarGravOff.setSelected(true);
		}
		
		// Sun radiation pressure
		int SRP_switch = Integer.parseInt(InputParams.getProperty("Solar_Radiation_Pressure").trim());
		if (SRP_switch ==1) {
			SRPOn.setSelected(true);
		} else {
			SRPOff.setSelected(true);
		}
    			
    	// Drag
		if (Integer.parseInt(InputParams.getProperty("Drag").trim()) ==1) {
			DragOn.setSelected(true);
		} else {
			DragOff.setSelected(false);
		}
		
		// Planetary gravity
		int Planetary_grav = Integer.parseInt(InputParams.getProperty("Planet_gravity").trim());
		if (Planetary_grav ==1) {
			PlanetGravAll.setSelected(true);
			// turn all on
			handlePlanetaryGrav();
			// check those who should be off
			// 1
			if (Integer.parseInt(InputParams.getProperty("Mercury").trim()) !=1) {
				Mercury.setSelected(false);
			}
			// 2
			if (Integer.parseInt(InputParams.getProperty("Venus").trim()) !=1) {
				Venus.setSelected(false);
			}
			// 4
			if (Integer.parseInt(InputParams.getProperty("Mars").trim()) !=1) {
				Mars.setSelected(false);
			}
			// 5
			if (Integer.parseInt(InputParams.getProperty("Jupiter").trim()) !=1) {
				Jupiter.setSelected(false);
			}
			// 6
			if (Integer.parseInt(InputParams.getProperty("Saturn").trim()) !=1) {
				Saturn.setSelected(false);
			}
			// 7
			if (Integer.parseInt(InputParams.getProperty("Uranus").trim()) !=1) {
				Uranus.setSelected(false);
			}
			// 8
			if (Integer.parseInt(InputParams.getProperty("Neptune").trim()) !=1) {
				Neptune.setSelected(false);
			}
			// 9
			if (Integer.parseInt(InputParams.getProperty("Pluto").trim()) !=1) {
				Pluto.setSelected(false);
			}
		} else {
			// turn off planetary gravity
	    	PlanetGravAll.setSelected(false);
	    	handlePlanetaryGrav();
		}

    	Atmosphere.getSelectionModel().selectFirst();
     	
    	/*
    	 * Configuration tab
    	 */
    	DataFolder.setText(InputParams.getProperty("Data_path"));
    	
    	switch (InputParams.getProperty("End_format").trim()) {
    		
    			case "1":
    			case "2":{	
    				Final_date.setSelected(true);
    			}
    			case "3": {
    				PropagationinSeconds.setSelected(true);
    			}
    			case "4":{
    				PropagationinDays.setSelected(true);
    			}
    	}
    	PropagationTime.setText(InputParams.getProperty("End_epoch"));
    				
    	Output_TimeStep.setText(InputParams.getProperty("outTimeStep"));
    	
    	OutputFileName.setText(InputParams.getProperty("outputFile"));
    	
    	// Orbit Input
    	int input_state_format = Integer.parseInt(InputParams.getProperty("Initial_state_format").trim());
		switch (input_state_format) {
		case 1: 
			// Orbital Elements
			InputFrame.getSelectionModel().select(0);
			OrbitController.loadConfigurationState(InputParams);
			break;
		case 2: 
			// GCRF
			InputFrame.getSelectionModel().select(1);
			StateController.loadConfigurationState(InputParams);
			break;
		case 3: 
			// ITRF
			InputFrame.getSelectionModel().select(2);
			StateController.loadConfigurationState(InputParams);
			break;
		case 4: 
			// EME200
			InputFrame.getSelectionModel().select(3);
			StateController.loadConfigurationState(InputParams);
			break;
		case 5: 
			//TLE
			InputFrame.getSelectionModel().select(4);
			TLEController.loadConfigurationState(InputParams);
			break;
		default:
			break;
		}
		
		
	}
   
    /*
     * Default State
     */
    public void CleanFields () {
    	/*
    	 * Force Model
    	 */
    	PlanetGravAll.setSelected(true);
    	handlePlanetaryGrav();
    	
    	gravModel.getSelectionModel().selectFirst();
    	Atmosphere.getSelectionModel().selectFirst();
    	order.setText("");
    	degree.setText("");
    	// All On
    	PlanetGravAll.setSelected(true);
    	handlePlanetaryGrav();
    	LunarGravOn.setSelected(true);
    	SolarGravOn.setSelected(true);
    	DragOn.setSelected(true);
    	SRPOn.setSelected(true);
    	
    	/*
    	 * Configuration
    	 */
    	DataFolder.setText("");
    	PropagationinDays.setSelected(true);
    	PropagationTime.setText("");
    	Output_TimeStep.setText("");
    	OutputFrame.getSelectionModel().selectFirst();
    	OutputFileName.setText("");
    	
    	/*
    	 * Initial State
    	 */
    	InputFrame.getSelectionModel().selectFirst();
    	if (OrbitController != null) {
    		OrbitController.CleanFields();
    	} else if (StateController != null) {
    		StateController.CleanFields();
    	} else if (TLEController !=null) {
    		TLEController.CleanFields();
    	}
    	
    }
    
    
    /*
     * ------------------------
     * Force Model Tab Methods
     * --------------------------
     */
    
    /*
     * Earth Gravity model ChoiceBox
     */
    private void handleGravityModel(String newvalue, String oldvalue) {
    	String two_body = gravModel.getItems().get(0).toString();
    	if (newvalue.equals(two_body)){
    		// Two- Body case-> disable order and degree
    		order.setDisable(true);
    		order.setText("0");
    		degree.setDisable(true);
    		degree.setText("0");
    	} else if ( oldvalue.equals(two_body)) {
    		// Re-enable order and degree
    		order.setDisable(false);
    		order.setText("2");
    		degree.setDisable(false);
    		degree.setText("2");
    	}
    	
    }
    
    /** 
     * Sets up Planetary gravity
     * 
     */
    @FXML
    private void handlePlanetaryGrav() {
    	// Get status 
    	if (PlanetGravAll.isSelected()) {
    		// Check all Planets
    		Mercury.setSelected(true);
    		Venus.setSelected(true);
    		Mars.setSelected(true);
    		Jupiter.setSelected(true);
    		Saturn.setSelected(true);
    		Uranus.setSelected(true);
    		Neptune.setSelected(true);
    		Pluto.setSelected(true);
    	} else {
    		// UnCheck all Planets
    		Mercury.setSelected(false);
    		Venus.setSelected(false);
    		Mars.setSelected(false);
    		Jupiter.setSelected(false);
    		Saturn.setSelected(false);
    		Uranus.setSelected(false);
    		Neptune.setSelected(false);
    		Pluto.setSelected(false);
    	}
    }
    
   /*
    * Validate the input in the Force Model Tab
    */
    private boolean validateForceModel() {
    	//Check 
    	String errorMessage = "";
    	if (gravModel.getSelectionModel().getSelectedIndex() != 0) {
    		// Check that the order and degree are int numbers 
    		// greater than 0
    		boolean add_error = false;
    		// Check for correct order of gravity field
			if (degree.getText() != null) {
				try {
					int set_degree = Integer.parseInt(degree.getText().trim());
					if (set_degree < 0) {
						add_error = true;
					}
				} catch (NumberFormatException e) {
					add_error = true;
				}
			} else {
				add_error = true;
			}
			if (add_error) {errorMessage += "No valid degree (must be a positive integer)!\n";}
			add_error = false;
    	
			// Check for correct degree of gravity field
			if (order.getText() != null) {
				try {
					int set_order = Integer.parseInt(order.getText().trim());
					if (set_order < 0) {
						add_error = true;
					}
				} catch (NumberFormatException e) {
					add_error = true;
				}
			} else {
				add_error = true;
			}
			if (add_error) {errorMessage += "No valid order (must be a positive integer)!\n";}
			
    		
    	}
    	if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
//            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
    
    /**
     * Save Force Model Options
     */
    @FXML
    public void handleSaveForceModel () {
    	if (validateForceModel()) {
    		// Earth gravity force
    		
    		switch(gravModel.getSelectionModel().getSelectedItem()) {
    		case "Two Body":
    			Parameters.setProperty("Earth_gravity", "0");
    		case "EGM96":
    			Parameters.setProperty("Earth_gravity", "1");
    		case "GGM02C":
    			Parameters.setProperty("Earth_gravity", "2");
    		case "GGM02S":
    			Parameters.setProperty("Earth_gravity", "3");
    		default:
    			Parameters.setProperty("Earth_gravity", "0");
    		}
    		if (!Parameters.getProperty("Earth_gravity").trim().equals("0")){
    			// get order and degree
    			Parameters.setProperty("Earth_gravity_degree", degree.getText());
    			Parameters.setProperty("Earth_gravity_order", order.getText());
    		};
    		// SolarGRav
    		if (SolarGravOn.isSelected()) {
    			Parameters.setProperty("Solar_gravity", "1");
    		} else {
    			Parameters.setProperty("Solar_gravity", "0");
    		}
    		// LunarGRav
    		if (LunarGravOn.isSelected()) {
    			Parameters.setProperty("Lunar_gravity", "1");
    		} else {
    			Parameters.setProperty("Lunar_gravity", "0");
    		}
    		// Planet gravity
    		if (PlanetGravAll.isSelected()) {
    			Parameters.setProperty("Planet_gravity", "1");
    			// Selected Planets
    			if (Mercury.isSelected()) {
    				Parameters.setProperty("Mercury", "1");
    			} else {
    				Parameters.setProperty("Mercury", "0");
    			}
    			if (Venus.isSelected()) {
    				Parameters.setProperty("Venus", "1");
    			} else {
    				Parameters.setProperty("Venus", "0");
    			}
    			if (Mars.isSelected()) {
    				Parameters.setProperty("Mars", "1");
    			} else {
    				Parameters.setProperty("Mars", "0");
    			}
    			if (Saturn.isSelected()) {
    				Parameters.setProperty("Saturn", "1");
    			} else {
    				Parameters.setProperty("Saturn", "0");
    			}
    			if (Jupiter.isSelected()) {
    				Parameters.setProperty("Jupiter", "1");
    			} else {
    				Parameters.setProperty("Jupiter", "0");
    			}
    			if (Uranus.isSelected()) {
    				Parameters.setProperty("Uranus", "1");
    			} else {
    				Parameters.setProperty("Uranus", "0");
    			}
    			if (Neptune.isSelected()) {
    				Parameters.setProperty("Neptune", "1");
    			} else {
    				Parameters.setProperty("Neptune", "0");
    			}
    			if (Pluto.isSelected()) {
    				Parameters.setProperty("Pluto", "1");
    			} else {
    				Parameters.setProperty("Pluto", "0");
    			}
    		} else {
    			Parameters.setProperty("Planet_gravity", "0");
    		}
    		// Drag
    		if (DragOn.isSelected()) {
    			Parameters.setProperty("Drag", "1");
    		} else {
    			Parameters.setProperty("Drag", "0");
    		}
    		// SRP
    		if (SRPOn.isSelected()) {
    			Parameters.setProperty("Solar_Radiation_Pressure", "1");
    		} else {
    			Parameters.setProperty("Solar_Radiation_Pressure", "0");
    		}

    	}
    	
    }
    
    /*
     * ------------------------
     * Orbit Definition Tab
     * -------------------------
     */
    /*
     * Check if we need to check the input mode view
     */
    private void handleInputModeChange(String oldvalue, String newvalue) {
    	// Only change the pane if we go from/to TLE or Orbital Elements
    	if (oldvalue.equals("TLE")|
    		oldvalue.equals(("Orbital Elements"))|
    		newvalue.equals("TLE")|
    		newvalue.equals(("Orbital Elements"))) {
    		showInputMode();
    	}
    }
   
    /*
     * Shows the correct input mode for the Orbit
     */
    private void showInputMode () {
    	String selected = InputFrame.getSelectionModel().getSelectedItem();
    	
    	if (selected.equals("TLE")) {
    		// Show pane TLE
    		FXMLLoader loader = new FXMLLoader();
            loader.setLocation(configurationTabController.class.getResource("inputTLE.fxml"));
            AnchorPane TLEView;
			try {
				TLEView = (AnchorPane) loader.load();
				TLEController = loader.getController();
				// Set person overview into the bottom of root layout.
	            OrbitInputPane.setCenter(TLEView);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	} else if (selected.equals("Orbital Elements")) {
    		// Show pane Orbital Elements
    		FXMLLoader loader = new FXMLLoader();
            loader.setLocation(configurationTabController.class.getResource("inputOrbitalElements.fxml"));
            AnchorPane OrbElementsView;
			try {
				OrbElementsView = (AnchorPane) loader.load();
				OrbitController = loader.getController();
				// Set person overview into the bottom of root layout.
	            OrbitInputPane.setCenter(OrbElementsView);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	} else {
    		// Cartesian State vector
    		// Show pane Orbital Elements
    		FXMLLoader loader = new FXMLLoader();
            loader.setLocation(configurationTabController.class.getResource("inputStateVector.fxml"));
            AnchorPane StateVectorView;
			try {
				StateVectorView = (AnchorPane) loader.load();
				StateController = loader.getController();
				// Set person overview into the bottom of root layout.
	            OrbitInputPane.setCenter(StateVectorView);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    /*
     * Saves the input orbit of the user
     */
    public void handleSaveOrbit () {
    	// Check the correct input case
    	boolean update = false;
    	Properties newProperties = new Properties();
    	switch (InputFrame.getSelectionModel().getSelectedItem()) {
    		case "TLE":
    			// TODO
    			if (TLEController.ValidateInput()) {
    				newProperties = TLEController.getValues();
    				update = true;
    				newProperties.setProperty("Initial_state_format", "5");
    			}
    			break;
    		case "Orbital Elements":
    			if (OrbitController.ValidateInput()) {
    				newProperties = OrbitController.getValues();
    				update = true;
    				newProperties.setProperty("Initial_state_format", "1");
    			}
    			break;
    		default:
    			System.out.println(InputFrame.getSelectionModel().getSelectedItem());
    			if (StateController.ValidateInput()) {
    				newProperties = StateController.getValues();
    				update = true;
    				// Set the apropiate input reference frame
    				switch (InputFrame.getSelectionModel().getSelectedItem()) {
    				case "GCRF":
    					newProperties.setProperty("Initial_state_format", "2");
    					break;
    				case "EME2000":
    					newProperties.setProperty("Initial_state_format", "4");
    					break;
    				case "ITRF":
    					newProperties.setProperty("Initial_state_format", "3");
    					break;
    				}
    			}
    			break;
    	}
    	if (update) {
    		Set prop;
    		String str;
    		prop = newProperties.keySet();
    		Iterator itr = prop.iterator();
    		while(itr.hasNext()) {
    			str = (String) itr.next();
    			Parameters.setProperty(str, newProperties.getProperty(str));
    		}
    	}
    		
    }
    
    /*
     * --------------------------
     * Configuration Tab
     * --------------------------
     */
    /*
     * Validates that the input in the configuration tab is correct
     */
    public boolean validateConfigurationTab() {
    	//
    	String errorMessage = "";
    	// Propagation Time format
    	if (PropagationTime.getText() == null ||PropagationTime.getText().length()==0) {
    		errorMessage += "Propagation Time cannot be empty\n";
    	}
    	
    	// Propagation Time as a Final Date
    	if (Final_date.isSelected()) {
    		// check that the final date format is correct matching against isot or double number
    		if (! DateUtil.isaDate(PropagationTime.getText())){
    			errorMessage += "Accepted formats for the final date are isot (YYYY-MM-DDThh:mm:ss.sss) or JD\n";
    		}
    	} else {
    		//check that propagation time is a double
    		try {
    			Double.parseDouble(PropagationTime.getText());
    		} catch (NumberFormatException e) {
    			errorMessage += "Propagation Time must be a number\n";
    		}
    	}
    	boolean add_error = false;
    	if (Output_TimeStep.getText() != null) {
    		try {
    			if ( Double.parseDouble(Output_TimeStep.getText()) <= 0) {
    				add_error = true;
    			}
    		} catch (NumberFormatException e) {
    			add_error = true;
    		}
    	}else {
    		add_error = true;
    	}
    	if (add_error) {
    		errorMessage += "Output Time Step must be a positive number\n";
    	}
    	// Output File Name
    	if (OutputFileName.getText() == null ||OutputFileName.getText().length()==0) {
    		errorMessage += "Output File Name cannot be empty";
    	}
    		
		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			// alert.initOwner(dialogStage);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
    }
    
    public void handleSaveConfiguration () {
    	// Check for correct input
    	if (validateConfigurationTab()) {
    		// check data folder
    		if (DataFolder.getText() == null ||DataFolder.getText().length()==0) {
    			Parameters.setProperty("Data_path", "Default");
    		} else {
    			Parameters.setProperty("Data_path", DataFolder.getText());
    		}
    		if (Final_date.isSelected()) {
    			// duration as a date in JD or isot
    			Parameters.setProperty("End_format", "2");
    		} else if (PropagationinDays.isSelected()) {
    			// duration in days
    			Parameters.setProperty("End_format", "4");
    		} else {
    			// Duration in seconds
    			Parameters.setProperty("End_format", "3");
    		}
    		// Set duration of propagation
    		Parameters.setProperty("End_epoch", PropagationTime.getText());
    		// Set output time step
    		Parameters.setProperty("outTimeStep", Output_TimeStep.getText());
    		// Set Oututfile
    		Parameters.setProperty("outputFile", OutputFileName.getText());
    	}
    }
    
    public Properties getParameters() {
    	return Parameters;
    }
    
   

}
