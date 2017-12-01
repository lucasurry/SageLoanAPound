package com.fairsail.mail;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;

public class MailSenderTest {
	
	private SimpleSmtpServer server;
	private MailSender ms;
	
	@Before
	public void setUp() throws Exception{
		server = SimpleSmtpServer.start(SimpleSmtpServer.DEFAULT_SMTP_PORT);
		ms = new MailSender("localhost", server.getPort());
	}
	
	@After
	public void tearDown() throws Exception{
		server.stop();
	}
	
	@Test
	public void sendtest() throws MessagingException {
        ms.sendMessage("sender@here.com", "Test", "Test Body", "receiver@there.com");
        
		List<SmtpMessage> emails = server.getReceivedEmails();
		assertThat(emails, hasSize(1));
		SmtpMessage email = emails.get(0);
		assertThat(email.getHeaderValue("Subject"), is("Test"));
		assertThat(email.getBody(), is("Test Body"));
		assertThat(email.getHeaderNames(), hasItem("Date"));
		assertThat(email.getHeaderNames(), hasItem("From"));
		assertThat(email.getHeaderNames(), hasItem("To"));
		assertThat(email.getHeaderNames(), hasItem("Subject"));
		assertThat(email.getHeaderValues("To"), contains("receiver@there.com"));
		assertThat(email.getHeaderValue("To"), is("receiver@there.com"));   
	}
	
	@Test
	public void testSendMessageWithCR() throws MessagingException {
		String bodyWithCR = "\n\nKeep these carriage returns";
		ms.sendMessage("sender@here.com", "Test", bodyWithCR, "receiver@there.com");

		List<SmtpMessage> emails = server.getReceivedEmails();
		assertThat(emails, hasSize(1));
		SmtpMessage email = emails.get(0);
		assertEquals(bodyWithCR, email.getBody());
	}
	
	@Test
	public void testSendTwoMessagesSameConnection() throws MessagingException {
		MimeMessage[] mimeMessages = new MimeMessage[2];

		mimeMessages[0] = ms.sendMessage("sender@here.com", "receiver@there.com", "Doodle1", "Bug1");
		mimeMessages[1] = ms.sendMessage("sender@here.com", "receiver@there.com", "Doodle2", "Bug2");

		assertThat(server.getReceivedEmails(), hasSize(2));
	}
}