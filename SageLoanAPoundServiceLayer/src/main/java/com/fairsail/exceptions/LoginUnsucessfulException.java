package com.fairsail.exceptions;

public class LoginUnsucessfulException extends Exception{

	/**
	 * Added a random serialVersionUID for completeness 
	 */
	private static final long serialVersionUID = 5097400776251038458L;

	public LoginUnsucessfulException() {}
	
	public LoginUnsucessfulException(String message) {
		super(message);
	}
}
