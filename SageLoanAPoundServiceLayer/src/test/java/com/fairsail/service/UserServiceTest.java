package com.fairsail.service;

import org.junit.Before;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;

import org.junit.Test;

import com.fairsail.accounts.CreditScore;

public class UserServiceTest {

	private UserService uService;
	private List<CreditScore> creditScores;
	private CreditScore cs;
	
	private int value = 0;
	
	final private int lowest = 100;
	final private int middle = 350;
	final private int highest = 700;
	final private int average = 383;
	
	final private String source1 = "source1";
	final private String source2 = "source2";
	final private String source3 = "source3";
	
	@Before
	public void setUp() {
		uService = new UserService(null);
		creditScores = new ArrayList<CreditScore>();		
		
		cs = new CreditScore();
		cs.setScore(lowest);
		cs.setSource(source1);
		creditScores.add(cs);
		
		cs = new CreditScore();
		cs.setScore(middle);
		cs.setSource(source2);
		creditScores.add(cs);
		
		cs = new CreditScore();
		cs.setScore(highest);
		cs.setSource(source3);
		creditScores.add(cs);
		
		// Reset before every test!
		value = 0;
	}
	
	@Test
	public void validateGetHighestValue() {
		value = uService.getHighestValue(creditScores);
		assertThat(value, is(highest));
	}
	
	@Test
	public void validateGetLowestValue() {
		value = uService.getLowestValue(creditScores);
		assertThat(value, is(lowest));
	}

	@Test
	public void validateGetAverageValue() {
		value = uService.getAverageValue(creditScores);
		assertThat(value, is(average));
	}
	
	@Test
	public void validateGetScoreFromSource() {	
		value = uService.getScoreFromSource(creditScores,source1);
		assertThat(value, is(lowest));
		
		value = 0;
		value = uService.getScoreFromSource(creditScores,source2);
		assertThat(value, is(middle));
		
		value = 0;
		value = uService.getScoreFromSource(creditScores,source3);
		assertThat(value, is(highest));
	}
	
	@Test
	public void validateChooseCreditScoreToUse() {
		value = uService.chooseCreditScoreToUse(creditScores, "highest");
		assertThat(value, is(highest));
		
		value = 0;
		value = uService.chooseCreditScoreToUse(creditScores, "lowest");
		assertThat(value, is(lowest));
		
		value = 0;
		value = uService.chooseCreditScoreToUse(creditScores, "average");
		assertThat(value, is(average));
		
		value = 0;
		value = uService.chooseCreditScoreToUse(creditScores, source1);
		assertThat(value, is(lowest));
		
		value = 0;
		value = uService.chooseCreditScoreToUse(creditScores, source2);
		assertThat(value, is(middle));
		
		value = 0;
		value = uService.chooseCreditScoreToUse(creditScores, source3);
		assertThat(value, is(highest));
	}
	
}
