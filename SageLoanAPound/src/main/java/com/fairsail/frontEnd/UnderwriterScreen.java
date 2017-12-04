package com.fairsail.frontEnd;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import com.fairsail.accounts.Applicant;
import com.fairsail.applications.MortgageApplication;
import com.fairsail.applications.PersonalLoanApplication;
import com.fairsail.dao.DatabaseQueryExecutor;
import com.fairsail.exceptions.NoResultsFoundException;
import com.fairsail.service.ApplicationService;
import com.fairsail.service.UserService;
import com.fairsail.utils.PrintSettings;

public class UnderwriterScreen {

	private ApplicationService aService;
	private UserService uService;
	
	/**
	 * Displays the screen for underwriters. Allows them to view open applications and choose to approve or reject them
	 * 
	 * @param DatabaseQueryExecutor
	 */
	public UnderwriterScreen(DatabaseQueryExecutor dqe) {
		aService = new ApplicationService(dqe);
		uService = new UserService(dqe);
	}
	
	/**
	 * Display the main menu for an underwriter user
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 * @throws MessagingException
	 */
	public void underwriterScreen() throws IOException, InterruptedException, SQLException, MessagingException {
		boolean isLoggedIn = true;
		
		// Until the user logs out
		while(isLoggedIn) {
			// Print the screen title
			List<String> title = new ArrayList<String>();
			title.add("Logged in as an underwriter\n\n");
			title.add("----------------------------------------\n\n");
			title.add("Please select from the following options by entering the correct number\n");
			title.add("  1) View and approve personal loans waiting for approval\n");
			title.add("  2) View and approve mortgages waiting for approval\n");
			title.add("\n Type logout to exit\n");
			PrintSettings.printScreenTitle(title);
		
			String input = PrintSettings.CONSOLE.readLine("Enter your selection : \n");
			
			// Use a switch statement to decide what to do based on user input
			switch(input.toLowerCase()) {
				case "1"		: viewPersonalLoanApplications();
								  break;
				case "2"		: viewMortgageApplications();
								  break;
				case "logout"	: isLoggedIn = false;
								  break;
				default 		: PrintSettings.CONSOLE.printf(input + " is not a valid option\n");
								  PrintSettings.CONSOLE.readLine("Press the return key to continue");
			}
		}
	}
	
	/*
	 * Take the underwriter to a screen which allows them to choose a personal loan application to view the details of then approve or reject that application
	 */
	private void viewPersonalLoanApplications() throws IOException, InterruptedException, SQLException, MessagingException {
		List<PersonalLoanApplication> loanApplications;
		boolean isBack = false;
		
		while(!isBack) {
			// Print the screen title
			List<String> title = new ArrayList<String>();
			title.add("Open Personal Loan Applications\n\n");
			PrintSettings.printScreenTitle(title);
			
			try {
			
			loanApplications = aService.getLoanApplications("A");
			
			
				// Set the format so that we can print a table
				String format = "|%15s|%8s|%16s||\n";
				
				PrintSettings.CONSOLE.printf(format.replace("%", "%-"), "Application ID", "Loan ID", "Value Requested");
				PrintSettings.CONSOLE.printf(format, "--------------", "-------", "---------------");
				
				for(PersonalLoanApplication application : loanApplications) {
					String applicationId = Integer.toString(application.getApplicationId());
					String loanId = Integer.toString(application.getLoanId());
					String requestedValue = "£" + Double.toString(application.getLoanValue());
					
					PrintSettings.CONSOLE.printf(format, applicationId, loanId, requestedValue);
				}
				
				PrintSettings.CONSOLE.printf("Type back to go back to screen selection\n");
				String input = PrintSettings.CONSOLE.readLine("Enter application id to go to approval screen\n");
				
				if(input.equalsIgnoreCase("back")) {
					isBack = true;
					continue;
				}
				
				try {
					int applicationId = Integer.parseInt(input);
					
					boolean isValid = false;
					PersonalLoanApplication application = null;
					
					for(PersonalLoanApplication plapplication : loanApplications) {
						if(plapplication.getApplicationId() == applicationId) {
							application = plapplication;
							isValid = true;
						}
					}
					
					if(!isValid) {
						PrintSettings.CONSOLE.printf("Application with the id " + input + " does not exist\n");
						PrintSettings.CONSOLE.readLine("Press the return key to continue");
						continue;
					}
					
					approvePersonalLoanScreen(application);
				}catch(NumberFormatException e) {
					PrintSettings.CONSOLE.printf(input + " is not a valid application id\n");
					PrintSettings.CONSOLE.readLine("Press the return key to continue");
				}			
			}catch(NoResultsFoundException e) {
				PrintSettings.CONSOLE.printf("No open loan applications were found\n");
				PrintSettings.CONSOLE.readLine("Press the return key to continue");
				return;
			}
		}
	}
	
