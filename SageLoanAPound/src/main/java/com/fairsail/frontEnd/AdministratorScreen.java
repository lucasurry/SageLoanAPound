package com.fairsail.frontEnd;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fairsail.accounts.User;
import com.fairsail.dao.DatabaseQueryExecutor;
import com.fairsail.exceptions.NoResultsFoundException;
import com.fairsail.loan.Mortgage;
import com.fairsail.loan.PersonalLoan;
import com.fairsail.service.MortgageService;
import com.fairsail.service.PersonalLoanService;
import com.fairsail.utils.GlobalSettings;

public class AdministratorScreen {
	
	private MortgageService mService;
	private PersonalLoanService pService;
	private ViewProductsScreen vps;
	
	public AdministratorScreen(DatabaseQueryExecutor dqe) {
		mService = new MortgageService(dqe);
		pService = new PersonalLoanService(dqe);
		
		vps = new ViewProductsScreen(mService, pService);
	}

	public void adminScreen(User currentUser) throws IOException, InterruptedException, SQLException {
		boolean isLoggedIn = true;
		
		// Until the user logs out
		while(isLoggedIn) {
			// Print the screen title
			List<String> title = new ArrayList<String>();
			title.add("Logged in as an administrator\n\n");
			title.add("----------------------------------------\n\n");
			title.add("Please select from the following options by entering the correct number\n");
			title.add("  1) View existing products\n");
			title.add("  2) Create a new product\n");
			title.add("  3) Delete existing product\n");
			title.add("\nType logout to exit\n");
			GlobalSettings.printScreenTitle(title);
		
			String input = GlobalSettings.CONSOLE.readLine("Enter your selection : \n");
			
			// Use a switch statement to decide what to do based on user input
			switch(input.toLowerCase()) {
				case "1"		: vps.viewProducts();
								  break;
				case "2"		: createProductScreen();
								  break;
				case "3"		: deleteProductScreen(currentUser);
								  break;
				case "logout"	: isLoggedIn = false;
								  break;
				default 		: GlobalSettings.CONSOLE.printf(input + " is not a valid option\n");
								  GlobalSettings.CONSOLE.readLine("Press the return key to continue");
			}
		}
	}
	
	public void createProductScreen() throws IOException, InterruptedException, SQLException {
		boolean isNotBack = true;
		
		// While the user has not asked to go back
		while(isNotBack) {
			// Print the screen title
			List<String> title = new ArrayList<String>();
			title.add("Create a new product\n\n");
			title.add("----------------------------------------\n\n");
			title.add("Please select from the following options by entering the correct number\n");
			title.add("  1) Create mortgage\n");
			title.add("  2) Create loan\n");
			title.add("\nType back to return to previous screen\n");
			GlobalSettings.printScreenTitle(title);
			
			String input = GlobalSettings.CONSOLE.readLine("Enter your selection : \n");
			
			// Use a switch statement to decide what to do based on user input
			switch(input.toLowerCase()) {
				case "1"		: createMortgageScreen();
								  break;
				case "2"		: createPersonalLoanScreen();
								  break;
				case "back"		: isNotBack = false;
								  break;
				default 		: GlobalSettings.CONSOLE.printf(input + " is not a valid option\n");
								  GlobalSettings.CONSOLE.readLine("Press the return key to continue");
			}
		}
	}
	
	private void createMortgageScreen() throws IOException, InterruptedException, SQLException {
		// Print the screen title
		List<String> title = new ArrayList<String>();
		title.add("Create a new mortgage\n");
		GlobalSettings.printScreenTitle(title);
		
		// Check the user input and create a new mortgage from that input
		mService.createMortgage(new AddProductInputChecker().checkMortgageInput());
	}
	
	private void createPersonalLoanScreen() throws IOException, InterruptedException, SQLException {
		// Print the screen title
		List<String> title = new ArrayList<String>();
		title.add("Create a new personal loan\n");
		GlobalSettings.printScreenTitle(title);
		
		// Check the user input and create a new personal loan from that input
		pService.createPersonalLoan((new AddProductInputChecker().checkPersonalLoanInput()));
	}
	
