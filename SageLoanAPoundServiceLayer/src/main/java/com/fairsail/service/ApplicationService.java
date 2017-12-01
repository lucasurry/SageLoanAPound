package com.fairsail.service;

import java.sql.SQLException;
import java.util.List;

import javax.mail.MessagingException;

import com.fairsail.accounts.Applicant;
import com.fairsail.applications.Application;
import com.fairsail.applications.MortgageApplication;
import com.fairsail.applications.PersonalLoanApplication;
import com.fairsail.dao.ApplicationDao;
import com.fairsail.dao.DatabaseQueryExecutor;
import com.fairsail.exceptions.NoResultsFoundException;
import com.fairsail.mail.MailSender;

public class ApplicationService {
	
	private ApplicationDao applicationDao;
	
	public ApplicationService(DatabaseQueryExecutor dqe) {
		applicationDao = new ApplicationDao(dqe);
	}
	
	public void createNewPersonalLoanApplication(PersonalLoanApplication application) throws SQLException {
		applicationDao.insertNewLoanApplication(application);
	}
	
	public void createNewMortgageApplication(MortgageApplication application) throws SQLException {
		applicationDao.insertNewMortgageApplication(application);

	}
	
	public List<PersonalLoanApplication> getLoanApplications(Applicant applicant) throws SQLException, NoResultsFoundException{
		return applicationDao.getUserLoanApplications(applicant.getUserId());
	}
	
	public List<MortgageApplication> getMortgageApplications(Applicant applicant) throws SQLException, NoResultsFoundException{
		return applicationDao.getUserMortgageApplications(applicant.getUserId());
	}
	
	public List<PersonalLoanApplication> getLoanApplications(String status) throws SQLException, NoResultsFoundException{
		return applicationDao.getLoanApplicationsByStatus(status);
	}
	
	public List<MortgageApplication> getMortgageApplications(String status) throws SQLException, NoResultsFoundException{
		return applicationDao.getMortgageApplicationsByStatus(status);
	}
	
	public void setCreditScoreUsedLoan(int applicationId, int creditScore) throws SQLException, NoResultsFoundException{
		applicationDao.setCreditScoreUsedLoan(applicationId, creditScore);;
	}
	
	public void setCreditScoreUsedMortgage(int applicationId, int creditScore) throws SQLException, NoResultsFoundException{
		applicationDao.setCreditScoreUsedMortgage(applicationId, creditScore);;
	}
	
	public void sendApplicationToUnderwriter(PersonalLoanApplication application, Applicant applicant, String message) throws SQLException, MessagingException {
		applicationDao.updateLoanApplicationStatus("A", application.getApplicationId());
		sendApplicationApprovedMail(application, applicant, message);
	}
	
	public void sendApplicationToUnderwriter(MortgageApplication application, Applicant applicant, String message) throws SQLException, MessagingException {
		applicationDao.updateMortgageApplicationStatus("A", application.getApplicationId());
		sendApplicationApprovedMail(application, applicant, message);
	}

	public void approveApplication(PersonalLoanApplication application, Applicant applicant, String message) throws SQLException, MessagingException {
		applicationDao.updateLoanApplicationStatus("C", application.getApplicationId());
		sendApplicationApprovedMail(application, applicant, message);
	}
	
	public void approveApplication(MortgageApplication application, Applicant applicant, String message) throws SQLException, MessagingException {
		applicationDao.updateMortgageApplicationStatus("C", application.getApplicationId());
		sendApplicationApprovedMail(application, applicant, message);
	}
	
	public void rejectApplication(PersonalLoanApplication application, Applicant applicant, String message) throws SQLException, MessagingException {
		applicationDao.updateLoanApplicationStatus("R", application.getApplicationId());
		sendApplicationRejectedMail(application, applicant, message);
	}
	
	public void rejectApplication(MortgageApplication application, Applicant applicant, String message) throws SQLException, MessagingException {
		applicationDao.updateMortgageApplicationStatus("R", application.getApplicationId());
		sendApplicationRejectedMail(application, applicant, message);
	}
	
	private void sendApplicationApprovedMail(Application application, Applicant applicant, String message) throws MessagingException {
		MailSender ms = new MailSender("smtp.gmail.com", 465, "lucasurrytest@gmail.com", "fairsailtest");
		
		ms.sendMessage("lucasurrytest@gmail.com", "Application was approved", "Your application " + application.getApplicationId() + " has been successfully approved \n\n" + message, applicant.getEmailAddress());
	}
	
	private void sendApplicationRejectedMail(Application application, Applicant applicant, String message) throws MessagingException {
		MailSender ms = new MailSender("smtp.gmail.com", 465, "lucasurrytest@gmail.com", "fairsailtest");
		
		ms.sendMessage("lucasurrytest@gmail.com", "Application was rejected", "Your application " + application.getApplicationId() + " has been rejected\n\n" + message, applicant.getEmailAddress());
	}
}
