package org.SatProp;

import java.io.IOException;
import java.io.PrintStream;

import org.SatProp.gui.TerminalOutput;
import org.SatProp.gui.configurationTabController;
import org.SatProp.gui.rootLayoutController;
import org.SatProp.gui.terminalController;
import org.SatProp.propagator.Input;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SatPropMain extends Application {

	// Application Stage
	private Stage primaryStage;
	// Background layout
    private BorderPane rootLayout;
    // Properties 
    private ObservableList<Input> Parameters = FXCollections.observableArrayList();
    // controller of terminal view
    private terminalController the_terminal;
    // controller of tabs
    private configurationTabController the_tabs;
    // Root LayputController
    private rootLayoutController rootController;
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
        this.primaryStage.setTitle("SatProp");

        initRootLayout();
        
        // tabs need to be first
        showConfigurationTabs();
        
        showTerminalView();
        
        // Get access for the root controller to the configuration tabs
        rootController.setConfigurationController(the_tabs);
        
	}

	
	
	/**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SatPropMain.class.getResource("gui/rootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            
            // Get controller and pass the Stage
            rootController = loader.getController();
            rootController.setStage(primaryStage);
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
	
    /**
     * Sets the terminal view inside the root layout
     */
    
    public void showTerminalView() {
    	try {
            // Load terminal overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SatPropMain.class.getResource("gui/terminalLayOut.fxml"));
            AnchorPane terminalView = (AnchorPane) loader.load();
            the_terminal = loader.getController();
            // Set person overview into the bottom of root layout.
            rootLayout.setBottom(terminalView);
            
            //Give the controller access to the configuration tab
            the_terminal.setConfigurationTab(the_tabs);
            
            
            // Set the terminal as the system.out
            TerminalOutput console = new TerminalOutput(the_terminal.getTerminalArea());
            PrintStream ps = new PrintStream(console, true);
            System.setOut(ps);
            System.setErr(ps);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Sets the configuration panels inside the root layout
     */
    public void showConfigurationTabs() {
    	try {
    		// Load configuration tabs
    		FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SatPropMain.class.getResource("gui/configurationTabsLayout.fxml"));
            AnchorPane ConfigurationTabs = (AnchorPane) loader.load();
            the_tabs = loader.getController();
            // Set configuration tab into the center of root layout
            rootLayout.setCenter(ConfigurationTabs);
            
            //
            
    	} catch (IOException e) {
            e.printStackTrace();
        }
    }

	public static void main(String[] args) {
		launch(args);
	}
}
