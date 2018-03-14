package org.SatProp.propagator;

import java.util.ArrayList;
import java.util.List;

import org.hipparchus.ode.ODEIntegrator;
import org.hipparchus.ode.nonstiff.DormandPrince54Integrator;
import org.orekit.errors.OrekitException;
import org.orekit.forces.ForceModel;
import org.orekit.forces.gravity.HolmesFeatherstoneAttractionModel;
import org.orekit.forces.gravity.NewtonianAttraction;
import org.orekit.forces.gravity.potential.GravityFieldFactory;
import org.orekit.frames.FramesFactory;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.numerical.NumericalPropagator;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;
import org.orekit.time.AbsoluteDate;

public class OrbitPropagator {
	
	public static List<SpacecraftState> propagate_orbit (Orbit initialOrbit, List<ForceModel> forceModel, double propagationDurationSeconds, double stepSizeSeconds) throws OrekitException{
		List<SpacecraftState> states = new ArrayList<SpacecraftState>();
		final double[][] tolerances = NumericalPropagator.tolerances(10.0, initialOrbit, initialOrbit.getType());

		final ODEIntegrator integrator =
				new DormandPrince54Integrator(0.01, 100.0, tolerances[0], tolerances[1]);
		final NumericalPropagator propagator = new NumericalPropagator(integrator);
		propagator.setOrbitType(initialOrbit.getType());
		
		propagator.resetInitialState(new SpacecraftState(initialOrbit));
		propagator.setMu(Constants.EGM96_EARTH_MU);

		for (ForceModel force: forceModel) {
			propagator.addForceModel(force);
		}
		
		
		final AbsoluteDate date = initialOrbit.getDate();
		
		for (double dt = 0.0; dt < propagationDurationSeconds; dt += stepSizeSeconds) {
            states.add(propagator.propagate(date.shiftedBy(dt)));
        }
		
		
		return states;
	}
	
	public static List<SpacecraftState> propagate_orbit (Orbit initialOrbit, ForceModel forceModel, double propagationDurationSeconds, double stepSizeSeconds) throws OrekitException{
		List<ForceModel> listForces= new ArrayList<ForceModel>();
		listForces.add(forceModel);
		return propagate_orbit(initialOrbit, listForces, propagationDurationSeconds,stepSizeSeconds);
	}
	
	public static List<SpacecraftState> propagate_orbit (Orbit initialOrbit,  double propagationDurationSeconds, double stepSizeSeconds) throws OrekitException{
		
		final ForceModel defaultModel =
				new NewtonianAttraction(Constants.EGM96_EARTH_MU);
		
		return propagate_orbit (initialOrbit, defaultModel, propagationDurationSeconds, stepSizeSeconds);
		
	}
	
	
}
