package com.fairsail.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.fairsail.exceptions.NoResultsFoundException;
import com.fairsail.loan.PersonalLoan;

public class PersonalLoanDao {

	private DatabaseQueryExecutor dqe;
	
	public PersonalLoanDao(DatabaseQueryExecutor dqe) {
		this.dqe = dqe;
	}
	
	public Map<Integer, PersonalLoan> viewAllPersonalLoans() throws SQLException, NoResultsFoundException{
		Map<Integer, PersonalLoan> allPersonalLoans = new HashMap<Integer, PersonalLoan>();
		
		String getAllLoans =
				"SELECT "
					+ "* "
				+"FROM "
					+ "v_personal_loans";
		
		selectPersonalLoans(getAllLoans, allPersonalLoans);
		
		return allPersonalLoans;
	}
	
	public Map<Integer, PersonalLoan> viewApplicablePersonalLoans(int creditScore) throws SQLException, NoResultsFoundException{
		Map<Integer, PersonalLoan> applicablePersonalLoans = new HashMap<Integer, PersonalLoan>();
		
		String getAllLoans =
				"SELECT "
				+ "* "
			  + "FROM "
			  	+ "v_personal_loans "
			  + "WHERE "
			  	+ "minimum_credit_score < " + creditScore;
		
		selectPersonalLoans(getAllLoans, applicablePersonalLoans);
		
		return applicablePersonalLoans;
	}
	
	public PersonalLoan getPersonalLoanById(int loanId) throws SQLException, NoResultsFoundException {
		Map<Integer, PersonalLoan> personalLoans = new HashMap<Integer, PersonalLoan>();
		
		String getAllLoans =
				"SELECT "
				+ "* "
			  + "FROM "
			  	+ "v_personal_loans "
			  + "WHERE "
			  	+ "loan_id = " + loanId;
		
		selectPersonalLoans(getAllLoans, personalLoans);
			
		return personalLoans.get(loanId);
	}
	
	private void selectPersonalLoans(String sql, Map<Integer, PersonalLoan> loans) throws SQLException, NoResultsFoundException {
		try {
			ResultSet rs = dqe.executeSelect(sql);
			
			if(rs.next()) {
				do {
					PersonalLoan loan = new PersonalLoan();
					
					loan.setLender(rs.getString("lender"));
					loan.setRepaymentMonths(rs.getInt("repayment_months"));
					loan.setMinimumValue(rs.getDouble("minimum_value"));
					loan.setMaximumValue(rs.getDouble("maximum_value"));
					loan.setInterestRate(rs.getDouble("interest_rate"));
					loan.setCreditScoreRule(rs.getString("rule"));
					loan.setMinimumCreditScore(rs.getInt("minimum_credit_score"));
					
					loans.put(rs.getInt("loan_id"), loan);
				}while(rs.next());
			}else {
				throw new NoResultsFoundException();
			}
			
		}finally {
			// Make sure we close the statement or we could have a memory leak
			dqe.closeStatement();
		}
	}
	
	public void insertPersonalLoan(PersonalLoan loan) throws SQLException {	    
		// Insert a new row into the mortgages table
		String insertLoan = 
				"INSERT INTO personal_loans ("
					+ "lender, " 
					+ "repayment_months, "
					+ "minimum_value, "
					+ "maximum_value, "
					+ "interest_rate, " 
					+ "rule, "
					+ "minimum_credit_score " 
				+ ") VALUES ("
					+ "'" + loan.getLender() + "', "
					+ loan.getRepaymentMonths() + ", "
					+ loan.getMinimumValue() + ", "
					+ loan.getMaximumValue() + ", "
					+ loan.getInterestRate() + ", "
					+ "'" + loan.getCreditScoreRule() + "', "
					+ loan.getMinimumCreditScore()
				+ ")";
		
		// Run the insert
		dqe.executeStatement(insertLoan);
	}
	
	public void deletePersonalLoan(int id) throws SQLException {
		// Delete rows with the given id
		dqe.executeStatement("DELETE FROM personal_loans WHERE loan_id = " + id);
	}
	
}
