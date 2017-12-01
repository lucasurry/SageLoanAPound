package com.fairsail.accounts;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class ApplicantTest {
	
	@Test
	public void verifyAddNote() {
		int userId = 1;
		String name = "applicant";
		String email = "test@email.com";		
		
		Applicant applicant = new Applicant(userId, name, email);
		
		String note = "test";
		
		applicant.addNote(note);
		assertThat(applicant.getNotes(), hasSize(1));
		
		applicant.addNote(note);
		assertThat(applicant.getNotes(), hasSize(2));
	}
}
