package com.fairsail.loanEngine;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.fairsail.accounts.Applicant;
import com.fairsail.accounts.CreditScore;

/**
 * I have not mocked up the responses so these work fine if the API is running but they fail if it is not running.
 * 
 * Given a bit more time I would rewrite the classa a bit so I could feed in some mock objects and test it properly (not really used API calls before
 * so it would take a bit of working out).
 * 
 * May have to turn these off to make the build work!
 * 
 * @author Lucas
 *
 */
public class CreditScoreFetcherTest {

	private Applicant applicant; 
	private CreditScoreFetcher csFetcher;
	private CreditScore cs;
	
	@Before
	public void setUp() throws Exception{
		csFetcher = new CreditScoreFetcher();
		cs = new CreditScore();
	}
	
	@Test
	public void fetchUser2CreditSiteTest() throws IOException, ParserConfigurationException, SAXException {
		applicant = new Applicant(2, "applicant", null);
		
		cs = csFetcher.getCreditScore(applicant, "creditsite");
		
		assertThat(cs.getScore(), is(451));
		assertThat(cs.getSource(), is("creditsite"));
		assertThat(applicant.getNotes(), hasSize(3));
	}
	
	@Test
	public void fetchUser2CreditSite2Test() throws IOException, ParserConfigurationException, SAXException {
		applicant = new Applicant(2, "applicant", null);
		
		cs = csFetcher.getCreditScore(applicant, "creditsite2");
		
		assertThat(cs.getScore(), is(473));
		assertThat(cs.getSource(), is("creditsite2"));
		assertThat(applicant.getNotes(), hasSize(2));
	}

	@Test
	public void fetchUser3CreditSiteTest() throws IOException, ParserConfigurationException, SAXException {
		applicant = new Applicant(3, "applicant_cs", null);
		
		cs = csFetcher.getCreditScore(applicant, "creditsite");
		
		assertThat(cs.getScore(), is(627));
		assertThat(cs.getSource(), is("creditsite"));
		assertThat(applicant.getNotes(), hasSize(1));
	}
	
	@Test
	public void fetchUser3CreditSite2Test() throws IOException, ParserConfigurationException, SAXException {
		applicant = new Applicant(3, "applicant_cs", null);
		
		cs = csFetcher.getCreditScore(applicant, "creditsite2");
		
		assertThat(cs.getScore(), is(642));
		assertThat(cs.getSource(), is("creditsite2"));
		assertThat(applicant.getNotes(), hasSize(2));
	}
}
