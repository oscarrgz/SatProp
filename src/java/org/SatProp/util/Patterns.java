package org.SatProp.util;

public interface Patterns {
	/*
	 * Enumerates different patterns
	 */
	String isot = "\\s*([0-9]{4})-([0-9]{2})-([0-9]{2})T([0-9]{2}):([0-9]{2}):([0-9]{2}(?:\\.[0-9]*)?)\\.*";
	String double_num = "[+-]?[0-9]+(?:\\.[0-9]*)?";
	String int_num = "[+-]?[0-9]\\s+";

}
