package org.SatProp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.orekit.errors.OrekitException;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;

/*
 * Implements methods to handle dates
 */
public class DateUtil {

	/*
	 * Method that text is a string contains a date in either a isot
	 * or JD formats
	 */
	public static boolean isaDate (String date_text){
		Pattern isot= Pattern.compile(Patterns.isot);
		Pattern JD_pattern = Pattern.compile("\\s*"+Patterns.double_num+"\\s*");
		
		Matcher isot_match = isot.matcher(date_text);
		Matcher JD_match = JD_pattern.matcher(date_text);
		
		return (isot_match.find()||JD_match.find());
	}
	
	/*
	 * Method that tries to extract a date contained in a string as an absoluteDate
	 * from orekit
	 */
	public static AbsoluteDate readDate(String dateString) throws OrekitException {
		//Reads a string that can be either JD or IsoT format and
		// returns the date
		Pattern isot= Pattern.compile("\\s*([0-9]{4})-([0-9]{2})-([0-9]{2})T([0-9]{2}):([0-9]{2}):([0-9]{2}\\.?[0-9]+)\\.*");
		Pattern JD_pattern = Pattern.compile("\\s*([-]?[0-9]*\\.?[0-9]+)\\s*");
		AbsoluteDate date = new AbsoluteDate();
		TimeScale UTC = TimeScalesFactory.getUTC();
		
		Matcher isot_match = isot.matcher(dateString);
		Matcher JD_match = JD_pattern.matcher(dateString);
		
		if (isot_match.find()) {
			// create an absolute date
			int year = Integer.parseInt(isot_match.group(1));
			int month = Integer.parseInt(isot_match.group(2));
			int day = Integer.parseInt(isot_match.group(3));
			int hour = Integer.parseInt(isot_match.group(4));
			int min = Integer.parseInt(isot_match.group(5));
			double sec = Double.parseDouble(isot_match.group(6));
			date = new  AbsoluteDate(year, month, day, hour, min, sec, UTC);					
		} else if (JD_match.find()) {
			// If format is not isot it must be JD
			double JD = Double.parseDouble(JD_match.group(1));
			double MJD = JD - 2400000.5;
			int MJD_int =  (int) Math.floor(MJD);
			double secs = MJD - (double) MJD_int;
			date = AbsoluteDate.createMJDDate(MJD_int, secs, UTC);
		} else {
			System.out.println("Error passing date");
		}
		return date;
	}
}
