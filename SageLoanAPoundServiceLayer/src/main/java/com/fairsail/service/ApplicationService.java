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
	
	/**
	 * Data access object which interfaces with the database and the email client to update information about applications
	 * 
	 * @param DatabaseQueryExecutor
	 */
	public ApplicationService(DatabaseQueryExecutor dqe) {
		applicationDao = new ApplicationDao(dqe);
	}
	
	/**
	 * Insert a personal loan application to the database
	 * 
	 * @param PersonalLoanApplication application
	 * 
	 * @throws SQLException
	 */
	public void createNewPersonalLoanApplication(PersonalLoanApplication application) throws SQLException {
		applicationDao.insertNewLoanApplication(application);
	}
	
	/**
	 * Insert a mortgage application to the database
	 * 
	 * @param Mortgage application
	 * 
	 * @throws SQLException
	 */
	public void createNewMortgageApplication(MortgageApplication application) throws SQLException {
		applicationDao.insertNewMortgageApplication(application);

	}
	
	/**
	 * Get a list of personal loan applications for a given applicant
	 * 
	 * @param Applicant applicant
	 * 
	 * @return List<PersonalLoanApplication>
	 * 
	 * @throws SQLException
	 * @throws NoResultsFoundException
	 */
	public List<PersonalLoanApplication> getLoanApplications(Applicant applicant) throws SQLException, NoResultsFoundException{
		return applicationDao.getUserLoanApplications(applicant.getUserId());
	}
	
	/**
	 * Get a list of mortgage applications for a given applicant
	 * 
	 * @param Applicant applicant
	 * 
	 * @return List<MortgageApplication>
	 * 
	 * @throws SQLException
	 * @throws NoResultsFoundException
	 */
	public List<MortgageApplication> getMortgageApplications(Applicant applicant) throws SQLException, NoResultsFoundException{
		return applicationDao.getUserMortgageApplications(applicant.getUserId());
	}
	
	/**
	 * Get a list of personal loan applications which are in a particular state
	 * 
	 * @param String status
	 * 
	 * @return List<PersonalLoanApplication>
	 * 
	 * @throws SQLException
	 * @throws NoResultsFoundException
	 */
	public List<PersonalLoanApplication> getLoanApplications(String status) throws SQLException, NoResultsFoundException{
		return applicationDao.getLoanApplicationsByStatus(status);
	}
	
	/**
	 * Get a list of mortgage applications which are in a particular state
	 * 
	 * @param String status
	 * 
	 * @return List<MortgageApplication>
	 * 
	 * @throws SQLException
	 * @throws NoResultsFoundException
	 */
	public List<MortgageApplication> getMortgageApplications(String status) throws SQLException, NoResultsFoundException{
		return applicationDao.getMortgageApplicationsByStatus(status);
	}
	
	/**
	 * Set the credit score of the applicant which was used by this personal loan application (so the underwriter can see it mostly)
	 *  
	 * @param int application id
	 * @param int credit score
	 * 
	 * @throws SQLException
	 * @throws NoResultsFoundException
	 */
	public void setCreditScoreUsedLoan(int applicationId, int creditScore) throws SQLException, NoResultsFoundException{
		applicationDao.setCreditScoreUsedLoan(applicationId, creditScore);;
	}
	
	/**
	 * Set the credit score of the applicant which was used by this mortgage application (so the underwriter can see it mostly)
	 * 
	 * @param int application id
	 * @param int credit score
	 * 
	 * @throws SQLException
	 * @throws NoResultsFoundException
	 */
	public void setCreditScoreUsedMortgage(int applicationId, int creditScore) throws SQLException, NoResultsFoundException{
		applicationDao.setCreditScoreUsedMortgage(applicationId, creditScore);;
	}
	
	/**
	 * Update the personal loan application status to "A" (awaiting final approval) so that the underwriter can check it and send an email to update the applicant of this change
	 * 
	 * @param PersoinalLoanApplication application
	 * @param Applicant applicant
	 * @param String message
	 * 
	 * @throws SQLException
	 * @throws MessagingException
	 */
	public void sendApplicationToUnderwriter(PersonalLoanApplication application, Applicant applicant, String message) throws SQLException, MessagingException {
		applicationDao.updateLoanApplicationStatus("A", application.getApplicationId());
		sendApplicationApprovedMail(application, applicant, message);
	}
	
	/**
	 * Update the mortgage application status to "A" (awaiting final approval) so that the underwriter can check it and send an email to update the applicant of this change
	 * 
	 * @param MortgageApplication application
	 * @param Applicant applicant
	 * @param String message
	 * 
	 * @throws SQLException
	 * @throws MessagingException
	 */
	public void sendApplicationToUnderwriter(MortgageApplication application, Applicant applicant, String message) throws SQLException, MessagingException {
		applicationDao.updateMortgageApplicationStatus("A", application.getApplicationId());
		sendApplicationApprovedMail(application, applicant, message);
	}

	/**
	 * Update the personal loan application status to "C" (complete) and send an email to update the applicant of this change
	 * 
	 * @param PersonalLoanApplication application
	 * @param Applicant applicant
	 * @param String message
	 * 
	 * @throws SQLException
	 * @throws MessagingException
	 */
	public void approveApplication(PersonalLoanApplication application, Applicant applicant, String message) throws SQLException, MessagingException {
		applicationDao.updateLoanApplicationStatus("C", application.getApplicationId());
		sendApplicationApprovedMail(application, applicant, message);
	}
	
	/**
	 * Update the mortgage application status to "C" (complete) and send an email to update the applicant of this change
	 * 
	 * @param MortgageApplication application
	 * @param Applicant applicant
	 * @param String message
	 * 
	 * @throws SQLException
	 * @throws MessagingException
	 */
	public void approveApplication(MortgageApplication application, Applicant applicant, String message) throws SQLException, MessagingException {
		applicationDao.updateMortgageApplicationStatus("C", application.getApplicationId());
		sendApplicationApprovedMail(application, applicant, message);
	}
	
	/**
	 * Update the personal loan application status to "R" (rejected) and send an email to update the applicant of this change
	 * 
	 * @param PersonalLoanApplication application
	 * @param Applicant applicant
	 * @param String message
	 * 
	 * @throws SQLException
	 * @throws MessagingException
	 */
	public void rejectApplication(PersonalLoanApplication application, Applicant applicant, String message) throws SQLException, MessagingException {
		applicationDao.updateLoanApplicationStatus("R", application.getApplicationId());
		sendApplicationRejectedMail(application, applicant, message);
	}
	
	/**
	 * Update the mortgage application status to "R" (rejected) and send an email to update the applicant of this change
	 * 
	 * @param MortgageApplication application
	 * @param Applicant applicant
	 * @param String message
	 * 
	 * @throws SQLException
	 * @throws MessagingException
	 */
	public void rejectApplication(MortgageApplication application, Applicant applicant, String message) throws SQLException, MessagingException {
		applicationDao.updateMortgageApplicationStatus("R", application.getApplicationId());
		sendApplicationRejectedMail(application, applicant, message);
	}
	
	/*
	 * Send an email to the applicant updating them of their application status to approved
	 */
	private void sendApplicationApprovedMail(Application application, Applicant applicant, String message) throws MessagingException {
		MailSender ms = new MailSender("smtp.gmail.com", 465, "lucasurrytest@gmail.com", "fairsailtest");
		
		ms.sendMessage("lucasurrytest@gmail.com", "Application was approved", "Your application " + application.getApplicationId() + " has been successfully approved \n\n" + message, applicant.getEmailAddress());
	}
	
	/*
	 * Send an email to the applicant updating them of their application status to rejected
	 */
	private void sendApplicationRejectedMail(Application application, Applicant applicant, String message) throws MessagingException {
		MailSender ms = new MailSender("smtp.gmail.com", 465, "lucasurrytest@gmail.com", "fairsailtest");
		
		ms.sendMessage("lucasurrytest@gmail.com", "Application was rejected", "Your application " + application.getApplicationId() + " has been rejected\n\n" + message, applicant.getEmailAddress());
	}
}
