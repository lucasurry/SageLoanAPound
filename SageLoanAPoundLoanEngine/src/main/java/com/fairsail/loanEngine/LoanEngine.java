package com.fairsail.loanEngine;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.fairsail.accounts.Applicant;
import com.fairsail.accounts.CreditScore;
import com.fairsail.applications.MortgageApplication;
import com.fairsail.applications.PersonalLoanApplication;
import com.fairsail.dao.DatabaseQueryExecutor;
import com.fairsail.exceptions.NoResultsFoundException;
import com.fairsail.loan.Mortgage;
import com.fairsail.loan.PersonalLoan;
import com.fairsail.service.ApplicationService;
import com.fairsail.service.MortgageService;
import com.fairsail.service.PersonalLoanService;
import com.fairsail.service.UserService;

public class LoanEngine {

	private DatabaseQueryExecutor dqe = null;
	private UserService uService = null;
	private ApplicationService aService = null;
	private PersonalLoanService pService = null;
	private MortgageService mService = null;
	
	private CreditScoreFetcher csFetcher = null;
	
	private int creditScore = 0;
	
	public String message = "";
	private Applicant applicant;
	
	/*
	 * Can probably handle some of these exceptions, would need some clearer instuctions from the end user as to what they want to do if they hit an issue though
	 */
	public static void main(String args[]) throws SQLException, NoResultsFoundException, MessagingException, IOException, ParserConfigurationException, SAXException {
		new LoanEngine().approveApplications();
	}
	
	/*
	 * Start up the services we need to run the loan engine
	 */
	private void initalize() throws SQLException {
		dqe = new DatabaseQueryExecutor("application","apppass","localhost", "loanapound");
		uService = new UserService(dqe);
		aService = new ApplicationService(dqe);
		pService = new PersonalLoanService(dqe);
		mService = new MortgageService(dqe);
	}
	
