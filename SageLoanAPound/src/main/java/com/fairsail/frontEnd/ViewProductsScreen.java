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
import com.fairsail.utils.GlobalSettings;

public class ViewProductsScreen {
	
	private MortgageService mService;
	private PersonalLoanService pService;
	
	public ViewProductsScreen (MortgageService mService, PersonalLoanService pService) {
		this.mService = mService;
		this.pService = pService;
	}
	
	public void viewProducts() throws IOException, InterruptedException, SQLException {
		boolean isNotBack = true;
		
		while(isNotBack) {
			// Print the screen title
			List<String> title = new ArrayList<String>();
			title.add("Product view\n\n");
			title.add("----------------------------------------\n\n");
			title.add("Please select from the following options by entering the correct number\n");
			title.add("  1) View mortgages\n");
			title.add("  2) View loans\n");
			title.add("\nType back to return to the previous screen\n");
			GlobalSettings.printScreenTitle(title);
			
			String input = GlobalSettings.CONSOLE.readLine("Enter your selection : \n");
			
			switch(input) {
				case "1"		: viewMortgages();
								  GlobalSettings.CONSOLE.readLine("Press the return key to continue");
								  break;
				case "2"		: viewLoans();
								  GlobalSettings.CONSOLE.readLine("Press the return key to continue");
				                  break;
				case "back"		: isNotBack = false;
								  break;
				default 		: GlobalSettings.CONSOLE.printf(input + " is not a valid option\n");
								  GlobalSettings.CONSOLE.readLine("Press the return key to continue");
			}
		}
	}
	
	public void viewMortgages() throws IOException, InterruptedException, SQLException {
		Map<Integer, Mortgage> mortgages;
		
		// Print the screen title
		List<String> title = new ArrayList<String>();
		title.add("Advailable mortgages\n\n");
		GlobalSettings.printScreenTitle(title);
		
		try {
			mortgages = mService.getMortgages();
			
			// Set the format so that we can print a table
			String format = "|%5s|%20s|%18s|%14s|%8s|%5s|%10s|%10s|\n";
			
			GlobalSettings.CONSOLE.printf(format.replace("%", "%-"), "ID", "Lender", "No. of Repayments", "Min Deposit", "Fees", "Rate", "Min Value", "Max Value");
			GlobalSettings.CONSOLE.printf(format, "----", "-------------------", "-----------------", "-------------", "-------", "----", "---------", "---------");
			
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
				
				
				GlobalSettings.CONSOLE.printf(format, id, lender, repaymentMonths, minDeposit, fees, rate, minVal, maxVal);
				
			}
		}catch (NoResultsFoundException e) {
			GlobalSettings.CONSOLE.printf("No mortgages could be found in the system");
		}
	}
	
	public void viewLoans() throws IOException, InterruptedException, SQLException {
		Map<Integer, PersonalLoan> personalLoans;
		
		// Print the screen title
		List<String> title = new ArrayList<String>();
		title.add("Advailable loans\n\n");
		GlobalSettings.printScreenTitle(title);

		try{
			personalLoans = pService.getPersonalLoans();
			
			if(personalLoans != null && personalLoans.size() > 0) {
				// Set the format so that we can print a table
				String format = "|%5s|%20s|%18s|%5s|%10s|%10s|\n";
				
				// add the table headers
				GlobalSettings.CONSOLE.printf(format.replace("%", "%-"), "ID", "Lender", "No. of Repayments", "Rate", "Min Value", "Max Value");
				GlobalSettings.CONSOLE.printf(format, "----", "-------------------", "-----------------", "----", "---------", "---------");
			
				for(int loanId : personalLoans.keySet()) {
					PersonalLoan loan = personalLoans.get(loanId);
						
					String id = Integer.toString(loanId);
					String lender = loan.getLender();
					String repaymentMonths = Integer.toString(loan.getRepaymentMonths());
					String rate = Double.toString(loan.getInterestRate()) + "%";
					String minVal = "£" + Double.toString(loan.getMinimumValue());
					String maxVal = "£" + Double.toString(loan.getMaximumValue());
					
					GlobalSettings.CONSOLE.printf(format, id, lender, repaymentMonths, rate, minVal, maxVal);
				}
			}
		}catch (NoResultsFoundException e) {
			GlobalSettings.CONSOLE.printf("No mortgages could be found in the system");
		}
	}
}
