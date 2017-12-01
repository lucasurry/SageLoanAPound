package com.fairsail.service;

import java.sql.SQLException;
import java.util.Map;

import com.fairsail.dao.DatabaseQueryExecutor;
import com.fairsail.dao.PersonalLoanDao;
import com.fairsail.exceptions.NoResultsFoundException;
import com.fairsail.loan.PersonalLoan;

public class PersonalLoanService {
	
	private PersonalLoanDao personalLoanDao;
	
	public PersonalLoanService(DatabaseQueryExecutor dqe) 
	{
		personalLoanDao = new PersonalLoanDao(dqe);
	}	
	
	public PersonalLoanDao getPersonalLoanDao() 
	{
		return personalLoanDao;
	}
	
	public Map<Integer, PersonalLoan> getPersonalLoans() throws SQLException, NoResultsFoundException
	{	
		return personalLoanDao.viewAllPersonalLoans();
	}
	
	public PersonalLoan getPersonalLoanById(int loanId) throws SQLException, NoResultsFoundException {
		return personalLoanDao.getPersonalLoanById(loanId);
	}
	
	public void createPersonalLoan(PersonalLoan personalLoan) throws SQLException 
	{
		personalLoanDao.insertPersonalLoan(personalLoan);	
	}
	
	public void deletePersonalLoan(int personalLoanId) throws SQLException 
	{
		personalLoanDao.deletePersonalLoan(personalLoanId);
	}
}
