/**
 * 
 */
package org.SatProp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that validates different inputs
 * 
 * @author oscar
 *
 */
public class Validator {

	/**
	 * Check if text contains double
	 * @param text
	 * @return boolean
	 */
	public static boolean isDouble(String text) {
		if (text != null && text.length() != 0) {
			try {
				Double.parseDouble(text);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		} else {
			return false;
		}
	}

	public static boolean isPositiveDouble(String text) {
		if (isDouble(text)) {
			return Double.parseDouble(text) > 0;
		} else {
			return false;
		}
	}

	public static boolean isInt(String text) {
		if (text != null && text.length() != 0) {
			try {
				Integer.parseInt(text);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		} else {
			return false;
		}
	}

	public static boolean isPositiveInt(String text) {
		if (isInt(text)) {
			return Double.parseDouble(text) > 0;
		} else {
			return false;
		}
	}
	
	/*
	 * Check if text contains angle in degrees between 0 and 360
	 * @param text
	 * @return boolean
	 */
	public static boolean isAngleDeg(String text) {
		if (isDouble(text)) {
			final Double angle = Double.parseDouble(text);
			return (angle >=0.0&& angle <=360.0);
		} else {
			return false;
		}
		
	}
	
	public static boolean isDoublebetween(String text, Double maxVal, Double minVal) {
		if (isDouble(text)) {
			final Double angle = Double.parseDouble(text);
			return (angle >=minVal && angle <=maxVal);
		} else {
			return false;
		}
	}
	public static boolean is3DVecor (String text){
		Pattern Vector3Dpatt = Pattern.compile("\\s*"+Patterns.double_num+"\\s+" +Patterns.double_num+"\\s+"+Patterns.double_num+"\\s*" );
		if (text == null || text.length() == 0)  {
			return false;
		} else {
			Matcher aVector = Vector3Dpatt.matcher(text); 	
			if (aVector.find()) {
				return true;
			} else {
				return false;
			}
		}
	}
	
}
