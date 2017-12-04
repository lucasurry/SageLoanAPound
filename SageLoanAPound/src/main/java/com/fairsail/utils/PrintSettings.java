package com.fairsail.utils;

import java.io.Console;
import java.io.IOException;
import java.util.List;

public class PrintSettings {
	
	// Set up the console once so we don't have to pass it about
	public static final Console CONSOLE = System.console();
	
	/**
	 * Clears the console of anything on the screen to make it clearer
	 * 
	 * This clear screen only works for windows, needs updating if we are going to run it in linux or OSX
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void clearScreen() throws IOException, InterruptedException {
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
	}
	
	/**
	 * Prints a title panel on the screen so it is consistent
	 * 
	 * @param title
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void printScreenTitle(List<String> title) throws IOException, InterruptedException {
		clearScreen();
		CONSOLE.printf("-------------------------------------------------------------------------\n\n");
		
		for(String titleLine : title) {
			CONSOLE.printf(" " + titleLine);
		}
		
		CONSOLE.printf("-------------------------------------------------------------------------\n\n");
	}
}
