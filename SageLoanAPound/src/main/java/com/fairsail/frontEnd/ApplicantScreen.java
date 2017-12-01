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
import com.fairsail.utils.GlobalSettings;

public class ApplicantScreen {
	
	private MortgageService mService;
	private PersonalLoanService pService;
	private ApplicationService aService;
	private ViewProductsScreen vps;
	
	public ApplicantScreen(DatabaseQueryExecutor dqe) {
		mService = new MortgageService(dqe);
		pService = new PersonalLoanService(dqe);
		aService = new ApplicationService(dqe);
		
		vps = new ViewProductsScreen(mService, pService);
	}
	
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
			GlobalSettings.printScreenTitle(title);
		
			String input = GlobalSettings.CONSOLE.readLine("Enter your selection : \n");
			
			switch(input) {
				case "1"		: vps.viewProducts();
								  break;
				case "2"		: applicationScreen(currentUser);
								  break;
				case "3"		: viewPreviousApplicationsScreen(currentUser);
								  GlobalSettings.CONSOLE.readLine("Press the return key to continue");
								  break;
				case "logout"	: isLoggedIn = false;
								  break;
				default 		: GlobalSettings.CONSOLE.printf(input + " is not a valid option\n");
								  GlobalSettings.CONSOLE.readLine("Press the return key to continue");
			}
		}
	}
	
	private void applicationScreen(Applicant currentUser) throws IOException, InterruptedException, SQLException {
		boolean isNotBack = true;
		
		// Print the screen title
		while(isNotBack) {
			List<String> title = new ArrayList<String>();
			title.add("Create a new product\n\n");
			title.add("----------------------------------------\n\n");
			title.add("Please select from the following options by entering the correct number\n");
			title.add("  1) Apply for a mortgage\n");
			title.add("  2) Apply for a loan\n");
			title.add("\nType back to return to previous screen\n");
			GlobalSettings.printScreenTitle(title);
			
			String input = GlobalSettings.CONSOLE.readLine("Enter your selection : \n");
			
			switch(input) {
				case "1"		: applyForMortgageScreen(currentUser);
								  break;
				case "2"		: applyForPersonalLoanScreen(currentUser);
								  break;
				case "back"		: isNotBack = false;
								  break;
				default 		: GlobalSettings.CONSOLE.printf(input + " is not a valid option\n");
								  GlobalSettings.CONSOLE.readLine("Press the return key to continue");
			}
		}
	}
	
	private void applyForMortgageScreen(Applicant currentUser) throws IOException, InterruptedException, SQLException {
		boolean isValid = false;
		int mortgageId = 0;
		String input = null;
		
		while(!isValid) {
			try {
			Map<Integer, Mortgage> mortgages = mService.getMortgages();
			vps.viewMortgages();
			
			try {
				GlobalSettings.CONSOLE.printf("To go back without applying for a mortgage type back\n");
				input = GlobalSettings.CONSOLE.readLine("Please enter the id of the mortgage you wish to apply for : \n");
				
				if(input.equals("back")) {
					return;
				}
				
				mortgageId = Integer.parseInt(input);
				
				if(mortgages.containsKey(mortgageId)) {
					MortgageApplication application = new MortgageApplication(currentUser, mortgageId);
					aService.createNewMortgageApplication(new ApplicationInputChecker().mortgageApplicationInputChecker(application));
					isValid = true;
				}else {
					GlobalSettings.CONSOLE.printf(input + " was not found in the list of id's, please enter a valid id\n");
					GlobalSettings.CONSOLE.readLine("Press the return key to continue");
				}
			}catch(NumberFormatException e) {
				GlobalSettings.CONSOLE.printf(input + " is not a valid id, please enter a valid id\n");
				GlobalSettings.CONSOLE.readLine("Press the return key to continue");
			}
			}catch(NoResultsFoundException e){
				// If no mortgages are found display a message then go back to the previous screen
				GlobalSettings.CONSOLE.printf("Either there are no mortgages or you were not found to be eligible for any of the mortgages that were found\n");
				GlobalSettings.CONSOLE.readLine("Press the return key to continue");
				return;
			}
		}
	}
	
	private void applyForPersonalLoanScreen(Applicant currentUser) throws IOException, InterruptedException, SQLException {
		boolean isValid = false;
		int loanId = 0;
		String input = null;
		
		while(!isValid) {
			try {
				Map<Integer, PersonalLoan> loans = pService.getPersonalLoans();
				vps.viewLoans();
				
				try {
					GlobalSettings.CONSOLE.printf("To go back without applying for a loan type back\n");
					input = GlobalSettings.CONSOLE.readLine("Please enter the id of the loan you wish to apply for : \n");
					
					if(input.equalsIgnoreCase("back")) {
						return;
					}
					
					loanId = Integer.parseInt(input);
					
					if(loans.containsKey(loanId)) {
						PersonalLoanApplication application = new PersonalLoanApplication(currentUser, loanId);
						aService.createNewPersonalLoanApplication(new ApplicationInputChecker().personalLoanApplicationInputChecker(application));
						isValid = true;
					}else {
						GlobalSettings.CONSOLE.printf(input + " was not found in the list of id's, please enter a valid id\n");
						GlobalSettings.CONSOLE.readLine("Press the return key to continue");
					}
				}catch(NumberFormatException e) {
					GlobalSettings.CONSOLE.printf(input + " is not a valid id, please enter a valid id\n");
					GlobalSettings.CONSOLE.readLine("Press the return key to continue");
				}
			}catch(NoResultsFoundException e){
				// If no personal loans are found display a message then go back to the previous screen
				GlobalSettings.CONSOLE.printf("Either there are no personal loans or you were not found to be eligible for any of the personal loans that were found\n");
				GlobalSettings.CONSOLE.readLine("Press the return key to continue");
				return;
			}
		}
	}
	
	private void viewPreviousApplicationsScreen(Applicant currentUser) throws SQLException, IOException, InterruptedException {
		List<PersonalLoanApplication> loanApplications;
		List<MortgageApplication> mortgageApplications;
		
		// Print the screen title
		List<String> title = new ArrayList<String>();
		title.add("Existing applications\n\n");
		GlobalSettings.printScreenTitle(title);
		
		try {
			loanApplications = aService.getLoanApplications(currentUser);
			
			// Set the format so that we can print a table
			String format = "|%15s|%8s|%16s|%19s|\n";
			
			GlobalSettings.CONSOLE.printf("\n\nOpen Personal Loan Applications\n");
			GlobalSettings.CONSOLE.printf(format.replace("%", "%-"), "Application ID", "Loan ID", "Value Requested", "Application Status");
			GlobalSettings.CONSOLE.printf(format, "--------------", "-------", "---------------", "------------------");
			
			for(PersonalLoanApplication application : loanApplications) {
				if(application.getApplicationStatus().equals("U") || application.getApplicationStatus().equals("A")) {
					String applicationId = Integer.toString(application.getApplicantId());
					String loanId = Integer.toString(application.getLoanId());
					String requestedValue = "£" + Double.toString(application.getLoanValue());
					String status = application.getApplicationStatus().equals("U") ? "Unchecked" : "Awaiting approval";
					
					GlobalSettings.CONSOLE.printf(format, applicationId, loanId, requestedValue, status);
				}
			}
			
			GlobalSettings.CONSOLE.printf("\nClosed Personal Loan Applications\n");
			GlobalSettings.CONSOLE.printf(format.replace("%", "%-"), "Application ID", "Loan ID", "Value Requested", "Application Status");
			GlobalSettings.CONSOLE.printf(format, "--------------", "-------", "---------------", "------------------");
			
			for(PersonalLoanApplication application : loanApplications) {
				if(application.getApplicationStatus().equals("C") || application.getApplicationStatus().equals("R")) {
					String applicationId = Integer.toString(application.getApplicantId());
					String loanId = Integer.toString(application.getLoanId());
					String requestedValue = "£" + Double.toString(application.getLoanValue());
					String status = application.getApplicationStatus().equals("C") ? "Approved" : "Rejected";
					
					GlobalSettings.CONSOLE.printf(format, applicationId, loanId, requestedValue, status);
				}
			}
		}catch (NoResultsFoundException e) {
			GlobalSettings.CONSOLE.printf("No personal loan applications were found\n");
		}
		
		try {
			mortgageApplications = aService.getMortgageApplications(currentUser);
			
			// Set the format so that we can print a table
			String format = "|%15s|%12s|%16s|%13s|%19s|\n";
			
			GlobalSettings.CONSOLE.printf("\n\nOpen Mortgage Applications\n");
			GlobalSettings.CONSOLE.printf(format.replace("%", "%-"), "Application ID", "Mortgage ID", "Value Requested", "Deposit", "Application Status");
			GlobalSettings.CONSOLE.printf(format, "--------------", "-----------", "---------------", "------------", "------------------");
			
			for(MortgageApplication application : mortgageApplications) {
				if(application.getApplicationStatus().equals("U") || application.getApplicationStatus().equals("A")) {
					String applicationId = Integer.toString(application.getApplicantId());
					String loanId = Integer.toString(application.getMortgageId());
					String requestedValue = "£" + Double.toString(application.getLoanValue());
					String deposit = "£" + Double.toString(application.getDepositAmount());
					String status = application.getApplicationStatus().equals("U") ? "Unchecked" : "Awaiting approval";
					
					GlobalSettings.CONSOLE.printf(format, applicationId, loanId, requestedValue, deposit, status);
				}
			}
			
			GlobalSettings.CONSOLE.printf("\nClosed Mortgage Applications\n");
			GlobalSettings.CONSOLE.printf(format.replace("%", "%-"), "Application ID", "Mortgage ID", "Value Requested", "Deposit", "Application Status");
			GlobalSettings.CONSOLE.printf(format, "--------------", "-----------", "---------------", "------------", "------------------");
			
			for(MortgageApplication application : mortgageApplications) {
				if(application.getApplicationStatus().equals("C") || application.getApplicationStatus().equals("R")) {
					String applicationId = Integer.toString(application.getApplicantId());
					String loanId = Integer.toString(application.getMortgageId());
					String requestedValue = "£" + Double.toString(application.getLoanValue());
					String deposit = "£" + Double.toString(application.getDepositAmount());
					String status = application.getApplicationStatus().equals("C") ? "Approved" : "Rejected";
					
					GlobalSettings.CONSOLE.printf(format, applicationId, loanId, requestedValue, deposit, status);
				}
			}
		}catch (NoResultsFoundException e) {
			GlobalSettings.CONSOLE.printf("No mortgage applications were found\n");
		}
	}
}
