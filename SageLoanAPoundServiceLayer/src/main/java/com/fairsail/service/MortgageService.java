package com.fairsail.service;

import java.sql.SQLException;
import java.util.Map;

import com.fairsail.dao.DatabaseQueryExecutor;
import com.fairsail.dao.MortgageDao;
import com.fairsail.exceptions.NoResultsFoundException;
import com.fairsail.loan.Mortgage;

public class MortgageService {

	private MortgageDao mortgageDao;
	
	/**
	 * Data access object which interfaces with the database and the email client to update information about mortgages
	 * 
	 * @param DatabaseQueryExecutor
	 */
	public MortgageService(DatabaseQueryExecutor dqe) 
	{
		mortgageDao = new MortgageDao(dqe);
	}
	
	/**
	 * Get a list of all mortgages
	 * 
	 * @return Map<Integer, Mortgage> map of mortgages with the key being the mortgage id
	 * 
	 * @throws SQLException
	 * @throws NoResultsFoundException
	 */
	public Map<Integer, Mortgage> getMortgages() throws SQLException, NoResultsFoundException
	{
		return mortgageDao.viewAllMortgages();
	}
	
	/**
	 * Get a mortgage based on the id
	 * 
	 * @param int mortgage id
	 * 
	 * @return Mortgage
	 * 
	 * @throws SQLException
	 * @throws NoResultsFoundException
	 */
	public Mortgage getMortgageById(int mortgageId) throws SQLException, NoResultsFoundException {
		return mortgageDao.getMortgageById(mortgageId);
	}

	/**
	 * Create a new mortgage in the mortgages table in the database
	 * 
	 * @param Mortgage
	 * 
	 * @throws SQLException
	 */
	public void createMortgage(Mortgage mortgage) throws SQLException 
	{
		mortgageDao.insertMortgage(mortgage);	
	}
	
	/**
	 * Delete a mortgage from the mortgages table in the database based on the mortgage id
	 * 
	 * @param int mortgage id
	 * 
	 * @throws SQLException
	 */
	public void deleteMortgage(int mortgageId) throws SQLException 
	{
		mortgageDao.deleteMortgage(mortgageId);
	}
	
}
