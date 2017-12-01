package com.fairsail.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fairsail.applications.MortgageApplication;
import com.fairsail.applications.PersonalLoanApplication;
import com.fairsail.exceptions.NoResultsFoundException;

public class ApplicationDao {

	private DatabaseQueryExecutor dqe;
	
	public ApplicationDao(DatabaseQueryExecutor dqe) {
		this.dqe = dqe;
	}
	
	public void insertNewLoanApplication(PersonalLoanApplication application) throws SQLException {
		String sql = 
				  "INSERT INTO personal_loan_applications ("
					+ "applicant_id, "
					+ "loan_id, "
					+ "loan_value, "
					+ "application_status "
				+ ") VALUES ("
					+ application.getApplicant().getUserId() + ", "
					+ application.getLoanId() + ", "
					+ application.getLoanValue() + ", "
					+ "'" + application.getApplicationStatus() + "'"
				+ ")";
		
		// Run the insert
		dqe.executeStatement(sql);
	}
	
	public void insertNewMortgageApplication(MortgageApplication application) throws SQLException {
		String sql = 
				  "INSERT INTO mortgage_applications ("
					+ "applicant_id, "
					+ "mortgage_id, "
					+ "loan_value, "
					+ "deposit_amount, "
					+ "first_time_buyer, "
					+ "application_status "
				+ ") VALUES ("
					+ application.getApplicant().getUserId() + ", "
					+ application.getMortgageId() + ", "
					+ application.getLoanValue() + ", "
					+ application.getDepositAmount() + ", "
					+ application.isFirstTimeBuyer() + ", "
					+ "'" + application.getApplicationStatus() + "'"
				+ ")";
		
		// Run the insert
		dqe.executeStatement(sql);
	}
	
	private List<PersonalLoanApplication> getLoanApplications(String sql) throws SQLException, NoResultsFoundException{
		List<PersonalLoanApplication> applications = new ArrayList<PersonalLoanApplication>();
		
		ResultSet rs = dqe.executeSelect(sql);
		
		// Check there was at least one result
		if(rs.next()) {
			do {
				PersonalLoanApplication application = new PersonalLoanApplication();
				
				application.setApplicationId(rs.getInt("application_id"));
				application.setApplicantId(rs.getInt("applicant_id"));
				application.setLoanId(rs.getInt("loan_id"));
				application.setLoanValue(rs.getDouble("loan_value"));
				application.setCreditScoreUsed(rs.getInt("credit_score_used"));
				application.setApplicationStatus(rs.getString("application_status"));
				
				applications.add(application);
			}while(rs.next());
		}else{
			throw new NoResultsFoundException();
		}
		
		return applications;
	}
	
	private List<MortgageApplication> getMortgageApplications(String sql) throws SQLException, NoResultsFoundException{
		List<MortgageApplication> applications = new ArrayList<MortgageApplication>();
		
		ResultSet rs = dqe.executeSelect(sql);
		
		// Check there was at least one result
		if(rs.next()) {
			do {
				MortgageApplication application = new MortgageApplication();
				
				application.setApplicationId(rs.getInt("application_id"));
				application.setApplicantId(rs.getInt("applicant_id"));
				application.setMortgageId(rs.getInt("mortgage_id"));
				application.setLoanValue(rs.getDouble("loan_value"));
				application.setDepositAmount(rs.getDouble("deposit_amount"));
				application.setFirstTimeBuyer(rs.getBoolean("first_time_buyer"));
				application.setCreditScoreUsed(rs.getInt("credit_score_used"));
				application.setApplicationStatus(rs.getString("application_status"));
				
				applications.add(application);
			}while(rs.next());
		}else{
			throw new NoResultsFoundException();
		}
		
		return applications;
	}
	
	public List<PersonalLoanApplication> getUserLoanApplications(int user_id) throws SQLException, NoResultsFoundException{
		String sql = 
				  "SELECT "
					+ "application_id, "
					+ "applicant_id, "
					+ "loan_id, "
					+ "loan_value, "
					+ "credit_score_used, "
					+ "application_status "
				+ "FROM "
					+ "personal_loan_applications "
				+ "WHERE "
					+ "applicant_id = " + user_id;
		
		return getLoanApplications(sql);
	}
	
	public List<MortgageApplication> getUserMortgageApplications(int user_id) throws SQLException, NoResultsFoundException{
		String sql = 
				  "SELECT "
					+ "application_id, "
					+ "applicant_id, "
					+ "mortgage_id, "
					+ "loan_value, "
					+ "deposit_amount, "
					+ "first_time_buyer, "
					+ "credit_score_used, "
					+ "application_status "
				+ "FROM "
					+ "mortgage_applications "
				+ " WHERE "
					+ "applicant_id = " + user_id;
		
		return getMortgageApplications(sql);
	}
	
	public List<PersonalLoanApplication> getLoanApplicationsByStatus(String status) throws SQLException, NoResultsFoundException{
		String sql = 
				  "SELECT "
					+ "application_id, "
					+ "applicant_id, "
					+ "loan_id, "
					+ "loan_value, "
					+ "credit_score_used, "
					+ "application_status "
				+ "FROM "
					+ "personal_loan_applications "
				+ "WHERE "
					+ "application_status = '" + status + "'";
		
		return getLoanApplications(sql);
	}
	
	public List<MortgageApplication> getMortgageApplicationsByStatus(String status) throws SQLException, NoResultsFoundException{
		String sql = 
				  "SELECT "
					+ "application_id, "
					+ "applicant_id, "
					+ "mortgage_id, "
					+ "loan_value, "
					+ "deposit_amount, "
					+ "first_time_buyer, "
					+ "credit_score_used, "
					+ "application_status "
				+ "FROM "
					+ "mortgage_applications "
				+ "WHERE "
					+ "application_status = '" + status + "'";
		
		return getMortgageApplications(sql);
	}
	
	public void updateLoanApplicationStatus(String status, int applicationId) throws SQLException {
		String sql = 
				  "UPDATE "
					+ "personal_loan_applications "
				+ "SET "
					+ "application_status = '" + status + "' "
				+ "WHERE "
					+ "application_id = " +  applicationId;
		
		dqe.executeStatement(sql);
	}
	
	public void updateMortgageApplicationStatus(String status, int applicantionId) throws SQLException {
		String sql = 
				  "UPDATE "
					+ "mortgage_applications "
				+ "SET "
					+ "application_status = '" + status + "' "
				+ "WHERE "
					+ "application_id = " +  applicantionId;
		
		dqe.executeStatement(sql);
	}
	
	public void setCreditScoreUsedLoan(int applicationId, int creditScore) throws SQLException {
		String sql = "UPDATE personal_loan_applications SET credit_score_used = " + creditScore + " WHERE application_id = " + applicationId;
		
		dqe.executeStatement(sql);
	}
	
	public void setCreditScoreUsedMortgage(int applicationId, int creditScore) throws SQLException {
		String sql = "UPDATE mortgage_applications SET credit_score_used = " + creditScore + " WHERE application_id = " + applicationId;
		
		dqe.executeStatement(sql);
	}
}
