package com.haifisch.server.datamanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Manages the database. Creates the connection, executes queries, return results for selects, closes the connection.
 */

public class DatabaseManager {
	
	private static final String DEFAULT_PORT = "1306"; // or empty
	private static final String DEFAULT_HOST = "localhost";
	private static final String DEFAULT_DB_NAME = "haifisch";
	private static final String DEFAULT_USERNAME = ""; // to be "username", for now now no credentials
	private static final String DEFAULT_PASSWORD = ""; // to be "password"
	
	private String dbURL = "jdbc:mysql://";
	private String dbClass = "com.mysql.jdbc.Driver";
	private Connection connection;
	
	/**
	 * Modular constructor
	 * @param host the host
	 * @param port	the port
	 * @param dbName	the database name
	 * @param username	the username
	 * @param password	the password
	 */
	public DatabaseManager(String host, String port, String dbName, String username, String password){
		setdbURL(host, port, dbName, username, password);
	}
	
	/**
	 * Full path constructor
	 * @param dbURL
	 */
	public DatabaseManager(String dbURL){
		setdbURLFull(dbURL);
	}
	
	/**
	 * Establishes a connection to the database server
	 */
	public void connectToDatabase(){
		try {
			Class.forName(dbClass);
			connection = DriverManager.getConnection(dbURL);
		} catch (ClassNotFoundException | SQLException e){
			closeConnection();
			e.printStackTrace();
			if (e instanceof SQLException)
				System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Closes an established connection;
	 */
	public void closeConnection(){
		try {
			if (connection != null && !connection.isClosed()) // may change to some seconds
				connection.close();
		} catch (SQLException sqle) {
			closeConnection();
			System.err.println(sqle.getMessage());
		}
	}
	
	/**
	 * Executes a given query and returns the results.
	 * @param query the sql query to execute
	 * @return set of results
	 */
	public ResultSet executeQuery(String query){
		Statement statement = null;
		ResultSet results = null;
		try {
			if (!connection.isClosed()) { // may change to some seconds
				statement = connection.createStatement();
				results = statement.executeQuery(query);
			}
		} catch (SQLException sqle) {
			closeConnection();
			sqle.printStackTrace();
			System.err.println("\n"+sqle.getMessage());

		}
		/*
		Closing the statement closes the whole damn thing
		finally {

			try {
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println("\n"+e.getMessage());
			}
		}*/
		return results;
	}
	
	/**
	 * Initializes the database url
	 * @param value the given value
	 * @param defaultValue the default value if the given is incorrect
	 * @param delimiter the prefix of the value
	 */
	private void dbURLInitializer(String value, String defaultValue, String delimiter){
		if (delimiter != null) this.dbURL += delimiter.trim();
		if (value == null || value.trim().equals(""))
			this.dbURL += defaultValue;
		else
			this.dbURL += value.trim();
	}
	
	/**
	 * Sets the db url based on separate values of it
	 * @param host	the host
	 * @param port	the port
	 * @param dbName	the database name
	 * @param username	the username
	 * @param password	the password
	 */
	protected void setdbURL(String host, String port, String dbName, String username, String password){
		dbURLInitializer(host, DEFAULT_HOST, null);
		dbURLInitializer(port, DEFAULT_PORT, ":");
		dbURLInitializer(dbName, DEFAULT_DB_NAME, "/");
		dbURLInitializer(username, "user="+DEFAULT_USERNAME, "?");
		dbURLInitializer(password, "password="+DEFAULT_PASSWORD, "&");
	}
	
	/**
	 * Sets the full db url
	 * @param dbURL the whole database url
	 */
	protected void setdbURLFull(String dbURL){
		if (dbURL.startsWith("jdbc:mysql://"))
			this.dbURL = dbURL;
		else
			this.dbURL += dbURL;
	}
	
}
