package com.fairsail.service;

import java.sql.SQLException;
import java.util.Map;

import com.fairsail.dao.DatabaseQueryExecutor;
import com.fairsail.dao.MortgageDao;
import com.fairsail.exceptions.NoResultsFoundException;
import com.fairsail.loan.Mortgage;

public class MortgageService {

	private MortgageDao mortgageDao;
	
	public MortgageService(DatabaseQueryExecutor dqe) 
	{
		mortgageDao = new MortgageDao(dqe);
	}
	
	public MortgageDao getMortgageDao() 
	{
		return mortgageDao;
	}
	
	public Map<Integer, Mortgage> getMortgages() throws SQLException, NoResultsFoundException
	{
		return mortgageDao.viewAllMortgages();
	}
	
	public Mortgage getMortgageById(int mortgageId) throws SQLException, NoResultsFoundException {
		return mortgageDao.getMortgageById(mortgageId);
	}

	public void createMortgage(Mortgage mortgage) throws SQLException 
	{
		mortgageDao.insertMortgage(mortgage);	
	}
	
	public void deleteMortgage(int mortgageId) throws SQLException 
	{
		mortgageDao.deleteMortgage(mortgageId);
	}
	
}
