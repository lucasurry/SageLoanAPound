package com.fairsail.accounts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Applicant extends User {
	
	private String emailAddress;
	private List<CreditScore> creditScores;
	private Date lastCSUpdate;
	private List<String> notes = new ArrayList<String>();
	
	// Use this if the credit scores have not been fetched
	public Applicant(int userId, String name, String emailAddress) {
		super(userId, name);
		
		this.emailAddress = emailAddress;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public List<CreditScore> getCreditScores() {
		return creditScores;
	}

	public void addCreditScores(List<CreditScore> creditScores) {
		// Replace the whole list, always do this so we don't end up with duplicats sources or stale scores
		this.creditScores = creditScores;
	}

	public void setLastCSUpdate(Date lastUpdate) {
		this.lastCSUpdate = lastUpdate;
	}
	
	public Date getLastCSUpdate() {
		return lastCSUpdate;
	}
	
	public List<String> getNotes(){
		return notes;
	}
	
	public void addNote(String note) {
		notes.add(note);
	}

}
