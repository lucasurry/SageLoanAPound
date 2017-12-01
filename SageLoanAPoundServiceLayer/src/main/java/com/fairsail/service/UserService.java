package com.fairsail.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.fairsail.accounts.Applicant;
import com.fairsail.accounts.CreditScore;
import com.fairsail.dao.DatabaseQueryExecutor;
import com.fairsail.dao.UserDao;
import com.fairsail.exceptions.NoResultsFoundException;

public class UserService {
	private UserDao userDao;
	
	public UserService(DatabaseQueryExecutor dqe) { 
		userDao = new UserDao(dqe);
	}
	
	public Applicant getApplicatInformation(int applicantId) throws SQLException, NoResultsFoundException {
		return userDao.selectApplicant(applicantId);	
	}
	
	public List<CreditScore> getApplicantCreditScores(int userId) throws SQLException {
		return userDao.selectUserCreditScores(userId);
	}
	
	public Date getLastCreditScoreUpdate(int userId) throws SQLException {
		return userDao.selectLastCreditCheckDate(userId);
	}
	
	public List<String> getApplicantNotes(int userId) throws SQLException{
		return userDao.selectApplicantNotes(userId);
	}
	
	public void updateApplicantCreditCheck(int userId, List<CreditScore> creditScores, List<String> notes) throws SQLException {
		userDao.updateUserCreditScore(userId, creditScores);
		userDao.insertNotesForApplicant(userId, notes);
		userDao.updateLastCreditCheckDate(userId);
	}
	
	
	public int chooseCreditScoreToUse(List<CreditScore> scores, String rule) {
		int creditScore = 0;
		
		switch(rule) {
			case "highest"	: creditScore = getHighestValue(scores);
							  break;
			case "lowest"	: creditScore = getLowestValue(scores);
							  break;
			case "average"	: creditScore = getAverageValue(scores);
							  break;
			default			: creditScore = getScoreFromSource(scores, rule);
		}
		
		return creditScore;
	}
	
	public int getHighestValue(List<CreditScore> scores) {
		int highest = 0;
		
		for(CreditScore creditScore : scores) {
			if(creditScore.getScore() > highest) {
				highest = creditScore.getScore();
			}
		}
		
		return highest;
	}
	
	public int getLowestValue(List<CreditScore> scores) {
		// Max credit score is 999 (I think) so set this to 1000
		int lowest = 1000;
		
		for(CreditScore creditScore : scores) {
			if(creditScore.getScore() < lowest) {
				lowest = creditScore.getScore();
			}
		}
		
		return lowest;
	}
	
	public int getAverageValue(List<CreditScore> scores) {
		int total = 0;
		
		for(CreditScore creditScore : scores) {
			total = total + creditScore.getScore();
		}

		return (total/scores.size());
	}
	
	public int getScoreFromSource(List<CreditScore> scores, String source) {
		for(CreditScore creditScore : scores) {
			if(creditScore.getSource().equals(source)) {
				return creditScore.getScore();
			}
		}
		
		return 0;
	}
}
