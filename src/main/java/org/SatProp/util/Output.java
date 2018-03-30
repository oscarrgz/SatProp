package org.SatProp.util;

import java.io.IOException;
import java.util.List;

import org.orekit.errors.OrekitException;
import org.orekit.files.ccsds.OEMWriter;
import org.orekit.files.general.OrekitEphemerisFile;
import org.orekit.files.general.OrekitEphemerisFile.OrekitSatelliteEphemeris;
import org.orekit.propagation.SpacecraftState;

/** This class implements output methods to write satellite ephemeris
 * 
 * @author Oscar Rodriguez Fernandez
 *
 */
public class Output {
	
	private OrekitEphemerisFile ephemerisFile;
	//private OrekitSatelliteEphemeris satellite;
	
	
	/**
	 * Constructror method 
	 * 
	 * @param SatID
	 * @param states
	 * @throws OrekitException
	 */
	public  Output(String SatID,  List<SpacecraftState> states ) throws OrekitException {
		/**
		 * Constructor of the simple output class to generate a OEM
		 */
		this.ephemerisFile = new OrekitEphemerisFile();
		OrekitSatelliteEphemeris satellite = this.ephemerisFile.addSatellite(SatID);
		satellite.addNewSegment(states);
	}
	
	/**
	 * Writes an OEM file
	 * 
	 * @param outputfile
	 * @throws OrekitException
	 * @throws IOException
	 */
	public  void write_OEM (String outputfile) throws OrekitException, IOException {
		/** 
		 * Writes and output OEM file with the info contained in the structure
		 */
		new OEMWriter().write(outputfile, this.ephemerisFile);
	}

}
