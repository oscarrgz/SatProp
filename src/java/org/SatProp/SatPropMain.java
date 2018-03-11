package org.SatProp;

import java.io.IOException;


import javafx.application.Application;
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
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
        this.primaryStage.setTitle("SatProp");

        initRootLayout();
        
        showTerminalView();
        
        showConfigurationTabs();
        
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

            // Set person overview into the bottom of root layout.
            rootLayout.setBottom(terminalView);
            
            //Give the controller access to the main app.
//            terminalView controller = loader.getController();
//            controller.setMainApp(this);
            
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
            TabPane ConfigurationTabs = (TabPane) loader.load();
            
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
