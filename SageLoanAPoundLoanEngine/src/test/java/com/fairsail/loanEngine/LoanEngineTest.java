package com.fairsail.loanEngine;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

/**
 * Test everything which does not need to connect to the API or a database as I can't guarantee either will be set up
 * and I am not sure how to mock either
 * 
 * @author Lucas
 *
 */
public class LoanEngineTest {

	final double deposit1 = 10.0;
	final double deposit2 = 1000.0;
	
	final double value1 = 1000.0;
	final double value2 = 500.0;
	final double value3 = 2000.0;
	final double value4 = 100.0;
	final double value5 = 10000.0;
	
	final double minimum = 500.0;
	final double maximum = 2000.0;
	
	final double minPercent = 10.0;	
	
	final private LoanEngine le = new LoanEngine();
	
	@Test
	public void validateCheckLoanAmount(){	
		// Check that the correct responses are given for each value
		// Value in the middle
		assertThat(le.checkLoanAmount(value1, minimum, maximum), is(true));
		// Value at bottom end of the range
		assertThat(le.checkLoanAmount(value2, minimum, maximum), is(true));
		// Value at the top end of the range
		assertThat(le.checkLoanAmount(value3, minimum, maximum), is(true));
		// Value below the range
		assertThat(le.checkLoanAmount(value4, minimum, maximum), is(false));
		// Value above the range
		assertThat(le.checkLoanAmount(value5, minimum, maximum), is(false));
	}
	
	@Test
	public void validateCheckDepositAmount(){	
		// Check that the correct responses are given for each value
		
		// Deposit is a percentage of loan value		
		// Deposit should be too low
		assertThat(le.checkDepositAmount(deposit1, value1, minPercent, true), is(false));
		// Deposit should be fine
		assertThat(le.checkDepositAmount(deposit2, value1, minPercent, true), is(true));
		
		// Fixed deposit value
		// Deposit should be too low
		assertThat(le.checkDepositAmount(deposit1, value1, minimum, false), is(false));
		// Deposit should be fine
		assertThat(le.checkDepositAmount(deposit2, value1, minimum, false), is(true));
	}
	
	@Test
	public void validateCheckFirstTimeBuyerStatus(){	
		// Check that the correct responses are given for each value
		
		// Is a first time buyer, should always pass
		assertThat(le.checkFirstTimeBuyerStatus(true, true), is(true));
		assertThat(le.checkFirstTimeBuyerStatus(true, false), is(true));
		
		// If not a first time buyer should only be true if the product is not only for first time buyers
		assertThat(le.checkFirstTimeBuyerStatus(false, true), is(false));
		assertThat(le.checkFirstTimeBuyerStatus(false, false), is(true));
	}
}
