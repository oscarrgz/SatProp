package org.SatProp.util;

import java.io.File;

import org.orekit.data.DataProvider;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.errors.OrekitException;


public class Loader {
	
	private DataProvidersManager manager;
	public Loader () throws OrekitException {
		this.loadDefault();
	}
	
	public Loader (String data_path) throws OrekitException {
		
		// Check if data_path is default
		data_path = data_path.trim();
		if (data_path.trim().equals("Default")) {
			System.out.println("loading default data");
			this.loadDefault();
		} else {
			// Load user supplied path
			this.loadDirectory(data_path);
		}
	}
	
	public void loadDefault( ) throws OrekitException  {
			/*
			 * Path to the data folder
			 */
			final File home = new File (System.getProperty("user.home"));
			final File workspace = new File (home, "Documents" + File.separator 
															+ "Eclipse-workspace" );
			
			final File orekitdir = new File (workspace, "Data" + File.separator
															   + "orekit-data");
			// set up data provider
			final DataProvider provider = new DirectoryCrawler(orekitdir);
			this.manager = DataProvidersManager.getInstance();
			this.manager.addProvider(provider);
			
	}
	
	private void loadDirectory (String path) throws OrekitException {
		final File orekitdir = new File (path);
		
		// set up data provider
		final DataProvider provider = new DirectoryCrawler(orekitdir);
		this.manager = DataProvidersManager.getInstance();
		this.manager.addProvider(provider);
		
	}
	
	public void PrintData () {
		for (String s: DataProvidersManager.getInstance().getLoadedDataNames()) {
			System.out.println(s);
		}
	}

}
