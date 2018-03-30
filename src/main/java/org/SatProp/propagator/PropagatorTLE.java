package org.SatProp.propagator;

import java.util.ArrayList;
import java.util.List;

import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.orbits.CartesianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.time.AbsoluteDate;

public class PropagatorTLE {
	/*
	 * Propagates a TLE 
	 */
	
	public static List<SpacecraftState> propagate_tle(TLE initialTLE,  double propagationDurationSeconds, double stepSizeSeconds) throws OrekitException{
		List<SpacecraftState> states = new ArrayList<SpacecraftState>();
		
		TLEPropagator propagator = TLEPropagator.selectExtrapolator(initialTLE);
        AbsoluteDate initDate = initialTLE.getDate();

        Frame GCRF = FramesFactory.getGCRF();
        for (double dt = 0.0; dt < propagationDurationSeconds; dt += stepSizeSeconds) {
        	SpacecraftState TEMEstate = propagator.propagate(initDate.shiftedBy(dt)); 
        	// Transform orbit to GCRF frame
        	Orbit TEMEOrbit = TEMEstate.getOrbit();
        	Orbit GCRFOrbit = new CartesianOrbit(TEMEOrbit.getPVCoordinates(GCRF), GCRF, TEMEOrbit.getMu());
        	states.add(new SpacecraftState(GCRFOrbit));

        }
        
		return states;
	}
	
	public static List<SpacecraftState> propagate_tle(String line1, String line2,  double propagationDurationSeconds, double stepSizeSeconds) throws OrekitException{
		TLE initialTLE  = new TLE(line1, line2);
		
		return propagate_tle(initialTLE, propagationDurationSeconds, stepSizeSeconds);
	}
}
