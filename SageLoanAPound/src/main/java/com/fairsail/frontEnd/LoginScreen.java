package com.fairsail.frontEnd;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fairsail.accounts.User;
import com.fairsail.dao.DatabaseQueryExecutor;
import com.fairsail.exceptions.LoginUnsucessfulException;
import com.fairsail.exceptions.NoResultsFoundException;
import com.fairsail.service.LoginService;
import com.fairsail.utils.PrintSettings;

public class LoginScreen {
	
	private LoginService login;
	
	/**
	 * Create a login screen as the first screen which a user sees when they log into the database
	 * 
	 * @param DatabaseQueryExecutor
	 */
	public LoginScreen(DatabaseQueryExecutor dqe) {
		login = new LoginService(dqe);
	}
	
	/**
	 * Create a screen to get users to log in to the system, taking their username and password
	 * 
	 * @return User
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	public User loginScreen() throws IOException, InterruptedException, SQLException {
		// Print the screen title
		List<String> title = new ArrayList<String>();
		title.add("Welcome to Loan A Pound\n\n");
		title.add("Please enter your login credentials to continue\n\n");
		PrintSettings.printScreenTitle(title);
		
		User currentUser = null;
		
		boolean isLoggedIn = false;
		
		while(!isLoggedIn) {
			// Get the username
			String username = PrintSettings.CONSOLE.readLine("Enter your name : \n");
			
			// Get the password
			String password = new String(PrintSettings.CONSOLE.readPassword("Enter your password : \n"));
			
			try {
				currentUser = login.checkCredentials(username, password);
				isLoggedIn = true;
			} catch (NoResultsFoundException | LoginUnsucessfulException e) {
				PrintSettings.CONSOLE.printf("User name and password not found, please log in using an existing account\n\n");
			}
		}
		
		return currentUser;
	}	
}
