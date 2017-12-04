package com.fairsail.loanEngine;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fairsail.accounts.Applicant;
import com.fairsail.accounts.CreditScore;

/**
 * Can't really work out how to mock up a server and it costs a lot of money plus registration as a finance company to get actual credit scores so just do something.
 * 
 * @author Lucas
 *
 */
public class CreditScoreFetcher {
	
	/**
	 * This method sends a GET request for the current applicant to the CreditScoreAPI and returns a credit score value with a set of notes against the account for the
	 * given source (creditsource or creditsource2 in this example)
	 * 	
	 * @param Applicant applicant
	 * @param String source
	 * 
	 * @return CreditScore
	 * 
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public CreditScore getCreditScore(Applicant applicant, String source) throws IOException, ParserConfigurationException, SAXException {
		CreditScore creditScore = new CreditScore();
		// Create the url to get the credit score from
		String url = "http://localhost:8080/CreditScoreAPI/rest/creditsites/" + source + "/credit-scores/" + applicant.getName();
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		// Send a GET request
		con.setRequestMethod("GET");
		
		int responseCode = con.getResponseCode();
		
		// Check the response code is 200 - if not then something has gone wrong
		if(responseCode == 200) {
			// Read the response from the API
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			
			while((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			// Parse the response xml and read out the information we want
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document parse = dBuilder.parse(new ByteArrayInputStream(response.toString().getBytes()));
			
			// Get the credit score
			creditScore.setScore(Integer.parseInt(parse.getElementsByTagName("userScore").item(0).getTextContent()));
			creditScore.setSource(source);
			
			// Get the notes
			NodeList nList = parse.getElementsByTagName("note");
			
			for(int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);
				
				if(nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					
					applicant.addNote(eElement.getTextContent());
				}
				
			}
		}
		
		// Check the credit score has been set properly
		if(creditScore.getScore() != 0) {
			return creditScore;
		}
		
		return null;
	}
}
