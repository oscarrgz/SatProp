package org.SatProp.util;

import java.util.regex.Pattern;

public interface Patterns {
	/*
	 * Enumerates different patterns
	 */
	final String isot = "\\s*([0-9]{4})-([0-9]{2})-([0-9]{2})T([0-9]{2}):([0-9]{2}):([0-9]{2}(?:\\.[0-9]*)?)\\.*";
	final String double_num = "[+-]?[0-9]+(?:\\.[0-9]*)?";
	final String int_num = "[+-]?[0-9]\\s+";
	final String TLE_0 = "0 \\S*";
	
	final String TLE_1 = "1 [ 0-9]{5}[A-Z] [ 0-9]{5}[ A-Z]{3} [ 0-9]{5}[.][ 0-9]{8} (?:(?:[ 0+-][.][ 0-9]{8})|(?: [ +-][.][ 0-9]{7})) " +
            "[ +-][ 0-9]{5}[+-][ 0-9] [ +-][ 0-9]{5}[+-][ 0-9] [ 0-9] [ 0-9]{4}[ 0-9]";
	final String TLE_2 =  "2 [ 0-9]{5} [ 0-9]{3}[.][ 0-9]{4} [ 0-9]{3}[.][ 0-9]{4} [ 0-9]{7} " +
            "[ 0-9]{3}[.][ 0-9]{4} [ 0-9]{3}[.][ 0-9]{4} [ 0-9]{2}[.][ 0-9]{13}[ 0-9]";
}
