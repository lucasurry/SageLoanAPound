package com.fairsail.applications;

public class MortgageApplication extends Application{
	
	private int mortgageId = 0;
	private double depositAmount = 0;
	private boolean isFirstTimeBuyer = false;
	
	public MortgageApplication() {}
	
	public MortgageApplication(int applicantId, int mortgageId) {
		this.setApplicantId(applicantId);
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
