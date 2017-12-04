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
	
	/**
	 * Data access object which interfaces with the database and the email client to update information about users
	 * 
	 * @param DatabaseQueryExecutor
	 */
	public UserService(DatabaseQueryExecutor dqe) { 
		userDao = new UserDao(dqe);
	}
	
	/**
	 * Returns an applicant found by id from the database
	 * 
	 * @param int applicant id
	 * 
	 * @return Applicant
	 * 
	 * @throws SQLException
	 * @throws NoResultsFoundException
	 */
	public Applicant getApplicatInformation(int applicantId) throws SQLException, NoResultsFoundException {
		return userDao.selectApplicant(applicantId);	
	}
	
	/**
	 * Returns a list of credit scores for a given applicant 
	 * 
	 * @param int user id
	 * 
	 * @return List<CreditScore>
	 * 
	 * @throws SQLException
	 */
	public List<CreditScore> getApplicantCreditScores(int userId) throws SQLException {
		return userDao.selectUserCreditScores(userId);
	}
	
	/**
	 * Returns the date of the last credit score update for a given applicant
	 * 	
	 * @param int user id
	 * 
	 * @return Date last credit score update
	 * 
	 * @throws SQLException
	 */
	public Date getLastCreditScoreUpdate(int userId) throws SQLException {
		return userDao.selectLastCreditCheckDate(userId);
	}
	
	/**
	 * Returns the notes on the account of a given applicant
	 * 
	 * @param int user id
	 * 
	 * @return List<String> notes on the applicants account
	 * 
	 * @throws SQLException
	 */
	public List<String> getApplicantNotes(int userId) throws SQLException{
		return userDao.selectApplicantNotes(userId);
	}
	
	/**
	 * Updates the users credit scores, notes and updates the last credit score update date
	 * 
	 * @param int user id
	 * @param List<CreditScore> list of users credit scores
	 * @param List<String> notes on users account
	 * 
	 * @throws SQLException
	 */
	public void updateApplicantCreditCheck(int userId, List<CreditScore> creditScores, List<String> notes) throws SQLException {
		userDao.updateUserCreditScore(userId, creditScores);
		userDao.insertNotesForApplicant(userId, notes);
		userDao.updateLastCreditCheckDate(userId);
	}
	
	/**
	 * Given a list of credit scores and a rule this will return the credit score we want to use for an application
	 * 
	 * @param List<CreditScore> scores
	 * @param String rule
	 * 
	 * @return int credit score value to use
	 */
	public int chooseCreditScoreToUse(List<CreditScore> scores, String rule) {
		int creditScore = 0;
		
		// Figure out which credit score we should be using
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
	
	/**
	 * Takes a list of credit scores and returns the highest value
	 * 
	 * @param List<CreditScore> scores
	 * 
	 * @return int highest credit score
	 */
	public int getHighestValue(List<CreditScore> scores) {
		int highest = 0;
		
		for(CreditScore creditScore : scores) {
			if(creditScore.getScore() > highest) {
				highest = creditScore.getScore();
			}
		}
		
		return highest;
	}
	
	/**
	 * Takes a list of credit scores and returns the lowest value
	 * 
	 * @param List<CreditScore> scores
	 * 
	 * @return int lowest credit score
	 */
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
	
	/**
	 * Takes a list of credit scores and returns the average value
	 * 
	 * @param List<CreditScore> scores
	 * 
	 * @return int average credit score
	 */
	public int getAverageValue(List<CreditScore> scores) {
		int total = 0;
		
		for(CreditScore creditScore : scores) {
			total = total + creditScore.getScore();
		}

		return (total/scores.size());
	}
	
	/**
	 * Takes a list of credit scores and returns the score form a selected source
	 * 
	 * @param List<CreditScore> scores
	 * @param String source of credit score
	 *  
	 * @return int score from set source
	 */
	public int getScoreFromSource(List<CreditScore> scores, String source) {
		for(CreditScore creditScore : scores) {
			if(creditScore.getSource().equals(source)) {
				return creditScore.getScore();
			}
		}
		
		return 0;
	}
}