	/*
	 * Take the underwriter to a screen which allows them to choose an open personal loan application to view the details of then approve or reject that application
	 */
	private void approvePersonalLoanScreen(PersonalLoanApplication application) throws IOException, InterruptedException, SQLException, MessagingException {
		// Print the screen title
		List<String> title = new ArrayList<String>();
		title.add("Application information for application " + application.getApplicationId() + "\n\n");
		PrintSettings.printScreenTitle(title);
		
		try {
			// Print a table with the details of the selected personal loan
			Applicant applicant = uService.getApplicatInformation(application.getApplicantId());
			
			String format = "|%15s|%8s|%16s|\n";
			
			PrintSettings.CONSOLE.printf("Application Information\n");
			PrintSettings.CONSOLE.printf(format.replace("%", "%-"), "Application ID", "Loan ID", "Value Requested");
			PrintSettings.CONSOLE.printf(format, "--------------", "-------", "---------------");
			
			String applicationId = Integer.toString(application.getApplicantId());
			String loanId = Integer.toString(application.getLoanId());
			String requestedValue = "£" + Double.toString(application.getLoanValue());
			
			PrintSettings.CONSOLE.printf(format, applicationId, loanId, requestedValue);
			
			printApplicantInformation(applicant, application.getCreditScoreUsed());
			
			// Loop until valid input
			boolean isValid = false;
			while(!isValid) {
				String input = PrintSettings.CONSOLE.readLine("\n\nTo approve this personal loan type approve, to reject type reject, to go back type back \n");
				
				// Use a switch to check if the user has put one of the three valid inputs
				switch(input) {
					case "back"		: return;
					case "approve"	: aService.approveApplication(application, applicant, "The loan was approved by the underwriter.");
									  PrintSettings.CONSOLE.printf("Application approved\n");
									  PrintSettings.CONSOLE.readLine("Press the return key to continue");
									  isValid = true;
									  break;
					case "reject"	: aService.rejectApplication(application, applicant, "The loan was rejected by the underwriter.");
									  PrintSettings.CONSOLE.printf("Application rejected\n");
									  PrintSettings.CONSOLE.readLine("Press the return key to continue");
									  isValid = true;
									  break;
					default			: PrintSettings.CONSOLE.printf(input + " is not a vaild option\n\n");
				}
			}
		} catch (NoResultsFoundException e) {
			PrintSettings.CONSOLE.printf("Could not find information about the applicant who made this application!\n");
			PrintSettings.CONSOLE.readLine("Press the return key to continue");
			return;
		}
	}
	
	private void viewMortgageApplications() throws IOException, InterruptedException, SQLException, MessagingException {
		List<MortgageApplication> mortgageApplications;
		boolean isBack = false;
		
		while(!isBack) {
			// Print the screen title
			List<String> title = new ArrayList<String>();
			title.add("Open Mortgage Applications\n\n");
			PrintSettings.printScreenTitle(title);
			
			try {
			
				mortgageApplications = aService.getMortgageApplications("A");
			
				// Set the format so that we can print a table with all the open mortgage applications
				String format = "|%15s|%8s|%16s|%10s|\n";
				
				PrintSettings.CONSOLE.printf(format.replace("%", "%-"), "Application ID", "Loan ID", "Value Requested", "Deposit");
				PrintSettings.CONSOLE.printf(format, "--------------", "-------", "---------------", "---------");
				
				for(MortgageApplication application : mortgageApplications) {
					String applicationId = Integer.toString(application.getApplicationId());
					String mortgageId = Integer.toString(application.getMortgageId());
					String requestedValue = "£" + Double.toString(application.getLoanValue());
					String deposit = "£" + Double.toString(application.getDepositAmount());
					
					PrintSettings.CONSOLE.printf(format, applicationId, mortgageId, requestedValue, deposit);
				}
				
				// Get the user to enter the id of the application they want to look at, or back to go back
				PrintSettings.CONSOLE.printf("Type back to go back to screen selection\n");
				String input = PrintSettings.CONSOLE.readLine("Enter application id to go to approval screen\n");
				
				// Go back if back was input
				if(input.equalsIgnoreCase("back")) {
					isBack = true;
					continue;
				}
				
				try {
					int applicationId = Integer.parseInt(input);
					
					boolean isValid = false;
					MortgageApplication application = null;
					
					// Check the list of applications to check the user input was in the list
					for(MortgageApplication mapplication : mortgageApplications) {
						if(mapplication.getApplicationId() == applicationId) {
							application = mapplication;
							isValid = true;
						}
					}
					
					// If the input was not in the list then say this and loop back round
					if(!isValid) {
						PrintSettings.CONSOLE.printf("Application with the id " + input + " does not exist\n");
						PrintSettings.CONSOLE.readLine("Press the return key to continue");
						continue;
					}
					
					// Bring up the mortgage approval screen
					approveMortgageScreen(application);
				}catch(NumberFormatException e) {
					PrintSettings.CONSOLE.printf(input + " is not a valid application id\n");
					PrintSettings.CONSOLE.readLine("Press the return key to continue");
				}			
			}catch(NoResultsFoundException e) {
				PrintSettings.CONSOLE.printf("No open mortgage applications were found\n");
				PrintSettings.CONSOLE.readLine("Press the return key to continue");
				return;
			}
		}
	}
	