	public void deleteProductScreen(User user) throws IOException, InterruptedException, SQLException {
		boolean isNotBack = true;
		
		// While the user has not asked to go back
		while(isNotBack) {
			// Print the screen title
			List<String> title = new ArrayList<String>();
			title.add("Delete an existing product\n\n");
			title.add("----------------------------------------\n\n");
			title.add("Please select from the following options by entering the correct number\n");
			title.add("  1) Delete mortgage\n");
			title.add("  2) Delete loan\n");
			title.add("\nType back to return to previous screen\n");
			GlobalSettings.printScreenTitle(title);
			
			String input = GlobalSettings.CONSOLE.readLine("Enter your selection : \n");
			
			// Use a switch statement to decide what to do based on user input
			switch(input.toLowerCase()) {
				case "1"		: deleteMortgageScreen(user);
								  break;
				case "2"		: deletePersonalLoanScreen(user);
								  break;
				case "back"		: isNotBack = false;
								  break;
				default 		: GlobalSettings.CONSOLE.printf(input + " is not a valid option\n");
								  GlobalSettings.CONSOLE.readLine("Press the return key to continue");
			}
		}
	}
	
	private void deleteMortgageScreen(User currentUser) throws IOException, InterruptedException, SQLException {
		boolean isValid = false;
		String input = null;	
		
		// Loop until we get a valid user input
		while(!isValid) {			
			try {
				// Get a list of all the mortgages so we can check the input
				Map<Integer, Mortgage> mortgages = mService.getMortgages();
				// Display all the mortgages in the system so the administrator can get the correct id
				vps.viewMortgages();
				
				GlobalSettings.CONSOLE.printf("To go back without deleting a record type back\n");
				// Read the users input
				input = GlobalSettings.CONSOLE.readLine("Please enter the id of the mortgage you wish to delete : \n");
	
				// If the user wants to go back without deleting a record then we can do this
				if(input.equalsIgnoreCase("back")) {
					return;
				}
				
				// Parse the int out of the user input string
				int delId = Integer.parseInt(input);
				
				// Check the id exists
				if(mortgages.containsKey(delId)) {
					// If the id exists then delete the record
					mService.deleteMortgage(delId);
					// Exit the loop
					isValid = true;
				}else {
					// If the ID does not exist ask the user to try again
					GlobalSettings.CONSOLE.printf(input + " was not found in the list of id's, please enter a valid id\n");
					GlobalSettings.CONSOLE.readLine("Press the return key to continue");
				}
			}catch(NumberFormatException e) {
				// This will be thrown if the input string did not contain an int, ask the user to try again
				GlobalSettings.CONSOLE.printf(input + " is not a valid id, please enter a valid id\n");
				GlobalSettings.CONSOLE.readLine("Press the return key to continue");
			} catch (NoResultsFoundException e) {
				// If no mortgages are found display a message then go back to the previous screen
				GlobalSettings.CONSOLE.printf("No mortgages were found to be deleted\n");
				GlobalSettings.CONSOLE.readLine("Press the return key to continue");
				return;
			}
		}
	}
	
	private void deletePersonalLoanScreen(User currentUser) throws IOException, InterruptedException, SQLException {
		boolean isValid = false;
		String input = null;	
		
		// Loop until we get a valid user input
		while(!isValid) {			
			try {
				// Get a list of all the mortgages so we can check the input
				Map<Integer, PersonalLoan> personalLoans = pService.getPersonalLoans();
				// Display all the mortgages in the system so the administrator can get the correct id
				vps.viewLoans();
				
				GlobalSettings.CONSOLE.printf("To go back without deleting a record type back\n");
				// Read the users input
				input = GlobalSettings.CONSOLE.readLine("Please enter the id of the personal loan you wish to delete : \n");
	
				// If the user wants to go back without deleting a record then we can do this
				if(input.equalsIgnoreCase("back")){
					return;
				}
				
				// Parse the int out of the user input string
				int delId = Integer.parseInt(input);
				
				// Check the id exists
				if(personalLoans.containsKey(delId)) {
					// If the id exists then delete the record
					pService.deletePersonalLoan(delId);
					// Exit the loop
					isValid = true;
				}else {
					// If the ID does not exist ask the user to try again
					GlobalSettings.CONSOLE.printf(input + " was not found in the list of id's, please enter a valid id\n");
					GlobalSettings.CONSOLE.readLine("Press the return key to continue");
				}
			}catch(NumberFormatException e) {
				// This will be thrown if the input string did not contain an int, ask the user to try again
				GlobalSettings.CONSOLE.printf(input + " is not a valid id, please enter a valid id\n");
				GlobalSettings.CONSOLE.readLine("Press the return key to continue");
			} catch (NoResultsFoundException e) {
				// If no personal loans are found display a message then go back to the previous screen
				GlobalSettings.CONSOLE.printf("No mortgages were found to be deleted\n");
				GlobalSettings.CONSOLE.readLine("Press the return key to continue");
				return;
			}
		}
	}
}
