package com.fairsail.frontEnd;

import com.fairsail.accounts.Administrator;
import com.fairsail.accounts.Applicant;
import com.fairsail.accounts.Underwriter;
import com.fairsail.accounts.User;
import com.fairsail.dao.DatabaseQueryExecutor;
import com.fairsail.utils.GlobalSettings;

public class Main {
	
	public static void main(String args[]) throws Exception {
		// Open one connection now and use it for everything in this session
		DatabaseQueryExecutor dqe = new DatabaseQueryExecutor("application","apppass","localhost", "loanapound");
		try {
			LoginScreen login = new LoginScreen(dqe);
			
			User user  = login.loginScreen();
			
			if(user instanceof Administrator){
				new AdministratorScreen(dqe).adminScreen(user);
			}else if(user instanceof Applicant) {
				Applicant applicant = (Applicant) user;
				new ApplicantScreen(dqe).applicantScreen(applicant);
			}else if(user instanceof Underwriter) {
				new UnderwriterScreen(dqe).underwriterScreen();
			}else {
				GlobalSettings.CONSOLE.printf("Unknown user type, login falied!");
			}
		}finally {
			dqe.closeConnection();
		}
	}
	
}
