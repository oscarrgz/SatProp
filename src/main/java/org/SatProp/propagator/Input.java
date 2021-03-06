package org.SatProp.propagator;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;



import org.SatProp.util.DateUtil;
import org.SatProp.util.Validator;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.orbits.CartesianOrbit;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.PositionAngle;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;
import org.orekit.utils.PVCoordinates;

/**
 * Reads an input file to perform a propagation with Orekit
 * 
 * @author Oscar Rodriguez Fernandez
 *
 */
public class Input {
	
	
	// fields
	// Perturbation model
	public Properties parameters;
	// Satellite state

	public Properties getParameters() {
		return parameters;
	}
	public Input (String file_path) throws IOException {
		readDefault();
		final File f = new File (file_path);
		if (f.isFile()) {
			this.readConfig(file_path);
		} else {
			System.out.println("No input file");
		}
	}
	public Input() throws IOException {
		this("OrekitPropagator.ini");
	}
	public Input(Properties initialProperties) throws IOException {
		readDefault();
		updateConfig(initialProperties);
		
	}

	private void readDefault () throws IOException{
		// reads default config file
		Properties defaultProp = new Properties();
		defaultProp.load(Input.class.getResourceAsStream("/Default.properties"));
		// stores into variable
		this.parameters= new Properties(defaultProp);
	}
	
	private void readConfig (String file_path) throws IOException {
		
		this.parameters.load(new FileInputStream(file_path));

	}
	public void updateConfig(Properties newProperties) throws IOException {
		// update existing properties
		
		// Print all the keys
		Set prop;
		String str;
		prop = newProperties.keySet();
		Iterator itr = prop.iterator();
		while(itr.hasNext()) {
			str = (String) itr.next();
			parameters.setProperty(str, newProperties.getProperty(str));
		}
	}
	
	public void printValues() {
		// Print all the keys
		Set prop;
		String str;
		
		prop = this.parameters.keySet();
		Iterator itr = prop.iterator();
		
		while(itr.hasNext()) {
			str = (String) itr.next();
			System.out.println ("The property " + str + " is set to " + this.parameters.getProperty(str));
		}
	}
	public Orbit getInitialOrbit() throws OrekitException{
		// Gets the initial user-supplied orbit
		Orbit initialOrbit;
		// Get initial date
		AbsoluteDate epoch = DateUtil.readDate(this.parameters.getProperty("Start_Epoch"));
		// Get initial StateVector
		// Select frame
		int input_state_format = Integer.parseInt(this.parameters.getProperty("Initial_state_format").trim());
		Frame frame = FramesFactory.getGCRF();
		// ORBITAL ELEMENTS
		if (input_state_format == 1) {
			frame = FramesFactory.getGCRF();
			// Get values
			double a = 42000*1000.0;
			double ecc = 0;
			double inc = 0;
			double RAAN = 0;
			double argP = 0;
			double TA = 0;
			
			if (Validator.is6DVector(this.parameters.getProperty("Orbital_Elements"))) {
				 List<Double> components = Validator.getdoubleComponents(this.parameters.getProperty("Orbital_Elements"));
				 
				 a = components.get(0)*1000.0;
				 ecc = Math.toRadians(components.get(1));
				 inc = Math.toRadians(components.get(2));
				 RAAN = Math.toRadians(components.get(3));
				 argP = Math.toRadians(components.get(4));
				 TA = Math.toRadians(components.get(5));

			} else {
				System.out.println(
						" No match found for keplerian elements in " + this.parameters.getProperty("Orbital_Elements"));
				System.exit(-1);
			}
			initialOrbit = new CartesianOrbit( new KeplerianOrbit(a, ecc, inc, argP, RAAN, TA, PositionAngle.TRUE, frame, epoch,
					Constants.EGM96_EARTH_MU));
			
		// TLE orbit
		} else if (input_state_format == 5) {
			TLE orbitTLE = getTLE();
			TLEPropagator propagator = TLEPropagator.selectExtrapolator(orbitTLE);
			SpacecraftState initialState = propagator.getInitialState();
			initialOrbit = new CartesianOrbit(initialState.getOrbit());
			
			
		} else {
			// Select Appropiate frame
			if (input_state_format == 2) {
				// GCRF frame
				frame = FramesFactory.getGCRF();
			} else if (input_state_format == 3){
				// ITRF frame
				frame = FramesFactory.getITRF(IERSConventions.IERS_2010, true);
			} else if (input_state_format == 4) {
				// EME 2000
				frame = FramesFactory.getEME2000();
			}
			// Read PV coordinates
			Vector3D pos = null;
			Vector3D vel = null;
			if (Validator.is6DVector(this.parameters.getProperty("StateVector"))) {
				List<Double> components = Validator.getdoubleComponents(this.parameters.getProperty("StateVector"));
				 
				pos = new Vector3D(components.get(0), components.get(1), components.get(2));
				vel = new Vector3D(components.get(3), components.get(4), components.get(5));
				pos = pos.scalarMultiply(1000.0);
				vel = vel.scalarMultiply(1000.0);
			} else {
				System.exit(-1);
			}
			
			final PVCoordinates PV = new PVCoordinates(pos, vel);
			initialOrbit = new CartesianOrbit(PV, frame, epoch, Constants.EGM96_EARTH_MU);
		}
		return initialOrbit;
		
	}
	
	
	public double getTimeStep () {
		double TimeStep;
		TimeStep = Double.parseDouble(this.parameters.getProperty("outTimeStep"));
		return TimeStep;
	}
	
