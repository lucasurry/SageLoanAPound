package com.fairsail.frontEnd;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fairsail.accounts.Applicant;
import com.fairsail.applications.MortgageApplication;
import com.fairsail.applications.PersonalLoanApplication;
import com.fairsail.dao.DatabaseQueryExecutor;
import com.fairsail.exceptions.NoResultsFoundException;
import com.fairsail.loan.Mortgage;
import com.fairsail.loan.PersonalLoan;
import com.fairsail.service.ApplicationService;
import com.fairsail.service.MortgageService;
import com.fairsail.service.PersonalLoanService;
import com.fairsail.utils.PrintSettings;

public class ApplicantScreen {
	
	private MortgageService mService;
	private PersonalLoanService pService;
	private ApplicationService aService;
	private ViewProductsScreen vps;
	
	/**
	 * Displays the screen for applicants. Allows them to view existing products, create applications for these products or view the state of applications which they have raised in the past.
	 * 
	 * @param DatabaseQueryExecutor
	 */
	public ApplicantScreen(DatabaseQueryExecutor dqe) {
		mService = new MortgageService(dqe);
		pService = new PersonalLoanService(dqe);
		aService = new ApplicationService(dqe);
		
		vps = new ViewProductsScreen(mService, pService);
	}
	
	/**
	 * Display the main menu for an applicant user
	 * 
	 * @param Applicant currentUser
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	public void applicantScreen(Applicant currentUser) throws IOException, InterruptedException, SQLException {
		boolean isLoggedIn = true;
		
		while(isLoggedIn) {
			// Print the screen title
			List<String> title = new ArrayList<String>();
			title.add("Logged in as an applicant\n\n");
			title.add("----------------------------------------\n\n");
			title.add("Please select from the following options by entering the correct number\n");
			title.add("  1) View products\n");
			title.add("  2) Apply for a product\n");
			title.add("  3) View existing application status\n");
			title.add("\nType logout to exit\n");
			PrintSettings.printScreenTitle(title);
		
			String input = PrintSettings.CONSOLE.readLine("Enter your selection : \n");
			
			// Check their input
			switch(input) {
				case "1"		: vps.viewProducts();
								  break;
				case "2"		: applicationScreen(currentUser);
								  break;
				case "3"		: viewPreviousApplicationsScreen(currentUser);
								  PrintSettings.CONSOLE.readLine("Press the return key to continue");
								  break;
				case "logout"	: isLoggedIn = false;
								  break;
				default 		: PrintSettings.CONSOLE.printf(input + " is not a valid option\n");
								  PrintSettings.CONSOLE.readLine("Press the return key to continue");
			}
		}
	}
	
	/**
	 * Display a screen which the applicant can use to choose a type of product they want to create an application for
	 * 
	 * @param Applicant currentUser
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	private void applicationScreen(Applicant currentUser) throws IOException, InterruptedException, SQLException {
		boolean isNotBack = true;
		
		// Loop until valid input
		while(isNotBack) {
			// Print the screen title
			List<String> title = new ArrayList<String>();
			title.add("Create a new product\n\n");
			title.add("----------------------------------------\n\n");
			title.add("Please select from the following options by entering the correct number\n");
			title.add("  1) Apply for a mortgage\n");
			title.add("  2) Apply for a loan\n");
			title.add("\nType back to return to previous screen\n");
			PrintSettings.printScreenTitle(title);
			
			String input = PrintSettings.CONSOLE.readLine("Enter your selection : \n");
			
			// Check the user input
			switch(input) {
				case "1"		: applyForMortgageScreen(currentUser);
								  break;
				case "2"		: applyForPersonalLoanScreen(currentUser);
								  break;
				case "back"		: isNotBack = false;
								  break;
				default 		: PrintSettings.CONSOLE.printf(input + " is not a valid option\n");
								  PrintSettings.CONSOLE.readLine("Press the return key to continue");
			}
		}
	}
	
	/**
	 * Display a screen which the applicant can use to create an application for a mortgage
	 * 
	 * @param Applicant currentUser
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	private void applyForMortgageScreen(Applicant currentUser) throws IOException, InterruptedException, SQLException {
		boolean isValid = false;
		int mortgageId = 0;
		String input = null;
		
		// Loop until there is a valid input
		while(!isValid) {
			try {
				// View the existing mortgage products so that the user can see the id of the mortgage they want to apply for
				Map<Integer, Mortgage> mortgages = mService.getMortgages();
				vps.viewMortgages();
				
				try {
					PrintSettings.CONSOLE.printf("To go back without applying for a mortgage type back\n");
					input = PrintSettings.CONSOLE.readLine("Please enter the id of the mortgage you wish to apply for : \n");
					
					// If input is back don't apply for anything
					if(input.equals("back")) {
						return;
					}
					
					mortgageId = Integer.parseInt(input);
					
					// Check the mortgages to find the one which the user has entered the id for
					if(mortgages.containsKey(mortgageId)) {
						MortgageApplication application = new MortgageApplication(currentUser.getUserId(), mortgageId);
						// Get the information (value they want to borrow etc.) from the user and create the application in the database
						aService.createNewMortgageApplication(new ApplicationInputChecker().mortgageApplicationInputChecker(application));
						isValid = true;
					}else {
						// If the id is not found in the list of mortgages then say this and start again
						PrintSettings.CONSOLE.printf(input + " was not found in the list of id's, please enter a valid id\n");
						PrintSettings.CONSOLE.readLine("Press the return key to continue");
					}
				}catch(NumberFormatException e) {
					// If the input was not a number it isn't valid
					PrintSettings.CONSOLE.printf(input + " is not a valid id, please enter a valid id\n");
					PrintSettings.CONSOLE.readLine("Press the return key to continue");
				}
			}catch(NoResultsFoundException e){
				// If no mortgages are found display a message then go back to the previous screen
				PrintSettings.CONSOLE.printf("No mortgages could be found to apply for\n");
				PrintSettings.CONSOLE.readLine("Press the return key to continue");
				return;
			}
		}
	}
	
	/**
	 * Display a screen which the applicant can use to create an application for a personal loan
	 * 
	 * @param Applicant currentUser
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	private void applyForPersonalLoanScreen(Applicant currentUser) throws IOException, InterruptedException, SQLException {
		boolean isValid = false;
		int loanId = 0;
		String input = null;
		
		// Loop until there is a valid input
		while(!isValid) {
			try {
				// View the existing personal loan products so that the user can see the id of the personal loan they want to apply for
				Map<Integer, PersonalLoan> loans = pService.getPersonalLoans();
				vps.viewLoans();
				
				try {
					PrintSettings.CONSOLE.printf("To go back without applying for a loan type back\n");
					input = PrintSettings.CONSOLE.readLine("Please enter the id of the loan you wish to apply for : \n");
					
					// If input is back don't apply for anything
					if(input.equalsIgnoreCase("back")) {
						return;
					}
					
					loanId = Integer.parseInt(input);
					
					// Check the personal loan to find the one which the user has entered the id for
					if(loans.containsKey(loanId)) {
						PersonalLoanApplication application = new PersonalLoanApplication(currentUser.getUserId(), loanId);
						// Get the information (value they want to borrow etc.) from the user and create the application in the database
						aService.createNewPersonalLoanApplication(new ApplicationInputChecker().personalLoanApplicationInputChecker(application));
						isValid = true;
					}else {
						// If the id is not found in the list of personal loans then say this and start again
						PrintSettings.CONSOLE.printf(input + " was not found in the list of id's, please enter a valid id\n");
						PrintSettings.CONSOLE.readLine("Press the return key to continue");
					}
				}catch(NumberFormatException e) {
					// If the input was not a number it isn't valid
					PrintSettings.CONSOLE.printf(input + " is not a valid id, please enter a valid id\n");
					PrintSettings.CONSOLE.readLine("Press the return key to continue");
				}
			}catch(NoResultsFoundException e){
				// If no personal loans are found display a message then go back to the previous screen
				PrintSettings.CONSOLE.printf("No personal loans  could be found to apply for\n");
				PrintSettings.CONSOLE.readLine("Press the return key to continue");
				return;
			}
		}
	}
	
	/**
	 * Display a screen which the applicant can use to view previous applications they have created in the past
	 * 
	 * @param Applicant currentUser
	 * @throws SQLException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private void viewPreviousApplicationsScreen(Applicant currentUser) throws SQLException, IOException, InterruptedException {
		List<PersonalLoanApplication> loanApplications;
		List<MortgageApplication> mortgageApplications;
		
		// Print the screen title
		List<String> title = new ArrayList<String>();
		title.add("Existing applications\n\n");
		PrintSettings.printScreenTitle(title);
		
		try {
			// Get a list of the personal loan applications of the current user
			loanApplications = aService.getLoanApplications(currentUser);
			
			// Set the format so that we can print a table
			String format = "|%15s|%8s|%16s|%19s|\n";
			
			// Print the table headers for open personal loan applications
			PrintSettings.CONSOLE.printf("\n\nOpen Personal Loan Applications\n");
			PrintSettings.CONSOLE.printf(format.replace("%", "%-"), "Application ID", "Loan ID", "Value Requested", "Application Status");
			PrintSettings.CONSOLE.printf(format, "--------------", "-------", "---------------", "------------------");
			
			// Print the information about all open personal loan applications
			for(PersonalLoanApplication application : loanApplications) {
				if(application.getApplicationStatus().equals("U") || application.getApplicationStatus().equals("A")) {
					String applicationId = Integer.toString(application.getApplicantId());
					String loanId = Integer.toString(application.getLoanId());
					String requestedValue = "£" + Double.toString(application.getLoanValue());
					String status = application.getApplicationStatus().equals("U") ? "Unchecked" : "Awaiting approval";
					
					PrintSettings.CONSOLE.printf(format, applicationId, loanId, requestedValue, status);
				}
			}
			
			// Print the table headers for closed or rejected personal loan applications
			PrintSettings.CONSOLE.printf("\nClosed Personal Loan Applications\n");
			PrintSettings.CONSOLE.printf(format.replace("%", "%-"), "Application ID", "Loan ID", "Value Requested", "Application Status");
			PrintSettings.CONSOLE.printf(format, "--------------", "-------", "---------------", "------------------");
			
			// Print the information about all closed or rejected personal loan applications
			for(PersonalLoanApplication application : loanApplications) {
				if(application.getApplicationStatus().equals("C") || application.getApplicationStatus().equals("R")) {
					String applicationId = Integer.toString(application.getApplicantId());
					String loanId = Integer.toString(application.getLoanId());
					String requestedValue = "£" + Double.toString(application.getLoanValue());
					String status = application.getApplicationStatus().equals("C") ? "Approved" : "Rejected";
					
					PrintSettings.CONSOLE.printf(format, applicationId, loanId, requestedValue, status);
				}
			}
		}catch (NoResultsFoundException e) {
			PrintSettings.CONSOLE.printf("No personal loan applications were found\n");
		}
		
		try {
			mortgageApplications = aService.getMortgageApplications(currentUser);
			
			// Set the format so that we can print a table
			String format = "|%15s|%12s|%16s|%13s|%19s|\n";
			
			// Print the table headers for open mortgage applications
			PrintSettings.CONSOLE.printf("\n\nOpen Mortgage Applications\n");
			PrintSettings.CONSOLE.printf(format.replace("%", "%-"), "Application ID", "Mortgage ID", "Value Requested", "Deposit", "Application Status");
			PrintSettings.CONSOLE.printf(format, "--------------", "-----------", "---------------", "------------", "------------------");
			
			// Print the information about all open mortgage applications
			for(MortgageApplication application : mortgageApplications) {
				if(application.getApplicationStatus().equals("U") || application.getApplicationStatus().equals("A")) {
					String applicationId = Integer.toString(application.getApplicantId());
					String loanId = Integer.toString(application.getMortgageId());
					String requestedValue = "£" + Double.toString(application.getLoanValue());
					String deposit = "£" + Double.toString(application.getDepositAmount());
					String status = application.getApplicationStatus().equals("U") ? "Unchecked" : "Awaiting approval";
					
					PrintSettings.CONSOLE.printf(format, applicationId, loanId, requestedValue, deposit, status);
				}
			}
			
			// Print the table headers for closed or rejected mortgage applications
			PrintSettings.CONSOLE.printf("\nClosed Mortgage Applications\n");
			PrintSettings.CONSOLE.printf(format.replace("%", "%-"), "Application ID", "Mortgage ID", "Value Requested", "Deposit", "Application Status");
			PrintSettings.CONSOLE.printf(format, "--------------", "-----------", "---------------", "------------", "------------------");
			
			// Print the information about all closed or rejected mortgage applications
			for(MortgageApplication application : mortgageApplications) {
				if(application.getApplicationStatus().equals("C") || application.getApplicationStatus().equals("R")) {
					String applicationId = Integer.toString(application.getApplicantId());
					String loanId = Integer.toString(application.getMortgageId());
					String requestedValue = "£" + Double.toString(application.getLoanValue());
					String deposit = "£" + Double.toString(application.getDepositAmount());
					String status = application.getApplicationStatus().equals("C") ? "Approved" : "Rejected";
					
					PrintSettings.CONSOLE.printf(format, applicationId, loanId, requestedValue, deposit, status);
				}
			}
		}catch (NoResultsFoundException e) {
			PrintSettings.CONSOLE.printf("No mortgage applications were found\n");
		}
	}
}
