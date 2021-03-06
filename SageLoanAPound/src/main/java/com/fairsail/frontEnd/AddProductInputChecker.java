package com.fairsail.frontEnd;

import com.fairsail.loan.Loan;
import com.fairsail.loan.Mortgage;
import com.fairsail.loan.PersonalLoan;
import com.fairsail.loan.SecuredLoan;
import com.fairsail.loan.UnsecuredLoan;
import com.fairsail.utils.PrintSettings;

public class AddProductInputChecker {

	/**
	 * A method to run checks on user input of personal loan details
	 * 
	 * @return PersonalLoan
	 */
	public PersonalLoan checkPersonalLoanInput() {
		PersonalLoan personalLoan = new PersonalLoan();
		// Create an unsecured loan
		addUnsecuredLoan(personalLoan);
		
		// There are no more values to set at the moment on a personal loan so return using what we already have
		return personalLoan;
	}
	
	/*
	 * A method to run checks on user input of unsecured loan details
	 */
	private void addUnsecuredLoan(UnsecuredLoan unsecuredLoan) {
		// Set the base object of loan values
		addLoan(unsecuredLoan);
		// Do nothing else for now!
	}
	
	/**
	 * A method to run checks on user input of mortgages details
	 * 
	 * @return Mortgage
	 */
	public Mortgage checkMortgageInput() {
		Mortgage mortgage = new Mortgage();
		
		boolean isValid;

		// Create an secured loan
		addSecuredLoan(mortgage);
		
		// Now add Mortgage specific attributes	
		// Check that the fee is a double
		isValid = false;
		while(!isValid) {
			try {
				double fees = Double.parseDouble(PrintSettings.CONSOLE.readLine("Enter the fee for this product (pounds and pence) : \n"));
				mortgage.setFees(fees);
				isValid = true;
			}catch(NumberFormatException e){
				PrintSettings.CONSOLE.printf("The fee value has to be just a number\n");
			}
		}
	
		// Find out if the product is for first time buyers only
		isValid = false;
		while(!isValid) {
			String input =  PrintSettings.CONSOLE.readLine("Is the product for first time buyers only? (Y/N) : \n");
			
			switch(input) {
				case "Y" : case "y" : mortgage.setFirstTimeBuyerOnly(true);
									  isValid = true;
									  break;
				case "N" : case "n" : mortgage.setFirstTimeBuyerOnly(false);
				  					  isValid = true;
				  					  break;
				default : PrintSettings.CONSOLE.printf("Valid inputs are Y or N\n");
			}
		}	
			
		return mortgage;
	}
	
	/*
	 * A method to run checks on user input of secured loan details
	 */
	private void addSecuredLoan(SecuredLoan securedLoan) {
		boolean isValid = false;
		
		// Create a new loan base object first
		addLoan(securedLoan);
		
		// Now add SecureLoan specific attributes		
		// Find out if the minimum deposit is a percentage or fixed value
				isValid = false;
				while(!isValid) {
					String input =  PrintSettings.CONSOLE.readLine("Is the deposit a percentage of the product? (Y/N) : \n");
					
					switch(input) {
						case "Y" : case "y" : securedLoan.setMinDepositIsPercent(true);
											  isValid = true;
											  break;
						case "N" : case "n" : securedLoan.setMinDepositIsPercent(false);
						  					  isValid = true;
						  					  break;
						default : PrintSettings.CONSOLE.printf("Valid inputs are Y if the deposit is a percentage or N if it is a fixed value\n");
					}
				}
		
		// Check that the minimum deposit is a double
		isValid = false;
		while(!isValid) {
			try {
				double minDeposit = Double.parseDouble(PrintSettings.CONSOLE.readLine("Enter the minimum deposit, either in percent or a fixed value (just numbers) : \n"));
				securedLoan.setMinimumDeposit(minDeposit);
				isValid = true;
			}catch(NumberFormatException e){
				PrintSettings.CONSOLE.printf("The deposit value has to be just a number\n");
			}
		}
	}
	
