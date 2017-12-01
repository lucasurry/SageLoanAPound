package com.fairsail.service;

import java.sql.SQLException;

import com.fairsail.accounts.User;
import com.fairsail.dao.DatabaseQueryExecutor;
import com.fairsail.dao.UserDao;
import com.fairsail.exceptions.LoginUnsucessfulException;
import com.fairsail.exceptions.NoResultsFoundException;

public class LoginService {

	private UserDao userDao;
	
	public LoginService(DatabaseQueryExecutor dqe) {
		userDao = new UserDao(dqe);
	}
	
	/**
	 * Very basic! Will check the user name and password exist in the database
	 * 
	 * @param userName
	 * @param Password
	 * @return User, the type of which is decided by the values in the user table
	 * @throws SQLException 
	 * @throws NoResultsFoundException 
	 * @throws LoginUnsucessfulException 
	 */
	
	public User checkCredentials(String userName, String password) throws SQLException, NoResultsFoundException, LoginUnsucessfulException {
		// Really not how to do it!
		String correctPassword = userDao.getUserPassword(userName);
		
		if(password.equals(correctPassword)) {
			switch(userDao.getUserType(userName)) {
				case "A" 		: return userDao.selectAdministrator(userName);
				case "B" 		: return userDao.selectApplicant(userName);
				case "C" 		: return userDao.selectUnderwriter(userName);
			}
		}
		
		throw new LoginUnsucessfulException();
	}
	
}
