package com.fairsail.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.fairsail.exceptions.NoResultsFoundException;
import com.fairsail.loan.Mortgage;

public class MortgageDao {

	private DatabaseQueryExecutor dqe;
	
	public MortgageDao(DatabaseQueryExecutor dqe)
	{
		this.dqe = dqe;
	}
	
	public Map<Integer, Mortgage> viewAllMortgages() throws SQLException, NoResultsFoundException
	{
		Map<Integer, Mortgage> allMortgages = new HashMap<Integer, Mortgage>();
		
		String getAllMortgages =
				"SELECT "
					+ "* "
				+"FROM "
					+ "v_mortgages";
		
		selectMortgages(getAllMortgages, allMortgages);
		
		return allMortgages;
	}
	
	public Map<Integer, Mortgage> viewApplicableMortgages(int creditScore) throws SQLException, NoResultsFoundException
	{
		Map<Integer, Mortgage> applicableMortgages = new HashMap<Integer, Mortgage>();
		
		String getAllApplicableMortgages =
				"SELECT "
				+ "* "
			  + "FROM "
			  	+ "v_mortgages "
			  + "WHERE "
			  	+ "minimum_credit_score < " + creditScore;
		
		selectMortgages(getAllApplicableMortgages, applicableMortgages);
		
		return applicableMortgages;
	}
	
	public Mortgage getMortgageById(int mortgageId) throws SQLException, NoResultsFoundException {
		Map<Integer, Mortgage> applicableMortgages = new HashMap<Integer, Mortgage>();
		
		String getAllApplicableMortgages =
				"SELECT "
				+ "* "
			  + "FROM "
			  	+ "v_mortgages "
			  + "WHERE "
			  	+ "mortgage_id = " + mortgageId;
		
		selectMortgages(getAllApplicableMortgages, applicableMortgages);
		
		return applicableMortgages.get(mortgageId);
	}
	
	private void selectMortgages(String sql, Map<Integer, Mortgage> mortgages) throws SQLException, NoResultsFoundException 
	{
		try {
			ResultSet rs = dqe.executeSelect(sql);
			
			if(rs.next()) {
				do {
					Mortgage mortgage = new Mortgage();
					
					mortgage.setLender(rs.getString("lender"));
					mortgage.setRepaymentMonths(rs.getInt("repayment_months"));
					mortgage.setMinimumValue(rs.getDouble("minimum_value"));
					mortgage.setMaximumValue(rs.getDouble("maximum_value"));
					mortgage.setInterestRate(rs.getDouble("interest_rate"));
					mortgage.setMinimumCreditScore(rs.getInt("minimum_credit_score"));
					mortgage.setMinDepositIsPercent(rs.getBoolean("min_deposit_is_percent"));
					mortgage.setMinimumDeposit(rs.getDouble("minimum_deposit"));
					mortgage.setFees(rs.getDouble("fees"));
					mortgage.setCreditScoreRule(rs.getString("rule"));
					mortgage.setFirstTimeBuyerOnly(rs.getBoolean("first_time_buyer_only"));
					
					mortgages.put(rs.getInt("mortgage_id"), mortgage);
				}while(rs.next());
			}else {
				throw new NoResultsFoundException();
			}
			
		}finally {
			// Make sure we close the statement or we could have a memory leak
			dqe.closeStatement();
		}
	}
	
	public void insertMortgage(Mortgage mortgage) throws SQLException 
	{
		// Insert a new row into the mortgages table
		String insertMortgage = 
				"INSERT INTO mortgages ("
					+ "lender, " 
					+ "repayment_months, "
					+ "minimum_value, "
					+ "maximum_value, "
					+ "interest_rate, " 
					+ "min_deposit_is_percent, " 
					+ "minimum_deposit, " 
					+ "minimum_credit_score, " 
					+ "fees, " 
					+ "first_time_buyer_only"
				+ ") VALUES ("
					+ "'" + mortgage.getLender() + "', "
					+ mortgage.getRepaymentMonths() + ", "
					+ mortgage.getMinimumValue() + ", "
					+ mortgage.getMaximumValue() + ", "
					+ mortgage.getInterestRate() + ", "
					+ mortgage.isMinDepositIsPercent() + ", "
					+ mortgage.getMinimumDeposit() + ", "
					+ mortgage.getMinimumCreditScore() + ", "
					+ mortgage.getFees() + ", "
					+ mortgage.isFirstTimeBuyerOnly()
				+ ")";
		
		// Run the insert
		dqe.executeStatement(insertMortgage);
	}
	
	public void deleteMortgage(int id) throws SQLException 
	{
		// Delete rows with the given id
		dqe.executeStatement("DELETE FROM mortgages WHERE mortgage_id = " + id);
	}	
}