	/*
	 * A method to run checks on user input of loan details
	 */
	private void addLoan(Loan loan) {
		boolean isValid = false;
				
		// Check that the lender name is not too long
		isValid = false;
		while(!isValid) {
			String lender = PrintSettings.CONSOLE.readLine("Enter the lender : \n");
			
			if(lender.length() > 20) {
				PrintSettings.CONSOLE.printf("Lender name is too long, max 20 characters\n");
			}else {
				loan.setLender(lender);
				isValid = true;
			}
		}
		
		// Check the repayment months can be cast to an int
		isValid = false;
		while(!isValid) {
			try {
				int repaymentMonths = Integer.parseInt(PrintSettings.CONSOLE.readLine("Enter the repayment period in months : \n"));
				
				// Check that repayment months have been set and not set to 0 (can do both in one as default is 0)
				switch(repaymentMonths){
					case 0		: PrintSettings.CONSOLE.printf("Repayment must be more than 0\n");
								  break;
					default		: loan.setRepaymentMonths(repaymentMonths);
								  isValid = true;
				}
			}catch(NumberFormatException e){
				PrintSettings.CONSOLE.printf("The period has to be a whole number of months\n");
			}
		}
		
		// Check the repayment months can be cast to an int
		isValid = false;
		while(!isValid) {
			try {
				int minCreditScore = Integer.parseInt(PrintSettings.CONSOLE.readLine("Enter the minimum credit score for this product : \n"));
				loan.setMinimumCreditScore(minCreditScore);
				isValid = true;
			}catch(NumberFormatException e){
				PrintSettings.CONSOLE.printf("The minimum credit score has to be a whole number\n");
			}
		}
		
		// Check that the interest rate is a double
		isValid = false;
		while(!isValid) {
			try {
				double rate = Double.parseDouble(PrintSettings.CONSOLE.readLine("Please enter the interest rate : \n"));
				loan.setInterestRate(rate);
				isValid = true;
			}catch(NumberFormatException e){
				PrintSettings.CONSOLE.printf("The fee interest rate has to be just a number\n");
			}
		}
		
		// Check that the minimum value is a double
		isValid = false;
		while(!isValid) {
			try {
				double minValue = Double.parseDouble(PrintSettings.CONSOLE.readLine("Enter the minimum loan value for this product (pounds and pence) : \n"));
				if(minValue <= 0) {
					PrintSettings.CONSOLE.printf("The minimum loan value has to be at least 1\n");
				}else {
					loan.setMinimumValue(minValue);
					isValid = true;
				}
			}catch(NumberFormatException e){
				PrintSettings.CONSOLE.printf("The minimum loan value has to be just a number\n");
			}
		}
		
		// Check that the maximum value is a double
		isValid = false;
		while(!isValid) {
			try {
				double maxValue = Double.parseDouble(PrintSettings.CONSOLE.readLine("Enter the maximum loan value for this product (pounds and pence) : \n"));
				
				if(loan.getMaximumValue() > maxValue) {
					PrintSettings.CONSOLE.printf("The maximum loan value has to be greater than or equal to the minimum loan value\n");
				}else {
					loan.setMaximumValue(maxValue);
					isValid = true;
				}
			}catch(NumberFormatException e){
				PrintSettings.CONSOLE.printf("The maximum loan value has to be just a number\n");
			}
		}
		
		// Get the rule to use to work out which credit score to use
		// Loop until we have a vaild input
		isValid = false;
		while(!isValid) {

			PrintSettings.CONSOLE.printf("\nPlease enter the rule from below which you wish to use to get the credit score for this product\n");
			PrintSettings.CONSOLE.printf("  lowest		- lowest credit score\n");
			PrintSettings.CONSOLE.printf("  highest	- highest credit score\n");
			PrintSettings.CONSOLE.printf("  average	- average of all the credit scores\n");
			// this is creditsource and creditsource2 for ease but would be experian or similar
			PrintSettings.CONSOLE.printf("  source	  	- name of the sourtce of the credit score (creditsite or creditsite2)\n\n");
			
			String input = PrintSettings.CONSOLE.readLine("Enter the maximum loan value for this product (pounds and pence) : \n");
			
			// Check the user input is valid
			if(input.equalsIgnoreCase("lowest") || 
					input.equalsIgnoreCase("highest") || 
					input.equalsIgnoreCase("average") || 
					input.equalsIgnoreCase("creditsite") || 
					input.equalsIgnoreCase("creditsite2")) 
			{
				loan.setCreditScoreRule(input);
				isValid = true;
			}else {
				PrintSettings.CONSOLE.printf(input + " is not in the list of valid options\n");
			}
		}
	}
}
