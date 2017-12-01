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
	
	public static void main(String args[]) throws IOException, ParserConfigurationException, SAXException {
		Applicant applicant = new Applicant(2, "applicant", null);
		new CreditScoreFetcher().getCreditScore(applicant, "creditsite");
	}
	
	public CreditScore getCreditScore(Applicant applicant, String source) throws IOException, ParserConfigurationException, SAXException {
		CreditScore creditScore = new CreditScore();
		String url = "http://localhost:8080/CreditScoreAPI/rest/creditsites/" + source + "/credit-scores/" + applicant.getName();
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
		con.setRequestMethod("GET");
		
		int responseCode = con.getResponseCode();
		
		
		if(responseCode == 200) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			
			while((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document parse = dBuilder.parse(new ByteArrayInputStream(response.toString().getBytes()));
			
			creditScore.setScore(Integer.parseInt(parse.getElementsByTagName("userScore").item(0).getTextContent()));
			creditScore.setSource(source);
			
			NodeList nList = parse.getElementsByTagName("note");
			
			for(int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);
				
				if(nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					
					applicant.addNote(eElement.getTextContent());
				}
				
			}
		}
		
		if(creditScore.getScore() != 0) {
			return creditScore;
		}
		
		return null;
	}
}
