package com.fairsail.applications;

import com.fairsail.accounts.Applicant;

public class Application {
	private Applicant applicant;
	private int applicantId;
	private int applicationId;
	private double loanValue = 0;
	private int creditScoreUsed = 0;
	private String applicationStatus = "U";

	public Application() {}

	public Applicant getApplicant() {
		return applicant;
	}

	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}

	public int getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(int applicantId) {
		this.applicantId = applicantId;
	}

	public int getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}

	public double getLoanValue() {
		return loanValue;
	}

	public void setLoanValue(double loanValue) {
		this.loanValue = loanValue;
	}

	public int getCreditScoreUsed() {
		return creditScoreUsed;
	}

	public void setCreditScoreUsed(int creditScoreUsed) {
		this.creditScoreUsed = creditScoreUsed;
	}

	public String getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}
	
	
}
