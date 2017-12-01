package com.fairsail.utils;

import java.text.DecimalFormat;

public class GlobalSettings {

	// Wouldn't store this here really but ok for now!
	public static final String ENCKEY = "hampster";
	
	// Formats for percentages and money values
	private static final DecimalFormat PERCENTAGEDECIMALFORMAT = new DecimalFormat("#.0"); 
	private static final DecimalFormat VALUEDECIMALFORMAT = new DecimalFormat("#.00"); 
	
	/**
	 * Formats a percentage value so it displays to 1 decimal place
	 * 
	 * @param percentage
	 * @return formatted double in the format #.0
	 */
	public static double formatPercentage(double percentage) {
		return Double.parseDouble(PERCENTAGEDECIMALFORMAT.format(percentage));
	}
	
	/**
	 * Formats a monetary value to the nearest full minor currency (pence in this case)
	 * 
	 * @param value
	 * @return formatted double in the format #.00
	 */
	public static double formatValue(double value) {
		return Double.parseDouble(VALUEDECIMALFORMAT.format(value));
	}
}
