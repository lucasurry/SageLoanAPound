package com.fairsail.exceptions;

public class NoResultsFoundException extends Exception{
	
	/**
	 * Added a random serialVersionUID for completeness 
	 */
	private static final long serialVersionUID = -1880504311644727532L;

	public NoResultsFoundException() {}
	
	public NoResultsFoundException(String message) {
		super(message);
	}
}
