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
import com.fairsail.utils.GlobalSettings;

public class UnderwriterScreen {

	private ApplicationService aService;
	private UserService uService;
	
	public UnderwriterScreen(DatabaseQueryExecutor dqe) {
		aService = new ApplicationService(dqe);
		uService = new UserService(dqe);
	}
	
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
			GlobalSettings.printScreenTitle(title);
		
			String input = GlobalSettings.CONSOLE.readLine("Enter your selection : \n");
			
			// Use a switch statement to decide what to do based on user input
			switch(input.toLowerCase()) {
				case "1"		: viewPersonalLoanApplications();
								  break;
				case "2"		: viewMortgageApplications();
								  break;
				case "logout"	: isLoggedIn = false;
								  break;
				default 		: GlobalSettings.CONSOLE.printf(input + " is not a valid option\n");
								  GlobalSettings.CONSOLE.readLine("Press the return key to continue");
			}
		}
	}
	
	private void viewPersonalLoanApplications() throws IOException, InterruptedException, SQLException, MessagingException {
		List<PersonalLoanApplication> loanApplications;
		boolean isBack = false;
		
		while(!isBack) {
			// Print the screen title
			List<String> title = new ArrayList<String>();
			title.add("Open Personal Loan Applications\n\n");
			GlobalSettings.printScreenTitle(title);
			
			try {
			
			loanApplications = aService.getLoanApplications("A");
			
			
				// Set the format so that we can print a table
				String format = "|%15s|%8s|%16s||\n";
				
				GlobalSettings.CONSOLE.printf(format.replace("%", "%-"), "Application ID", "Loan ID", "Value Requested");
				GlobalSettings.CONSOLE.printf(format, "--------------", "-------", "---------------");
				
				for(PersonalLoanApplication application : loanApplications) {
					String applicationId = Integer.toString(application.getApplicationId());
					String loanId = Integer.toString(application.getLoanId());
					String requestedValue = "£" + Double.toString(application.getLoanValue());
					
					GlobalSettings.CONSOLE.printf(format, applicationId, loanId, requestedValue);
				}
				
				GlobalSettings.CONSOLE.printf("Type back to go back to screen selection\n");
				String input = GlobalSettings.CONSOLE.readLine("Enter application id to go to approval screen\n");
				
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
						GlobalSettings.CONSOLE.printf("Application with the id " + input + " does not exist\n");
						GlobalSettings.CONSOLE.readLine("Press the return key to continue");
						continue;
					}
					
					approvePersonalLoanScreen(application);
				}catch(NumberFormatException e) {
					GlobalSettings.CONSOLE.printf(input + " is not a valid application id\n");
					GlobalSettings.CONSOLE.readLine("Press the return key to continue");
				}			
			}catch(NoResultsFoundException e) {
				GlobalSettings.CONSOLE.printf("No open loan applications were found\n");
				GlobalSettings.CONSOLE.readLine("Press the return key to continue");
				return;
			}
		}
	}
	
	private void approvePersonalLoanScreen(PersonalLoanApplication application) throws IOException, InterruptedException, SQLException, MessagingException {
		// Print the screen title
		List<String> title = new ArrayList<String>();
		title.add("Application information for application " + application.getApplicationId() + "\n\n");
		GlobalSettings.printScreenTitle(title);
		
		try {
			Applicant applicant = uService.getApplicatInformation(application.getApplicantId());
			
			String format = "|%15s|%8s|%16s|\n";
			
			GlobalSettings.CONSOLE.printf("Application Information\n");
			GlobalSettings.CONSOLE.printf(format.replace("%", "%-"), "Application ID", "Loan ID", "Value Requested");
			GlobalSettings.CONSOLE.printf(format, "--------------", "-------", "---------------");
			
			String applicationId = Integer.toString(application.getApplicantId());
			String loanId = Integer.toString(application.getLoanId());
			String requestedValue = "£" + Double.toString(application.getLoanValue());
			
			GlobalSettings.CONSOLE.printf(format, applicationId, loanId, requestedValue);
			
			printApplicantInformation(applicant, application.getCreditScoreUsed());
			
			boolean isValid = false;
			while(!isValid) {
				String input = GlobalSettings.CONSOLE.readLine("\n\nTo approve this mortgage type approve, to reject type reject, to go back type back \n");
				
				switch(input) {
					case "back"		: return;
					case "approve"	: aService.approveApplication(application, applicant, "The loan was approved by the underwriter.");
									  GlobalSettings.CONSOLE.printf("Application approved\n");
									  GlobalSettings.CONSOLE.readLine("Press the return key to continue");
									  isValid = true;
									  break;
					case "reject"	: aService.rejectApplication(application, applicant, "The loan was rejected by the underwriter.");
									  GlobalSettings.CONSOLE.printf("Application rejected\n");
									  GlobalSettings.CONSOLE.readLine("Press the return key to continue");
									  isValid = true;
									  break;
					default			: GlobalSettings.CONSOLE.printf(input + " is not a vaild option\n\n");
				}
			}
		} catch (NoResultsFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void viewMortgageApplications() throws IOException, InterruptedException, SQLException, MessagingException {
		List<MortgageApplication> mortgageApplications;
		boolean isBack = false;
		
		while(!isBack) {
			// Print the screen title
			List<String> title = new ArrayList<String>();
			title.add("Open Mortgage Applications\n\n");
			GlobalSettings.printScreenTitle(title);
			
			try {
			
				mortgageApplications = aService.getMortgageApplications("A");
			
			
				// Set the format so that we can print a table
				String format = "|%15s|%8s|%16s|%10s|\n";
				
				GlobalSettings.CONSOLE.printf(format.replace("%", "%-"), "Application ID", "Loan ID", "Value Requested", "Deposit");
				GlobalSettings.CONSOLE.printf(format, "--------------", "-------", "---------------", "---------");
				
				for(MortgageApplication application : mortgageApplications) {
					String applicationId = Integer.toString(application.getApplicationId());
					String mortgageId = Integer.toString(application.getMortgageId());
					String requestedValue = "£" + Double.toString(application.getLoanValue());
					String deposit = "£" + Double.toString(application.getDepositAmount());
					
					GlobalSettings.CONSOLE.printf(format, applicationId, mortgageId, requestedValue, deposit);
				}
				
				GlobalSettings.CONSOLE.printf("Type back to go back to screen selection\n");
				String input = GlobalSettings.CONSOLE.readLine("Enter application id to go to approval screen\n");
				
				if(input.equalsIgnoreCase("back")) {
					isBack = true;
					continue;
				}
				
				try {
					int applicationId = Integer.parseInt(input);
					
					boolean isValid = false;
					MortgageApplication application = null;
					
					for(MortgageApplication mapplication : mortgageApplications) {
						if(mapplication.getApplicationId() == applicationId) {
							application = mapplication;
							isValid = true;
						}
					}
					
					if(!isValid) {
						GlobalSettings.CONSOLE.printf("Application with the id " + input + " does not exist\n");
						GlobalSettings.CONSOLE.readLine("Press the return key to continue");
						continue;
					}
					
					approveMortgageScreen(application);
				}catch(NumberFormatException e) {
					GlobalSettings.CONSOLE.printf(input + " is not a valid application id\n");
					GlobalSettings.CONSOLE.readLine("Press the return key to continue");
				}			
			}catch(NoResultsFoundException e) {
				GlobalSettings.CONSOLE.printf("No open mortgage applications were found\n");
				GlobalSettings.CONSOLE.readLine("Press the return key to continue");
				return;
			}
		}
	}
	
	private void approveMortgageScreen(MortgageApplication application) throws IOException, InterruptedException, SQLException, MessagingException {
		// Print the screen title
		List<String> title = new ArrayList<String>();
		title.add("Application information for application " + application.getApplicationId() + "\n\n");
		GlobalSettings.printScreenTitle(title);
		
		try {
			Applicant applicant = uService.getApplicatInformation(application.getApplicantId());

			String format = "|%15s|%8s|%16s|%10s|\n";

			GlobalSettings.CONSOLE.printf("Application Information\n");
			GlobalSettings.CONSOLE.printf(format.replace("%", "%-"), "Application ID", "Loan ID", "Value Requested", "Deposit");
			GlobalSettings.CONSOLE.printf(format, "--------------", "-------", "---------------", "---------");
			
			String applicationId = Integer.toString(application.getApplicantId());
			String mortgageId = Integer.toString(application.getMortgageId());
			String requestedValue = "£" + Double.toString(application.getLoanValue());
			String deposit = "£" + Double.toString(application.getDepositAmount());
			
			GlobalSettings.CONSOLE.printf(format, applicationId, mortgageId, requestedValue, deposit);
			
			printApplicantInformation(applicant, application.getCreditScoreUsed());		
			
			boolean isValid = false;
			while(!isValid) {
				String input = GlobalSettings.CONSOLE.readLine("\n\nTo approve this mortgage type approve, to reject type reject, to go back type back \n");
				
				switch(input) {
					case "back"		: return;
					case "approve"	: aService.approveApplication(application, applicant, "The mortgage was approved by the underwriter.");
									  GlobalSettings.CONSOLE.printf("Application approved\n");
									  GlobalSettings.CONSOLE.readLine("Press the return key to continue");
									  isValid = true;
									  break;
					case "reject"	: aService.rejectApplication(application, applicant, "The mortgage was rejected by the underwriter.");
									  GlobalSettings.CONSOLE.printf("Application rejected\n");
									  GlobalSettings.CONSOLE.readLine("Press the return key to continue");
									  isValid = true;
									  break;
					default			: GlobalSettings.CONSOLE.printf(input + " is not a vaild option\n\n");
				}
			}
		} catch (NoResultsFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void printApplicantInformation(Applicant applicant, int creditScoreUsed) {
		String format = "|%13s|%20s|%16s|\n";
		
		GlobalSettings.CONSOLE.printf("\n\nApplicant Information\n");			
		GlobalSettings.CONSOLE.printf(format.replace("%", "%-"), "Applicant ID", "Name", "Credit Score");
		GlobalSettings.CONSOLE.printf(format, "------------", "-------------------", "-----------");
		
		String applicantId = Integer.toString(applicant.getUserId());
		String creditScore = Integer.toString(creditScoreUsed);
		
		GlobalSettings.CONSOLE.printf(format, applicantId, creditScore, applicant.getName());
		
		GlobalSettings.CONSOLE.printf("\nNotes on applicants account\n");
		
		if(applicant.getNotes().size() == 0) {
			GlobalSettings.CONSOLE.printf("There are no notes on this account");
		}else {
			for(String note : applicant.getNotes()) {
				GlobalSettings.CONSOLE.printf("NOTE : " + note + "\n");
			}
		}
	}
}
