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
import com.fairsail.utils.GlobalSettings;

public class LoginScreen {
	
	private LoginService login;
	
	public LoginScreen(DatabaseQueryExecutor dqe) {
		login = new LoginService(dqe);
	}
	
	public User loginScreen() throws IOException, InterruptedException, SQLException {
		// Print the screen title
		List<String> title = new ArrayList<String>();
		title.add("Welcome to Loan A Pound\n\n");
		title.add("Please enter your login credentials to continue\n\n");
		GlobalSettings.printScreenTitle(title);
		
		User currentUser = null;
		
		boolean isLoggedIn = false;
		
		while(!isLoggedIn) {
			// Get the username
			String username = GlobalSettings.CONSOLE.readLine("Enter your name : \n");
			
			// Get the password
			String password = new String(GlobalSettings.CONSOLE.readPassword("Enter your password : \n"));
			
			try {
				currentUser = login.checkCredentials(username, password);
				isLoggedIn = true;
			} catch (NoResultsFoundException | LoginUnsucessfulException e) {
				GlobalSettings.CONSOLE.printf("User name and password not found, please log in using an existing account\n\n");
			}
		}
		
		return currentUser;
	}	
}
