package org.SatProp.propagator;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.errors.OrekitException;
import org.orekit.forces.ForceModel;
import org.orekit.forces.drag.DragForce;
import org.orekit.forces.drag.IsotropicDrag;
import org.orekit.forces.drag.atmosphere.Atmosphere;
import org.orekit.forces.drag.atmosphere.HarrisPriester;
import org.orekit.forces.gravity.HolmesFeatherstoneAttractionModel;
import org.orekit.forces.gravity.NewtonianAttraction;
import org.orekit.forces.gravity.ThirdBodyAttraction;
import org.orekit.forces.gravity.potential.GravityFieldFactory;
import org.orekit.forces.gravity.potential.ICGEMFormatReader;
import org.orekit.forces.radiation.IsotropicRadiationSingleCoefficient;
import org.orekit.forces.radiation.SolarRadiationPressure;
import org.orekit.frames.FramesFactory;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;


/** 
 * Generates a Force Model compatible with SPOOK
 * @author Oscar Rodriguez Fernandez
 *
 */

public class PropagationForceModel {
	
	private List<ForceModel> Perturbations = new ArrayList<ForceModel>(); 
	
	public PropagationForceModel (Properties InputParams) throws OrekitException {
		/**
		 * Creates the force model compatible with SPOOK
		 */
		
		// Gravity Field
		
		// Read specific gravity model
		int GravModel = Integer.parseInt(InputParams.getProperty("Earth_gravity").trim());
		if (GravModel == 0 ) {
			System.out.println("Using two-body model for Earth gravity");
			this.Perturbations.add(new NewtonianAttraction(Constants.EGM96_EARTH_MU));
		} else {
			int Earth_gravity_degree = Integer.parseInt(InputParams.getProperty("Earth_gravity_order").trim());
			int Earth_gravity_order = Integer.parseInt(InputParams.getProperty("Earth_gravity_order").trim());
			if (GravModel ==1 ) {
				// EGM96 gravity model
				System.out.println("Using EGM96 gravity model");
				GravityFieldFactory.addPotentialCoefficientsReader(new ICGEMFormatReader("EGM96.gfc", false));
			} else if (GravModel ==2) {
				// GGM0C2C gravity model
				System.out.println("Using GGM02C gravity model");
				GravityFieldFactory.addPotentialCoefficientsReader(new ICGEMFormatReader("GGM02C.gfc", false));
			} else if (GravModel ==3 ) {
				// GGM0C2S gravity model
				System.out.println("Using GGM02S gravity model");
				GravityFieldFactory.addPotentialCoefficientsReader(new ICGEMFormatReader("GGM02S.gfc", false));
			}
			System.out.println("Degree: " +Earth_gravity_degree);
			System.out.println("Order: " + Earth_gravity_order);
 			this.Perturbations.add( new HolmesFeatherstoneAttractionModel(FramesFactory.getITRF(IERSConventions.IERS_2010, true), GravityFieldFactory.getNormalizedProvider(Earth_gravity_degree, Earth_gravity_order)));
		}
		
		// Sun Gravity
		int SunGravity = Integer.parseInt(InputParams.getProperty("Solar_gravity").trim());
		if (SunGravity == 1) {
			System.out.println("Solar Gravity ON");
			this.Perturbations.add(new ThirdBodyAttraction(CelestialBodyFactory.getSun()));
		}
		
		// Lunar Gravity
		int MoonGravity = Integer.parseInt(InputParams.getProperty("Lunar_gravity").trim());
		if (MoonGravity == 1) {
			System.out.println("Lunar Gravity ON");
			this.Perturbations.add(new ThirdBodyAttraction(CelestialBodyFactory.getMoon()));
		}
		// Sun radiation pressure
		// Frontal area set to 1.0 by default
		int SRP_switch = Integer.parseInt(InputParams.getProperty("Solar_Radiation_Pressure").trim());
		if (SRP_switch ==1) {
			double cross = Double.parseDouble(InputParams.getProperty("atoM"));
			// Radiation pressure coefficient
			double cr =  Double.parseDouble(InputParams.getProperty("CR"));;
			this.Perturbations.add( new SolarRadiationPressure( CelestialBodyFactory.getSun(), Constants.EGM96_EARTH_EQUATORIAL_RADIUS,
					new IsotropicRadiationSingleCoefficient(cross, cr)));
			System.out.println("Solar Radiation Pressure ON");
		}
		
		
		// Earth Radiation pressure NOT AVAILABLE
		
		// Planetary gravity
		int Planetary_grav = Integer.parseInt(InputParams.getProperty("Planet_gravity").trim());
		if (Planetary_grav ==1) {
			// 1
			if (Integer.parseInt(InputParams.getProperty("Mercury").trim()) ==1) {
				System.out.println("Mercury Gravity ON");
				this.Perturbations.add(new ThirdBodyAttraction(CelestialBodyFactory.getMercury()));
			}
			// 2
			if (Integer.parseInt(InputParams.getProperty("Venus").trim()) ==1) {
				System.out.println("Venus Gravity ON");
				this.Perturbations.add(new ThirdBodyAttraction(CelestialBodyFactory.getVenus()));
			}
			// 4
			if (Integer.parseInt(InputParams.getProperty("Mars").trim()) ==1) {
				System.out.println("Mars Gravity ON");
				this.Perturbations.add(new ThirdBodyAttraction(CelestialBodyFactory.getMars()));
			}
			// 5
			if (Integer.parseInt(InputParams.getProperty("Jupiter").trim()) ==1) {
				System.out.println("Jupiter Gravity ON");
				this.Perturbations.add(new ThirdBodyAttraction(CelestialBodyFactory.getJupiter()));
			}
			// 6
			if (Integer.parseInt(InputParams.getProperty("Saturn").trim()) ==1) {
				System.out.println("Saturn Gravity ON");
				this.Perturbations.add(new ThirdBodyAttraction(CelestialBodyFactory.getSaturn()));
			}
			// 7
			if (Integer.parseInt(InputParams.getProperty("Uranus").trim()) ==1) {
				System.out.println("Uranus Gravity ON");
				this.Perturbations.add(new ThirdBodyAttraction(CelestialBodyFactory.getUranus()));
			}
			// 8
			if (Integer.parseInt(InputParams.getProperty("Neptune").trim()) ==1) {
				System.out.println("Neptune Gravity ON");
				this.Perturbations.add(new ThirdBodyAttraction(CelestialBodyFactory.getNeptune()));
			}
			// 9
			if (Integer.parseInt(InputParams.getProperty("Pluto").trim()) ==1) {
				System.out.println("Pluto Gravity ON");
				this.Perturbations.add(new ThirdBodyAttraction(CelestialBodyFactory.getPluto()));
			}
		}
		
		
		// Drag 
		// Drag coefficient
		
		if (Integer.parseInt(InputParams.getProperty("Drag").trim()) ==1) {
		
			double CD = Double.parseDouble(InputParams.getProperty("CD"));
			double cross = Double.parseDouble(InputParams.getProperty("atoM"));
			
			// Earth shape
//			final BodyShape earth = 
//					new OneAxisEllipsoid(Constants.WGS84_EARTH_EQUATORIAL_RADIUS,
//										Constants.WGS84_EARTH_FLATTENING, 
//										FramesFactory.getITRF(IERSConventions.IERS_2010, false));
			
			// To construct the Atmosphere for the NRLMSISE00 we need to read input parameters
			// Not directly provided by orekit. Future development
		//		final Atmosphere Atm = NRLMSISE00(final NRLMSISE00InputParameters parameters,
		//                			          CelestialBodyFactory.getSun(),
		//                			          earth);
		
			final OneAxisEllipsoid Earth_ellipsoid = new OneAxisEllipsoid(Constants.WGS84_EARTH_EQUATORIAL_RADIUS,
																		 Constants.WGS84_EARTH_FLATTENING, 
																		 FramesFactory.getITRF(IERSConventions.IERS_2010, false));
			final Atmosphere Atm = new HarrisPriester(CelestialBodyFactory.getSun(), Earth_ellipsoid);
			
			
			System.out.println("Drag acceleration ON");
			System.out.println("Atmosphere Model HarrisPriester");
			this.Perturbations.add (new DragForce(Atm, new IsotropicDrag(cross, CD)));
		}
//		
		
		
	}
	

	public PropagationForceModel() throws OrekitException {
		this.DefaultForceModel();;
	}
	
	private void DefaultForceModel() throws OrekitException{
		
		// Only consider Two body perturbations of the Earth as default simple
		// Perturbation model
		this.Perturbations.add(new NewtonianAttraction(Constants.EGM96_EARTH_MU));
	}
	
	public List<ForceModel> getPerturbations() throws OrekitException {
		return this.Perturbations;
	}
	
	
	

}
