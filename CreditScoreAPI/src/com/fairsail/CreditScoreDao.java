package com.fairsail;

import java.util.HashMap;
import java.util.Map;

public class CreditScoreDao {

	private Map<String, CreditScore> creditScores = new HashMap<String, CreditScore>();
	private Map<String, CreditScore> creditScores2 = new HashMap<String, CreditScore>();
	
	public CreditScoreDao() {
		// Add mock values for credit scores 1
		mockCreditScores();
		
		// Add mock values for credit scores 2
		mockCreditScores2();
	}
	
	private void mockCreditScores() {
		CreditScore cs = new CreditScore();
		cs.setCreditScore(451);
		cs.addNote(new Note("Late repayment to bank1 on loan"));
		cs.addNote(new Note("Credit score check run by bank2"));
		cs.addNote(new Note("Rejected for mortgage from bank2"));
		
		creditScores.put("applicant", cs);
		
		cs = new CreditScore();
		cs.setCreditScore(627);
		cs.addNote(new Note("Credit score check run by bank3"));
		
		creditScores.put("applicant_cs", cs);
	}
	
	private void mockCreditScores2() {
		CreditScore cs = new CreditScore();
		cs.setCreditScore(473);
		cs.addNote(new Note("Credit score check run by bank2"));
		cs.addNote(new Note("Rejected for mortgage from bank2"));
		
		creditScores2.put("applicant", cs);
		
		cs = new CreditScore();
		cs.setCreditScore(642);
		cs.addNote(new Note("Credit score check run by bank3"));
		cs.addNote(new Note("SuccessfulApplication for loan with bank3"));
		
		creditScores2.put("applicant_cs", cs);
	}
	
	public CreditScore getCreditScore(String userName) {
		return creditScores.get(userName);
	}
	
	public CreditScore getCreditScore2(String userName) {
		return creditScores2.get(userName);
	}
}
