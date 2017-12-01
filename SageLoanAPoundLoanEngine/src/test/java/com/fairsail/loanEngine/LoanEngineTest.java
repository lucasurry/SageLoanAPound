package com.fairsail.loanEngine;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.fairsail.accounts.Applicant;
import com.fairsail.accounts.CreditScore;

public class LoanEngineTest {

	private Applicant applicant;
	private LoanEngine le;
	private List<CreditScore> creditScores;
	
	@Before
	public void setUp() throws Exception{
		le = new LoanEngine();
		le.initalize();
		creditScores = new ArrayList<CreditScore>();
	}
	
	@Test
	public void test() throws SQLException, IOException, ParserConfigurationException, SAXException {	
		applicant = new Applicant(2,"applicant", null);
		
		CreditScore cs = new CreditScore();
		cs.setScore(400);
		cs.setSource("creditsource");
		creditScores.add(cs);
		
		cs = new CreditScore();
		cs.setScore(500);
		cs.setSource("creditsource2");
		creditScores.add(cs);
		
		applicant.addCreditScores(creditScores);
		applicant.setLastCSUpdate(new Date());
		
		boolean isValid;
		
		isValid = le.checkCreditScore(applicant, 350, "lowest");
		assertTrue(isValid);
		
		isValid = le.checkCreditScore(applicant, 400, "lowest");
		assertTrue(isValid);		
	}

}
