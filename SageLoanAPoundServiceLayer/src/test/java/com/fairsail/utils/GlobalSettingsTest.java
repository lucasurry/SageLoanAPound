package com.fairsail.utils;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class GlobalSettingsTest {

	final private double highValue = 12345.6789;
	final private double lowValue = 12345.6234;
	
	@Test
	public void validateFormatPercentage() {
		double result = 0;
		result = GlobalSettings.formatPercentage(highValue);
		assertThat(result, is(12345.7));
		
		result = 0;
		result = GlobalSettings.formatPercentage(lowValue);
		assertThat(result, is(12345.6));

	}
	
	@Test
	public void validateFormatValue() {
		double result = 0;
		result = GlobalSettings.formatValue(highValue);
		assertThat(result, is(12345.68));

		result = 0;
		result = GlobalSettings.formatValue(lowValue);
		assertThat(result, is(12345.62));

	}

}