	public double getPropagationDuration() throws OrekitException {
		double PropagationDuration;
		int EndFormat = Integer.parseInt(this.parameters.getProperty("End_format").trim());
		
		if (EndFormat == 1 | EndFormat ==2) {
			// End Format is given as a date (JD or ISOt)
			AbsoluteDate EndDate = DateUtil.readDate(this.parameters.getProperty("End_epoch"));
			AbsoluteDate StartDate = DateUtil.readDate(this.parameters.getProperty("Start_Epoch"));
			
			PropagationDuration = EndDate.durationFrom(StartDate);
			return PropagationDuration;
		} else if (EndFormat==3) {
			// End Epoch is directly given as seconds from start
			PropagationDuration = Double.parseDouble(this.parameters.getProperty("End_epoch"));
			return PropagationDuration;
		} else if (EndFormat == 4) {
			// End Epoch is given as days from start
			PropagationDuration = Double.parseDouble(this.parameters.getProperty("End_epoch"));
			PropagationDuration = PropagationDuration*Constants.JULIAN_DAY;
			return PropagationDuration;
		} else {
			// Unknown Switch for output
			System.out.println("Unknown Switch for end epoch format");
			PropagationDuration = 0;
			return PropagationDuration;
		}
	}
	
	public TLE getTLE () throws OrekitException {
		// Generates a TLE orbit
		String line_1 = this.parameters.getProperty("TLE_1");
		String line_2 = this.parameters.getProperty("TLE_2");
		TLE TLEorbit = new TLE(line_1.trim(), line_2.trim());
		return TLEorbit;
	}
	
	public String getOEMfile() {
		return this.parameters.getProperty("outputFile", "output.oem");
	}
	
	public String getDataPath() {
		return this.parameters.getProperty("Data_path", "Default");
	}
	
	public boolean useSGP4() {
		// Checks if a TLE must be propagated using SGP4 model
		if (Integer.parseInt(this.parameters.getProperty("SGP4_propagator", "0").trim()) == 1) {
			// SGP4 model is selected, check if the orbit was defined using TLE, return false otherwise
			if (Integer.parseInt(this.parameters.getProperty("Initial_state_format").trim()) == 5) {
				return true;
			} else {
				return false;
			}
		} else
			// SGP4 is not selected, return false
			return false;
	}
	
}
