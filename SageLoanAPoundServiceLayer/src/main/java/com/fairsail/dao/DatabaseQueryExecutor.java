package com.fairsail.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseQueryExecutor {

	DatabaseConnectionFactory dcf = new DatabaseConnectionFactory(); 
	Connection connection;
	
	Statement stmt;
	
	public DatabaseQueryExecutor(String user, String password, String server, String database) throws SQLException {
		connection = dcf.createDatabaseConnection(user, password, server, database);
	}
	
	public ResultSet executeSelect(String sql) throws SQLException {
		stmt = connection.createStatement();
		return stmt.executeQuery(sql);
	}
	
	public void executeStatement(String sql) throws SQLException {
		// Run the insert
		try {
			stmt = connection.createStatement();
			stmt.execute(sql);
		}finally {
			closeStatement();
		}
	}
	
	public void closeStatement() throws SQLException{
		if(!stmt.isClosed()) {
			stmt.close();
		}
	}
	
	public void closeConnection() throws SQLException {
		if(!connection.isClosed()) {
			connection.close();
		}
	}
}
