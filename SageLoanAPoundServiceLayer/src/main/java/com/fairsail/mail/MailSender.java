package com.fairsail.mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;

import java.util.Date;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {

	private String host;
	private int port;
	private Properties props;
	private Session session;

	// If using an online mail system we may need to log in so get credentials
	private String userName = null;
	private String password = null;

	/**
	 * Use this if you do not need authentication on the smtp server.
	 * 
	 * Takes the host name and port for the server
	 * 
	 * @param host
	 * @param port
	 */
	public MailSender(String host, int port) {
		this.host = host;
		this.port = port;
		connectToMailServer();
		
	}
	
	/**
	 * Use this if you need authentication to log into the smtp server
	 * 
	 * Takes the host name and port as well as the user name and passowrd used to log in to the smtp server
	 * 
	 * @param host
	 * @param port
	 * @param userName
	 * @param password
	 */
	public MailSender(String host, int port, String userName, String password) {
		this.host = host;
		this.port = port;
		this.userName = userName;
		this.password = password;
		connectToMailServer();
	}
	
	/*
	 *  Create the smtp mail server properties then create the session on the server with authentication as needed 
	 */
	private void connectToMailServer() {
		// Create the mail properties
		props = new Properties();
		
		// Always need these ones
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "" + port);
		props.put("mail.smtp.sendpartial", "true");
		props.put("mail.smtp.socketFactory.port", port);
		
		// Only need to set these if you want to connect to a server which needs authentication
		if(userName != null && password != null) {
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
		}
		
		// Create our session
		if(userName != null && password != null) {
			// Create a new Authenticator as a part of the session if the passowrd and user name are set
			session = Session.getDefaultInstance(props, 
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(userName, password);
						}
					});
		}else {
			session = Session.getDefaultInstance(props);
		}
	}
	
	/**
	 * This method creates and sends an email using the smtp server set up by the current instance of the mailing system
	 * 
	 * @param sender (email address of the sender)
	 * @param subject (subject of the email)
	 * @param body (body text of the email, plain text only for now!)
	 * @param reciever (email address of the recipient)
	 * 
	 * @return returns the message as a MimeMessage
	 * 
	 * @throws MessagingException
	 */
	public MimeMessage sendMessage(String sender, String subject, String body, String reciever) throws MessagingException {	
		MimeMessage message = new MimeMessage(session);
		
		try {
			// Populate the message details
			message.setFrom(new InternetAddress(sender));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(reciever));
			message.setSubject(subject);
			message.setText(body);
			message.setSentDate(new Date());
		} catch (MessagingException e) {
			System.out.println("Failed to create the message to be sent");
			System.out.println("Error Message : " + e.getMessage());
			throw e;
		}
		
		try {
			// Send the message
			Transport.send(message);			
		}catch(MessagingException e) {
			System.out.println("Failed to send message");
			System.out.println("Error Message : " + e.getMessage());
			throw e;
		}
		
		// Return the message. Just using the for testing at the moment but it could be useful to see email's which have been sent somewhere locally.
		return message;
	}
}