	/*
	 * 
	 */
	private void approveMortgageScreen(MortgageApplication application) throws IOException, InterruptedException, SQLException, MessagingException {
		// Print the screen title
		List<String> title = new ArrayList<String>();
		title.add("Application information for application " + application.getApplicationId() + "\n\n");
		PrintSettings.printScreenTitle(title);
		
		try {
			// Print a table with the details of the selected mortgage
			Applicant applicant = uService.getApplicatInformation(application.getApplicantId());

			String format = "|%15s|%8s|%16s|%10s|\n";

			PrintSettings.CONSOLE.printf("Application Information\n");
			PrintSettings.CONSOLE.printf(format.replace("%", "%-"), "Application ID", "Loan ID", "Value Requested", "Deposit");
			PrintSettings.CONSOLE.printf(format, "--------------", "-------", "---------------", "---------");
			
			String applicationId = Integer.toString(application.getApplicantId());
			String mortgageId = Integer.toString(application.getMortgageId());
			String requestedValue = "£" + Double.toString(application.getLoanValue());
			String deposit = "£" + Double.toString(application.getDepositAmount());
			
			PrintSettings.CONSOLE.printf(format, applicationId, mortgageId, requestedValue, deposit);
			
			printApplicantInformation(applicant, application.getCreditScoreUsed());		
			
			// Loop until valid input
			boolean isValid = false;
			while(!isValid) {
				String input = PrintSettings.CONSOLE.readLine("\n\nTo approve this mortgage type approve, to reject type reject, to go back type back \n");
				
				// Use a switch to check if the user has put one of the three valid inputs
				switch(input) {
					case "back"		: return;
					case "approve"	: aService.approveApplication(application, applicant, "The mortgage was approved by the underwriter.");
									  PrintSettings.CONSOLE.printf("Application approved\n");
									  PrintSettings.CONSOLE.readLine("Press the return key to continue");
									  isValid = true;
									  break;
					case "reject"	: aService.rejectApplication(application, applicant, "The mortgage was rejected by the underwriter.");
									  PrintSettings.CONSOLE.printf("Application rejected\n");
									  PrintSettings.CONSOLE.readLine("Press the return key to continue");
									  isValid = true;
									  break;
					default			: PrintSettings.CONSOLE.printf(input + " is not a vaild option\n\n");
				}
			}
		} catch (NoResultsFoundException e) {
			PrintSettings.CONSOLE.printf("Could not find information about the applicant who made this application!\n");
			PrintSettings.CONSOLE.readLine("Press the return key to continue");
			return;
		}
	}

	/*
	 * Need to display an applicant the same way in both applications so do that here
	 */
	private void printApplicantInformation(Applicant applicant, int creditScoreUsed) throws SQLException {
		// Create a format for a table to display the applicant information
		String format = "|%13s|%20s|%12s|\n";
		
		PrintSettings.CONSOLE.printf("\n\nApplicant Information\n");			
		PrintSettings.CONSOLE.printf(format.replace("%", "%-"), "Applicant ID", "Name", "Credit Score");
		PrintSettings.CONSOLE.printf(format, "------------", "-------------------", "-----------");
		
		String applicantId = Integer.toString(applicant.getUserId());
		String creditScore = Integer.toString(creditScoreUsed);
		
		PrintSettings.CONSOLE.printf(format, applicantId,applicant.getName(), creditScore);
		
		// Print any notes which a user has against their account so the underwriter can use this information
		PrintSettings.CONSOLE.printf("\nNotes on applicants account\n");
		
		List<String> notes = uService.getApplicantNotes(applicant.getUserId());
		
		if(notes.size() == 0) {
			// If there are notes print this
			PrintSettings.CONSOLE.printf("There are no notes on this account");
		}else {
			for(String note : notes) {
				PrintSettings.CONSOLE.printf("NOTE : " + note + "\n");
			}
		}
	}
}
