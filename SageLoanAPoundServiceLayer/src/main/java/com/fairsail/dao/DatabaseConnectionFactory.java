package com.fairsail.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DatabaseConnectionFactory {

	public DatabaseConnectionFactory() {}
	
	public Connection createDatabaseConnection(String user, String password, String server, String database) throws SQLException {
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setUser(user);
		dataSource.setPassword(password);
		dataSource.setServerName(server);
		dataSource.setDatabaseName(database);
	
		Connection connection = dataSource.getConnection(); 
		
		/*
		// Check the connection is OK
		// Check that we are in the correct schema or we won't find the tables!
		if(connection.getSchema() == null || !connection.getSchema().equals("loanapound")) {
			connection.setSchema("loanapound");
		}*/
		
		return connection;
	}
}
