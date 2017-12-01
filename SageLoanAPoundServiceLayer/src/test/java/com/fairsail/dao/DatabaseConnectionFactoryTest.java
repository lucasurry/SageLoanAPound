package com.fairsail.dao;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Test;

public class DatabaseConnectionFactoryTest {
	
	private Connection connection;
	
	@After
	public void tearDown() throws SQLException {
		// Make sure we don't leave a connection!
		if(!connection.isClosed()) {
			connection.close();
		}
	}
	
	@Test
	// Test that we can connect to the database with the test credentials - would fail if the database is not there already
	public void validateConnection() throws SQLException {
		DatabaseConnectionFactory dcf = new DatabaseConnectionFactory();
		connection = dcf.createDatabaseConnection("application", "apppass", "localhost", "loanapound");

		assertThat(connection.isClosed(), is(false));

	}

}