	/**
	 * Runs the loan engine which checks for any applications created by the user, checks the applications validity and either sends them to the underwriter for final approval or rejects them
	 * 
	 * @throws SQLException
	 * @throws NoResultsFoundException
	 * @throws MessagingException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public void approveApplications() throws SQLException, NoResultsFoundException, MessagingException, IOException, ParserConfigurationException, SAXException {
		// Replace this if there is time
		File file = new File("loanEngine.stop");
		if(file.exists()) {
			file.delete();
		}
		
		System.out.println("To stop add the file " + file.getName() + " here " + file.getAbsolutePath());
		
		initalize();
		try {
			while(!file.exists()) {
				// Get a list of unapproved loans and a list of unapproved mortgages
				List<PersonalLoanApplication> personalLoanApplications = new ArrayList<PersonalLoanApplication>();
				List<MortgageApplication> mortgageApplications = new ArrayList<MortgageApplication>();
				
				
				try {
					personalLoanApplications = aService.getLoanApplications("U");
				} catch (NoResultsFoundException e) {
					// Do nothing, we just won't process any applications
				}
	
				try {
					mortgageApplications = aService.getMortgageApplications("U");
				} catch (NoResultsFoundException e) {
					// Do nothing, we just won't process any applications
				}
				
				// Check the loans and mortgages can be approved
				for(PersonalLoanApplication application : personalLoanApplications) {
					approvePersonalLoan(application);
				}
	
				for(MortgageApplication application : mortgageApplications) {
					approveMortgage(application);
				}
				
				// Simple polling, check once every minute
				// I would change this for a trigger from the database on the insert of a new application
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					// Do nothing
				}
			}
		}finally {
			// Make sure to close the connection
			dqe.closeConnection();
		}
	}
	
	/**
	 * This method takes a personal loan application, checks the user has a credit score high enough to apply for the product and that the value they requested is
	 * within an accepted range for the product.
	 * 
	 * If a user does not have a credit score set already or the set credit score is stale (more than a month old in this case) then it will also go and fetch the 
	 * users credit score from the CreditScoreAPI
	 * 
	 * @param PersonalLoanApplication
	 * 
	 * @throws SQLException
	 * @throws NoResultsFoundException
	 * @throws MessagingException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public void approvePersonalLoan(PersonalLoanApplication application) throws SQLException, NoResultsFoundException, MessagingException, IOException, ParserConfigurationException, SAXException {
		// Get information about the loan and the applicant
		PersonalLoan loan = pService.getPersonalLoanById(application.getLoanId());
		applicant = uService.getApplicatInformation(application.getApplicantId());
		
		// Check the users credit score
		applicant.addCreditScores(uService.getApplicantCreditScores(applicant.getUserId()));
		applicant.setLastCSUpdate(uService.getLastCreditScoreUpdate(applicant.getUserId()));
		
		boolean isValid = true;
		message = "";
		
		// Check loan amount applied for and credit score are ok
		isValid = checkLoanAmount(application.getLoanValue(), loan.getMinimumValue(), loan.getMaximumValue());
		isValid = checkCreditScore(applicant, loan.getMinimumCreditScore(), loan.getCreditScoreRule());
		
		aService.setCreditScoreUsedLoan(application.getApplicationId(), creditScore);
		
		// Approve if valid, otherwise reject
		if(isValid) {
			message = "Pending final approval by an underwriter";
			aService.sendApplicationToUnderwriter(application, applicant, message);
		}else {
			aService.rejectApplication(application, applicant, message);
		}
	}
	
	/**
	 * This method takes a mortgage application, checks the user has a credit score high enough to apply for the product, that the value they requested is
	 * within an accepted range for the product what their deposit is high enough and check if they are a first time buyer.
	 * 
	 * If a user does not have a credit score set already or the set credit score is stale (more than a month old in this case) then it will also go and fetch the 
	 * users credit score from the CreditScoreAPI
	 * 
	 * @param MortgageApplication
	 * 
	 * @throws SQLException
	 * @throws NoResultsFoundException
	 * @throws MessagingException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public void approveMortgage(MortgageApplication application) throws SQLException, NoResultsFoundException, MessagingException, IOException, ParserConfigurationException, SAXException {
		// Get information about the mortgage and the applicant
		Mortgage mortgage = mService.getMortgageById(application.getMortgageId());
		applicant = uService.getApplicatInformation(application.getApplicantId());
		
		// Check the users credit score
		applicant.addCreditScores(uService.getApplicantCreditScores(applicant.getUserId()));
		applicant.setLastCSUpdate(uService.getLastCreditScoreUpdate(applicant.getUserId()));
		
		boolean isValid = true;
		message = "";
		
		// Check loan amount applied for, proposed deposit and credit score are ok
		isValid = checkLoanAmount(application.getLoanValue(), mortgage.getMinimumValue(), mortgage.getMaximumValue());
		isValid = checkDepositAmount(application.getDepositAmount(), application.getLoanValue(), mortgage.getMinimumDeposit(), mortgage.isMinDepositIsPercent());
		isValid = checkFirstTimeBuyerStatus(application.isFirstTimeBuyer(), mortgage.isFirstTimeBuyerOnly());
		isValid = checkCreditScore(applicant, mortgage.getMinimumCreditScore(), mortgage.getCreditScoreRule());
		
		aService.setCreditScoreUsedMortgage(application.getApplicationId(), creditScore);
		
		// Approve if valid, otherwise reject
		if(isValid) {
			message = "Pending final approval by an underwriter";
			aService.sendApplicationToUnderwriter(application, applicant, message);
		}else {
			aService.rejectApplication(application, applicant, message);
		}
	}
	
	/**
	 * Check that the loan amount falls into the range of maximum loan amount and minimum loan amount
	 *  
	 * @param double value
	 * @param double minimum loan value
	 * @param double maximum loan value
	 * 
	 * @return boolean true if test passed, false if test failed
	 */
	public boolean checkLoanAmount(double value, double min, double max){
		boolean isValid = true;
		
		// Check requested loan amount is not to high or low
		if(value < min) {
			// If the requested loan amount was lower than the minimum amount allowed then add this to the email message
			isValid = false;
			message = message + "The requested loan value was too low.\n";
			message = message + "The requested loan value was " + value + ".\n";
			message = message + "This type of loan has a minimum value of  " + min + ".\n\n";
		}
		
		if(value > max) {
			// If the requested loan amount was higher than the maximum amount allowed then add this to the email message
			isValid = false;
			message = message + "The requested loan value was too high.\n";
			message = message + "The requested loan value was " + value + ".\n";
			message = message + "This type of loan has a maximum value of  " + max + ".\n\n";
		}
		
		return isValid;
	}
	
