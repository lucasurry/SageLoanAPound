package com.fairsail.applications;

public class PersonalLoanApplication extends Application {
	private int loanId;
	
	public PersonalLoanApplication() {}
	
	public PersonalLoanApplication(int applicantId, int loanId) {
		this.setApplicantId(applicantId);
		this.loanId = loanId;
	}
	public int getLoanId() {
		return loanId;
	}

	public void setLoanId(int loanId) {
		this.loanId = loanId;
	}
}
