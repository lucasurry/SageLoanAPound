package com.fairsail.frontEnd;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fairsail.exceptions.NoResultsFoundException;
import com.fairsail.loan.Mortgage;
import com.fairsail.loan.PersonalLoan;
import com.fairsail.service.MortgageService;
import com.fairsail.service.PersonalLoanService;
import com.fairsail.utils.PrintSettings;

public class ViewProductsScreen {
	
	private MortgageService mService;
	private PersonalLoanService pService;
	
	/**
	 * Displays screens used to see what products exist for administrators and applicants. 
	 * 
	 * @param mService
	 * @param pService
	 */
	public ViewProductsScreen (MortgageService mService, PersonalLoanService pService) {
		this.mService = mService;
		this.pService = pService;
	}
	
	/**
	 * Produces a screen which is a menu to choose what product type to view
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	public void viewProducts() throws IOException, InterruptedException, SQLException {
		boolean isNotBack = true;
		
		// Loop until the user wants to go back
		while(isNotBack) {
			// Print the screen title
			List<String> title = new ArrayList<String>();
			title.add("Product view\n\n");
			title.add("----------------------------------------\n\n");
			title.add("Please select from the following options by entering the correct number\n");
			title.add("  1) View mortgages\n");
			title.add("  2) View loans\n");
			title.add("\nType back to return to the previous screen\n");
			PrintSettings.printScreenTitle(title);
			
			String input = PrintSettings.CONSOLE.readLine("Enter your selection : \n");
			
			// Check the user input
			switch(input) {
				case "1"		: viewMortgages();
								  PrintSettings.CONSOLE.readLine("Press the return key to continue");
								  break;
				case "2"		: viewLoans();
								  PrintSettings.CONSOLE.readLine("Press the return key to continue");
				                  break;
				case "back"		: isNotBack = false;
								  break;
				default 		: PrintSettings.CONSOLE.printf(input + " is not a valid option\n");
								  PrintSettings.CONSOLE.readLine("Press the return key to continue");
			}
		}
	}
	
	/**
	 * Displays a screen which is a table of all mortgages which an applicant can apply for
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	public void viewMortgages() throws IOException, InterruptedException, SQLException {
		Map<Integer, Mortgage> mortgages;
		
		// Print the screen title
		List<String> title = new ArrayList<String>();
		title.add("Advailable mortgages\n\n");
		PrintSettings.printScreenTitle(title);
		
		try {
			mortgages = mService.getMortgages();
			
			// Set the format so that we can print a table of mortgages
			String format = "|%5s|%20s|%18s|%14s|%8s|%5s|%10s|%10s|\n";
			
			// Add the table headers
			PrintSettings.CONSOLE.printf(format.replace("%", "%-"), "ID", "Lender", "No. of Repayments", "Min Deposit", "Fees", "Rate", "Min Value", "Max Value");
			PrintSettings.CONSOLE.printf(format, "----", "-------------------", "-----------------", "-------------", "-------", "----", "---------", "---------");
			
			// Loop through all the mortgages and add them to the table
			for(int mortageId : mortgages.keySet()){
				Mortgage mortgage = mortgages.get(mortageId);
				
				String id = Integer.toString(mortageId);
				String lender = mortgage.getLender();
				String repaymentMonths = Integer.toString(mortgage.getRepaymentMonths());
				String minDeposit = null;
				
				if(mortgage.isMinDepositIsPercent()) {
					minDeposit = Double.toString(mortgage.getMinimumDeposit()) + "%";
				}else {
					minDeposit = "£" + Double.toString(mortgage.getMinimumDeposit());
				}
				
				String fees = "£" + Double.toString(mortgage.getFees());
				String rate = Double.toString(mortgage.getInterestRate()) + "%";
				String minVal = "£" + Double.toString(mortgage.getMinimumValue());
				String maxVal = "£" + Double.toString(mortgage.getMaximumValue());
				
				
				PrintSettings.CONSOLE.printf(format, id, lender, repaymentMonths, minDeposit, fees, rate, minVal, maxVal);
				
			}
		}catch (NoResultsFoundException e) {
			PrintSettings.CONSOLE.printf("No mortgages could be found in the system");
			PrintSettings.CONSOLE.readLine("Press the return key to continue");
		}
	}
	
	/**
	 * Displays a screen which is a table of all personal loans which an applicant can apply for
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	public void viewLoans() throws IOException, InterruptedException, SQLException {
		Map<Integer, PersonalLoan> personalLoans;
		
		// Print the screen title
		List<String> title = new ArrayList<String>();
		title.add("Advailable loans\n\n");
		PrintSettings.printScreenTitle(title);

		try{
			personalLoans = pService.getPersonalLoans();
			
			if(personalLoans != null && personalLoans.size() > 0) {
				// Set the format so that we can print a table
				String format = "|%5s|%20s|%18s|%5s|%10s|%10s|\n";
				
				// Add the table headers
				PrintSettings.CONSOLE.printf(format.replace("%", "%-"), "ID", "Lender", "No. of Repayments", "Rate", "Min Value", "Max Value");
				PrintSettings.CONSOLE.printf(format, "----", "-------------------", "-----------------", "----", "---------", "---------");
			
				// Loop through all the personal loans and add them to the table
				for(int loanId : personalLoans.keySet()) {
					PersonalLoan loan = personalLoans.get(loanId);
						
					String id = Integer.toString(loanId);
					String lender = loan.getLender();
					String repaymentMonths = Integer.toString(loan.getRepaymentMonths());
					String rate = Double.toString(loan.getInterestRate()) + "%";
					String minVal = "£" + Double.toString(loan.getMinimumValue());
					String maxVal = "£" + Double.toString(loan.getMaximumValue());
					
					PrintSettings.CONSOLE.printf(format, id, lender, repaymentMonths, rate, minVal, maxVal);
				}
			}
		}catch (NoResultsFoundException e) {
			PrintSettings.CONSOLE.printf("No mortgages could be found in the system");
			PrintSettings.CONSOLE.readLine("Press the return key to continue");
		}
	}
}
