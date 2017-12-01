package com.fairsail.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fairsail.accounts.Administrator;
import com.fairsail.accounts.Applicant;
import com.fairsail.accounts.CreditScore;
import com.fairsail.accounts.Underwriter;
import com.fairsail.exceptions.NoResultsFoundException;
import com.fairsail.utils.GlobalSettings;

public class UserDao {

	private DatabaseQueryExecutor dqe;
	
	public UserDao(DatabaseQueryExecutor dqe){
		this.dqe = dqe;
	}
	
	public String getUserPassword(String userName) throws SQLException, NoResultsFoundException {
		String sql = 
				  "SELECT "
					+ "CAST(AES_DECRYPT(password, '" + GlobalSettings.ENCKEY + "') AS CHAR(40)) password "
				+ "FROM "
					+ "users "
				+ "WHERE "
					+ "name = '" + userName + "'";
		
		ResultSet rs = dqe.executeSelect(sql);
		
		if(!rs.next()) {
			throw new NoResultsFoundException();
		}
		
		return rs.getString("password");
	}
	
	public String getUserType(String userName) throws SQLException, NoResultsFoundException {
		String sql = 
				  "SELECT "
					+ "user_type "
				+ "FROM "
					+ "users "
				+ "WHERE "
					+ "name = '" + userName + "'";
		
		ResultSet rs = dqe.executeSelect(sql);
		
		if(!rs.next()) {
			throw new NoResultsFoundException();
		}
		
		return rs.getString("user_type");
	}
	
	public Applicant selectApplicant(String userName) throws SQLException, NoResultsFoundException {
		String sql = 
				  "SELECT "
					+ "*, "
					+ "CAST(AES_DECRYPT(email_address, '" + GlobalSettings.ENCKEY + "') AS CHAR(40)) decrypted_email "
				+ "FROM "
					+ "v_applicants "
				+ "WHERE "
					+ "name = '" + userName + "'";
		
		ResultSet rs = dqe.executeSelect(sql);
		
		if(!rs.next()) {
			throw new NoResultsFoundException();
		}
		
		int userId = rs.getInt("user_id");
		String email = rs.getString("decrypted_email");
		
		return new Applicant(userId, userName, email); 
	}
	
	public Applicant selectApplicant(int applicantId) throws SQLException, NoResultsFoundException {
		String sql = 
				  "SELECT "
					+ "*, "
					+ "CAST(AES_DECRYPT(email_address, '" + GlobalSettings.ENCKEY + "') AS CHAR(40)) decrypted_email "
				+ "FROM "
					+ "v_applicants "
				+ "WHERE "
					+ "user_id = '" + applicantId + "'";
		
		ResultSet rs = dqe.executeSelect(sql);
		
		if(!rs.next()) {
			throw new NoResultsFoundException();
		}
		
		String userName = rs.getString("name");
		String email = rs.getString("decrypted_email");
		
		return new Applicant(applicantId, userName, email); 
	}
	
	public Administrator selectAdministrator(String userName) throws SQLException {
		String sql = 
				  "SELECT "
					+ "* "
				+ "FROM "
					+ "v_administrators "
				+ "WHERE "
					+ "name = '" + userName + "'";
		
		ResultSet rs = dqe.executeSelect(sql);
		
		rs.next();
		int userId = rs.getInt("user_id");
		
		return new Administrator(userId, userName);
	}
	
	public Underwriter selectUnderwriter(String userName) throws SQLException {
		String sql = 
				  "SELECT "
					+ "* "
				+ "FROM "
					+ "v_underwriters "
				+ "WHERE "
					+ "name = '" + userName + "'";
		
		ResultSet rs = dqe.executeSelect(sql);
		
		rs.next();
		int userId = rs.getInt("user_id");
		
		return new Underwriter(userId, userName);
	}
	
	public List<CreditScore> selectUserCreditScores(int userId) throws SQLException {
		List<CreditScore> creditScores = new ArrayList<CreditScore>();
		
		String sql = 
				  "SELECT "
					+ "* "
				+ "FROM "
					+ "applicant_credit_scores "
				+ "WHERE "
					+ "user_id = " + userId;
		
		ResultSet rs = dqe.executeSelect(sql);
		
		while(rs.next()) {
			CreditScore creditScore = new CreditScore();
			
			creditScore.setScore(rs.getInt("credit_score"));
			creditScore.setSource(rs.getString("credit_score_source"));
			
			creditScores.add(creditScore);
		}
		
		return creditScores;
	}
	
	public void updateUserCreditScore(int userId, List<CreditScore> creditScores) throws SQLException {
		// Delete existing credit scores so we can replace them with the new ones
		String delete = "DELETE FROM applicant_credit_scores WHERE user_id = " + userId; 
		
		dqe.executeStatement(delete);
		
		// Insert the new credit scores into the user table
		for(CreditScore creditScore : creditScores) {
			 String insert = 
					  "INSERT INTO  "
					+ 	"applicant_credit_scores ("
					+ 		"user_id, "
					+ 		"credit_score, "
					+ 		"credit_score_source"
					+ ") VALUES (" 
					+ 	userId + ", " 
					+ 	creditScore.getScore() + ", "
					+ 	"'" + creditScore.getSource() + "'"
					+ ")";
			 
			 dqe.executeStatement(insert);
		}
	}
	
	public List<String> selectApplicantNotes(int applicantId) throws SQLException {
		List<String> notes = new ArrayList<String>();
		
		String selectNotes = "SELECT * FROM applicant_notes WHERE user_id = " + applicantId;
		
		ResultSet rs = dqe.executeSelect(selectNotes);
		
		while(rs.next()) {
			notes.add(rs.getString("note"));
		}
		
		return notes;
	}
	
	public void insertNotesForApplicant(int userId, List<String> notes) throws SQLException {
		// Delete existing notes as we will get all relevent ones on a credit check
		String delete =
				"DELETE FROM applicant_notes WHERE user_id = " + userId;
		
		dqe.executeStatement(delete);
		
		// Now add the notes we got
		for(String note : notes) {
			String insert =	"INSERT INTO applicant_notes (user_id, note) VALUES (" + userId + ", '" + note  + "')";
			
			dqe.executeStatement(insert);
		}
	}
	
	public void updateLastCreditCheckDate(int userId) throws SQLException {
		String updateLastUpdatedDate =
				"UPDATE applicants SET last_cs_update = sysdate() WHERE user_id = " + userId;
		
		dqe.executeStatement(updateLastUpdatedDate);
	}
	
	public Date selectLastCreditCheckDate(int userId) throws SQLException {
		String updateLastUpdatedDate =
				"SELECT last_cs_update FROM applicants WHERE user_id = " + userId;
		
		ResultSet rs = dqe.executeSelect(updateLastUpdatedDate);
		
		rs.next();
		return rs.getDate("last_cs_update");
	}
}
