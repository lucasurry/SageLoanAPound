package com.fairsail.applications;

import com.fairsail.accounts.Applicant;

public class PersonalLoanApplication extends Application {
	private int loanId;
	
	public PersonalLoanApplication() {}
	
	public PersonalLoanApplication(Applicant applicant, int loanId) {
		setApplicant(applicant);
		this.loanId = loanId;
	}
	public int getLoanId() {
		return loanId;
	}

	public void setLoanId(int loanId) {
		this.loanId = loanId;
	}
}
