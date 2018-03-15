package org.SatProp.propagator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.SatProp.util.Loader;
import org.orekit.errors.OrekitException;
import org.orekit.files.ccsds.OEMWriter;
import org.orekit.files.ccsds.OEMWriter.InterpolationMethod;
import org.orekit.files.general.OrekitEphemerisFile;
import org.orekit.files.general.OrekitEphemerisFile.OrekitSatelliteEphemeris;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.tle.TLE;


public class PropagatorDriver {
	public static  void runPropagation (Input Parameters) throws OrekitException, IOException{
		// Load data
					System.out.println("---------------------------------");
					System.out.println("Loading configuration data: ");
					System.out.println("---------------------------------");
					final Loader data = new Loader(Parameters.getDataPath());
				
					System.out.println("Data loaded\n");
					
					//Get configuration of the propagation
					List<SpacecraftState> states = new ArrayList<SpacecraftState>();
					final double propagationDurationSeconds = Parameters.getPropagationDuration();
					final double stepSizeSeconds = Parameters.getTimeStep();
					if (Parameters.useSGP4()) {
						System.out.println("---------------------------------");
						System.out.println("Propagating TLE");
						System.out.println("---------------------------------\n");
						TLE initialTLE = Parameters.getTLE();
						states = PropagatorTLE.propagate_tle(initialTLE, propagationDurationSeconds, stepSizeSeconds);
					} else {
						// Get initial Orbit
						Orbit initialOrbit = Parameters.getInitialOrbit();
						System.out.println("---------------------------------");
						System.out.println("Setting propagation Force Model:");
						System.out.println("---------------------------------");
						// Get Force Model for propagation
						final PropagationForceModel perturbations = new PropagationForceModel(Parameters.parameters);
						System.out.println("");

						// Propagate Orbit
						System.out.println("---------------------------------");
						System.out.println("Propagating");
						System.out.println("---------------------------------\n");
						states = OrbitPropagator.propagate_orbit(initialOrbit,perturbations.getPerturbations(),  propagationDurationSeconds, stepSizeSeconds );
					}
			        // write Output file
			        OrekitEphemerisFile ephemerisFile = new OrekitEphemerisFile();
			        OrekitSatelliteEphemeris satellite = ephemerisFile.addSatellite("Satellite");
			        satellite.addNewSegment(states);

			        String OEMfile = Parameters.getOEMfile().trim();
			        System.out.println("---------------------------------");
			        System.out.println("Writing results to " + OEMfile);
			        System.out.println("---------------------------------");
			        new OEMWriter(InterpolationMethod.LAGRANGE, "SatProp", null, null).write(OEMfile, ephemerisFile);
	
			        
			        
			        // Print used data
			        System.out.println("\n Outside data used:");
			        data.PrintData();
	}

}
