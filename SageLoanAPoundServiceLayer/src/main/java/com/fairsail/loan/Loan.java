package com.fairsail.loan;

/**
 * The base loan object which all other loans are based off of
 * 
 * Stores information common to all loans - lender, minimum credit score, offers for credit score ranges, repayment length etc.
 * 
 * @author Lucas
 *
 */

public class Loan {
	private String lender = null;
	private double minimumValue = 0.00;
	private double maximumValue = 0.00;
	private double interestRate = 0.0;
	private int repaymentMonths = 0;
	private int minimumCreditScore = 0;
	private String creditScoreRule = null;
	
	public Loan() {}

	public String getLender() {
		return lender;
	}

	public void setLender(String lender) {
		this.lender = lender;
	}

	public String getCreditScoreRule() {
		return creditScoreRule;
	}

	public void setCreditScoreRule(String creditScoreRule) {
		this.creditScoreRule = creditScoreRule;
	}

	public double getMinimumValue() {
		return minimumValue;
	}
	
	public void setMinimumValue(double minimumValue) {
		this.minimumValue = minimumValue;
	}
	
	public double getMaximumValue() {
		return maximumValue;
	}
	
	public void setMaximumValue(double maximumValue) {
		this.maximumValue = maximumValue;
	}
	
	public double getInterestRate() {
		return interestRate;
	}
	
	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	public int getRepaymentMonths() {
		return repaymentMonths;
	}

	public void setRepaymentMonths(int repaymentMonths) {
		this.repaymentMonths = repaymentMonths;
	}

	public int getMinimumCreditScore() {
		return minimumCreditScore;
	}

	public void setMinimumCreditScore(int minimumCreditScore) {
		this.minimumCreditScore = minimumCreditScore;
	}
}