	/**
	 * For a secured loan check that the deposit value is high enough
	 * 
	 * @param double deposit
	 * @param double loan value
	 * @param double minimum deposit
	 * @param boolean true if the minimum deposit a percentage of the max value, false if the minimum deposit is a fixed value
	 * 
	 * @return boolean true if test passed, false if test failed
	 */
	public boolean checkDepositAmount(double deposit, double loanValue, double minimum, boolean isPercent) {
		boolean isValid = true;
		
		double minimumDeposit = 0;
		
		// If the minimum deposit is a percent work out what the minimum deposit is
		if(isPercent) {
			minimumDeposit = (loanValue / 100.0) * minimum;
		}else {
			minimumDeposit = minimum;
		}
		
		// If the requested deposit was lower than the minimum amount allowed then add this to the email message
		if(deposit < minimumDeposit) {
			isValid = false;
			message = message + "The your deposit was too low.\n";
			message = message + "The requested deposit was " + deposit + ".\n";
			message = message + "This type of loan has a minium deposit of  " + minimumDeposit + " for the amount you want to borrow.\n\n";
		}
		
		return isValid;
	}
	
	/**
	 * Check if the product is only for first time buyers and if it is then check the user is a first time buyer
	 * 
	 * @param boolean isFirstTimeBuyer
	 * @param boolean isForFirstTimeBuyers
	 * 
	 * @return boolean true if valid, false if test fails
	 */
	public boolean checkFirstTimeBuyerStatus(boolean isFirstTimeBuyer, boolean isForFirstTimeBuyers){
		boolean isValid = true;
		
		if(!isFirstTimeBuyer && isForFirstTimeBuyers) {
			isValid = false;
			// If the product is for a first time buyer and the applicant is not add this to the email message
			message = message + "The mortgage you selected was for first time buyers only and you are not a first time buyer.\n\n";
		}
		
		return isValid;
	}
	
	/**
	 * Check the users credit score is high enough to apply for this product
	 * 
	 * @param Applicant applicant
	 * @param int minimum credit score for the product
	 * @param String rule to get the correct credit score for the product
	 * 
	 * @return boolean true if the users credit score is high enough, false if it is not
	 * 
	 * @throws SQLException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public boolean checkCreditScore(Applicant applicant, int minimum, String rule) throws SQLException, IOException, ParserConfigurationException, SAXException {
		boolean isValid = true;
		
		// If there is no credit score in the system or the recorded one is more than a month old get a new one
		if(applicant.getCreditScores().size() == 0 || applicant.getLastCSUpdate().before(getlastMonthsDate())) {
			List<CreditScore> creditScores = new ArrayList<CreditScore>();
			csFetcher = new CreditScoreFetcher();
			
			// Set the sources
			String sources[] = {"creditsite","creditsite2"};
			
			for(String source : sources) {
				creditScores.add(csFetcher.getCreditScore(applicant, source));
			}
			
			// Add the credit score to the database so we don't have to get it every time
			uService.updateApplicantCreditCheck(applicant.getUserId(), creditScores, applicant.getNotes());
			applicant.addCreditScores(creditScores);
			
		}
		
		
		// Choose the credit score we are going to use based on the rule that was set
		creditScore = uService.chooseCreditScoreToUse(applicant.getCreditScores(), rule);
		
		if(creditScore < minimum) {
			isValid = false;
			// If the applicants credit score is too low add this to the email message
			message = message + "The your credit score was too low.\n";
			message = message + "The your credit score was " + creditScore + ".\n";
			message = message + "This type of loan requires a minimum credit score of  " + minimum + ".\n\n";
		}
		
		return isValid;
	}

	
	/*
	 * Get the date one month ago so we can check the stored credit score is not stale
	 */
	private Date getlastMonthsDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		return calendar.getTime();
	}
}
