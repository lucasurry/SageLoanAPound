package com.fairsail.loan;

/**
 * A mortgage is a type of secured loan used by customers buying houses, where the house is used to offset the loan.
 * 
 * There are usually extra fees for processing the loan and there can be offers for first time buyers to incentivise them to buy a house
 *  
 * @author Lucas
 *
 */
public class Mortgage extends SecuredLoan{
	
	private double fees = 0.00;
	private boolean firstTimeBuyerOnly = false;

	public Mortgage() {}

	public double getFees() {
		return fees;
	}
	public void setFees(double fees) {
		this.fees = fees;
	}
	public boolean isFirstTimeBuyerOnly() {
		return firstTimeBuyerOnly;
	}
	public void setFirstTimeBuyerOnly(boolean firstTimeBuyerOnly) {
		this.firstTimeBuyerOnly = firstTimeBuyerOnly;
	}
}
