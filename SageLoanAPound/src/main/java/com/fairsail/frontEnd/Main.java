package com.fairsail.frontEnd;

import com.fairsail.accounts.Administrator;
import com.fairsail.accounts.Applicant;
import com.fairsail.accounts.Underwriter;
import com.fairsail.accounts.User;
import com.fairsail.dao.DatabaseQueryExecutor;
import com.fairsail.utils.PrintSettings;

public class Main {
	
	public static void main(String args[]) throws Exception {
		// Open one connection now and use it for everything in this session, defauult it to the one account but wouldn't really want to do it here
		DatabaseQueryExecutor dqe = new DatabaseQueryExecutor("application","apppass","localhost", "loanapound");
		try {
			// Show the user a login screen, returns a user
			LoginScreen login = new LoginScreen(dqe);
			User user  = login.loginScreen();
			
			// Depending on the type of the user display the correct screen for them to use
			if(user instanceof Administrator){
				new AdministratorScreen(dqe).adminScreen(user);
			}else if(user instanceof Applicant) {
				Applicant applicant = (Applicant) user;
				new ApplicantScreen(dqe).applicantScreen(applicant);
			}else if(user instanceof Underwriter) {
				new UnderwriterScreen(dqe).underwriterScreen();
			}else {
				PrintSettings.CONSOLE.printf("Unknown user type, login falied!");
			}
		}finally {
			// Make sure to close the database connection when we are done
			dqe.closeConnection();
		}
	}
	
}
