package com.fairsail.loan;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;

public class SecuredLoanTest {

	private SecuredLoan sLoan;
	
	final private double lowValue = 100.12;
	final private double highValue = 100.17;
	
	@Before
	public void setUp() {
		sLoan = new SecuredLoan();
	}
	
	@Test
	public void validateSetMinimumDepositPercent() {
		sLoan.setMinDepositIsPercent(true);
		sLoan.setMinimumDeposit(lowValue);
		assertThat(sLoan.getMinimumDeposit(), is(not(100.12)));
		assertThat(sLoan.getMinimumDeposit(), is(100.1));
		
		sLoan.setMinimumDeposit(highValue);
		assertThat(sLoan.getMinimumDeposit(), is(not(100.17)));
		assertThat(sLoan.getMinimumDeposit(), is(100.2));
	}
	
	@Test
	public void validateSetMinimumDepositValue() {
		sLoan.setMinDepositIsPercent(false);
		sLoan.setMinimumDeposit(lowValue);
		assertThat(sLoan.getMinimumDeposit(), is(100.12));
		assertThat(sLoan.getMinimumDeposit(), is(not(100.1)));
		
		sLoan.setMinimumDeposit(highValue);
		assertThat(sLoan.getMinimumDeposit(), is(100.17));
		assertThat(sLoan.getMinimumDeposit(), is(not(100.2)));
	}

}
