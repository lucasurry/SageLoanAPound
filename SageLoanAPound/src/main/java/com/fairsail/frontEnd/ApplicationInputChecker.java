package com.fairsail.frontEnd;

import com.fairsail.applications.Application;
import com.fairsail.applications.MortgageApplication;
import com.fairsail.applications.PersonalLoanApplication;
import com.fairsail.utils.GlobalSettings;

public class ApplicationInputChecker {
	
	public MortgageApplication mortgageApplicationInputChecker(MortgageApplication application) {
		boolean isValid = false;
		
		loanApplicationInputChecker(application);
		
		isValid = false;
		while(!isValid) {
			// Check that the deposit value is a double
			try {
				double deposit = Double.parseDouble(GlobalSettings.CONSOLE.readLine("Enter amount you wish to deposit (pounds and pence) : \n"));
				application.setDepositAmount(deposit);
				isValid = true;
			}catch(NumberFormatException e){
				GlobalSettings.CONSOLE.printf("The depoist value has to be a number\n");
			}
		}
		
		isValid = false;
		while(!isValid) {
			// Set the first time buyer boolean
			String input = GlobalSettings.CONSOLE.readLine("Are you a first time buyer? (Y/N)\n");
			
			switch(input) {
				case "Y" : case "y" 	: application.setFirstTimeBuyer(true);
										  isValid = true;
				case "N" : case "n" 	: application.setFirstTimeBuyer(false);
										  isValid = true;
				default					: GlobalSettings.CONSOLE.printf(input + " is not a valid option! Please use Y or N");
			}
		}
		
		
		return application;
	}
	
	public PersonalLoanApplication personalLoanApplicationInputChecker(PersonalLoanApplication application) {
		loanApplicationInputChecker(application);
		
		return application;
	}
	
	private void loanApplicationInputChecker(Application application) {
		boolean isValid = false;
	
		isValid = false;
		while(!isValid) {
			// Check that the loan value is a double
			try {
				double loanValue = Double.parseDouble(GlobalSettings.CONSOLE.readLine("Enter amount you wish to loan (pounds and pence) : \n"));
				application.setLoanValue(loanValue);
				isValid = true;
			}catch(NumberFormatException e){
				GlobalSettings.CONSOLE.printf("The value has to be a number\n");
			}
		}
	}
}
