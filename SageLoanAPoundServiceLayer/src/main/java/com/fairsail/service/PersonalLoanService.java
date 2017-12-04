package com.fairsail.service;

import java.sql.SQLException;
import java.util.Map;

import com.fairsail.dao.DatabaseQueryExecutor;
import com.fairsail.dao.PersonalLoanDao;
import com.fairsail.exceptions.NoResultsFoundException;
import com.fairsail.loan.PersonalLoan;

public class PersonalLoanService {
	
	private PersonalLoanDao personalLoanDao;
	
	/**
	 * Data access object which interfaces with the database and the email client to update information about personal loans
	 * 
	 * @param DatabaseQueryExecutor
	 */
	public PersonalLoanService(DatabaseQueryExecutor dqe) 
	{
		personalLoanDao = new PersonalLoanDao(dqe);
	}	
	
	/**
	 * Get a list of all personal loans
	 * 
	 * @return Map<Integer, PersonalLoan> map of personal loans with the key being the personal loan id
	 * 
	 * @throws SQLException
	 * @throws NoResultsFoundException
	 */
	public Map<Integer, PersonalLoan> getPersonalLoans() throws SQLException, NoResultsFoundException
	{	
		return personalLoanDao.viewAllPersonalLoans();
	}
	
	/**
	 * Get a personal loan based on the id
	 * 
	 * @param int loan id
	 * 
	 * @return PersonalLoan
	 * 
	 * @throws SQLException
	 * @throws NoResultsFoundException
	 */
	public PersonalLoan getPersonalLoanById(int loanId) throws SQLException, NoResultsFoundException {
		return personalLoanDao.getPersonalLoanById(loanId);
	}
	
	/**
	 * Create a new personal loan in the personal loans table in the database
	 * 
	 * @param PersonalLoan
	 * 
	 * @throws SQLException
	 */
	public void createPersonalLoan(PersonalLoan personalLoan) throws SQLException 
	{
		personalLoanDao.insertPersonalLoan(personalLoan);	
	}
	
	/**
	 * Delete a personal loan from the personal loans table in the database based on the personal loan id
	 * 
	 * @param int personal loan id
	 * 
	 * @throws SQLException
	 */
	public void deletePersonalLoan(int personalLoanId) throws SQLException 
	{
		personalLoanDao.deletePersonalLoan(personalLoanId);
	}
}
