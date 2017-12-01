package com.fairsail.applications;

import com.fairsail.accounts.Applicant;

public class MortgageApplication extends Application{
	
	private int mortgageId = 0;
	private double depositAmount = 0;
	private boolean isFirstTimeBuyer = false;
	
	public MortgageApplication() {}
	
	public MortgageApplication(Applicant applicant, int mortgageId) {
		setApplicant(applicant);
		this.mortgageId = mortgageId;
	}

	public int getMortgageId() {
		return mortgageId;
	}

	public void setMortgageId(int mortgageId) {
		this.mortgageId = mortgageId;
	}

	public double getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(double depositAmount) {
		this.depositAmount = depositAmount;
	}

	public boolean isFirstTimeBuyer() {
		return isFirstTimeBuyer;
	}

	public void setFirstTimeBuyer(boolean isFirstTimeBuyer) {
		this.isFirstTimeBuyer = isFirstTimeBuyer;
	}
	
	
}
