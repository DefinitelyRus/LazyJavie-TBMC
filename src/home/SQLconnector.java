/*
 * ---------------!!! ADD TO README !!!---------------
 * This module serves as the connector between the program and the local SQL server.
 * Given the complexity of this program, we decided to put all MySQL-related
 * code in this module so all we have to do is call a function if we ever
 * need to get records from the database or update a table.
 * 
 * [1]	update() function
 * 		This function takes an SQL command and updates the selected table.
 * 		Both adding and updating records can be done with this function by putting
 * 		INSERT INTO instead of UPDATE.
 * 
 * [2]	get() function
 * 		This function takes an SQL query and a column to return then returns the result.
 * 		In case there are multiple results, this function will return the last one.
 * 
 * [3]	getList() function
 * 		This function takes an SQL query and a column to return then return a list of results.
 * 		Only use this if you intend to get a LINKED LIST not just a STRING.
 * 
 * [4]	NoDBfixer() function
 * 		Its function is to automatically fix a missing database.
 * 		It takes no input. Used for checking upon startup.
 */

package home;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Scanner;
import javax.security.auth.login.LoginException;

import org.apache.commons.lang3.exception.ExceptionUtils;

import commands.P;
import sql_queries.noDB_autofix;

public class SQLconnector {
	//Initializing variables
	static int records = 0;
	static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static String dbAddress = "jdbc:sqlite:lazyjavie.db";
	static String defaultAddress = "jdbc:sqlite:lazyjavie.db";
	static String dbID = "root";
	static String dbPass = "password";
	
	//-------------------------UPDATE-------------------------
	/**Can be used to add, delete, or modify tables and its contents.
	 * 
	 * <p>Usage:
	 * update("UPDATE table_name SET 0 WHERE column = 10", true) <p>
	 * <p>This will set all values in a column to 0 where the value is 10.<p>
	 * 
	 * @param query An SQL query like UPDATE-WHERE-SET. Note that you can only use this for one statement at a time.
	 * @param tp Boolean whether running this function should print what it's currently doing.
	 * @throws LoginException
	 * @throws SQLException
	 */
	public static void update(String query, boolean tp) throws LoginException, SQLException {
		
		dbPass = getPass();
		
		//Initialization
		String exeScript = query;
		
		try {
			//Starts a connection to the database using the JDBC driver.
			if (tp == true) {P.print("|[SQLcA-1] Starting connection with the database...");}
			Connection connection = DriverManager.getConnection(dbAddress, dbID, dbPass);
			
			//Creates a statement
			if (tp == true) {P.print("|[SQLcA-2] Creating statement...");}
			Statement statement = connection.createStatement();
			
			//Starts the SQL query.
			if (tp == true) {P.print("|[SQLcA-3] Executing SQL script...");}
			statement.execute(exeScript);
			
			if (tp == true) {P.print("|[SQLcA-4] Done!");}
			
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//-------------------------GET-------------------------
	/**Returns a record from the database.
	 * 
	 * <p>Usage:
	 * SQLconnector.get("select * from database.table where username is not null", "username")
	 * This will return the last item in the table.<p>
	 * 
	 * @param query An SQL query like SELECT-WHERE. Note that you can only use this for one statement at a time.
	 * @param toReturn A column or value you want returned.
	 * @param tp Boolean whether running this function should print what it's currently doing.
	 * @return The last or only string value that qualify the SQL query.
	 * @throws LoginException
	 * @throws SQLException
	 * @see getList(String query, String toReturn, boolean tp) - for getting a list of values.
	 */
	public static String get(String query, String toReturn, boolean tp) throws LoginException, SQLException {
		
		dbPass = getPass();
		
		//Initialization
		String returnMsg = "Attempting to update table.";
		String exeScript = query;
		
		try {
			//Starts a connection to the database using the JDBC driver.
			if (tp == true) {P.print("|[SQLcB-1] Starting connection with the database...");}
			Connection connection = DriverManager.getConnection(dbAddress, dbID, dbPass);
			returnMsg = "Connection started.";
			
			//Creates a statement
			if (tp == true) {P.print("|[SQLcB-2] Creating statement...");}
			Statement statement = connection.createStatement();
			returnMsg = "Statement created.";
			
			//Starts the SQL query.
			if (tp == true) {P.print("|[SQLcB-3] Executing SQL script...");}
			ResultSet results = statement.executeQuery(exeScript);
			returnMsg = "Executed.";
			
			//Returns the requested record.
			if (tp == true) {P.print("|[SQLcB-4] Outputting results...");}
			returnMsg = "Empty result.";
			while (results.next()) {returnMsg = results.getString(toReturn);}
			
			//Closes the connection then returns the result.
			connection.close();
			return returnMsg;
		} catch (Exception e) {
			e.printStackTrace();
			return "Error encountered: " + e;
		}
	}
	
	//-------------------------GET LIST-------------------------
	/**Returns a list of records from the database.
	 * 
	 * <p>Usage:
	 * SQLconnector.get("select * from database.table where username is not null", "username")
	 * This will return all the items in the table that fit this condition into a linked list.<p>
	 * 
	 * @param query An SQL query like SELECT-WHERE. Note that you can only use this for one statement at a time.
	 * @param toReturn A column or value you want returned.
	 * @param tp Boolean whether running this function should print what it's currently doing.
	 * @return A list (LinkedList) of results according to the SQL query and toReturn (column) parameters.
	 * @throws LoginException
	 * @throws SQLException
	 * @see get(String query, String toReturn, boolean tp) - for getting single values.
	 */
	public static LinkedList<String> getList(String query, String toReturn, boolean tp) throws LoginException, SQLException {
		/* WORK-IN-PROGRESS
		
		 */
		
		dbPass = getPass();
		
		//Initialization
		//Creating a linked list.
		LinkedList<String> returnList = new LinkedList<String>(); returnList.add("");
		String exeScript = query;
		
		try {
			//Starts a connection to the database using the JDBC driver.
			if (tp == true) {P.print("|[SQLcB-1] Starting connection with the database...");}
			Connection connection = DriverManager.getConnection(dbAddress, dbID, dbPass);
			
			//Creates a statement
			if (tp == true) {P.print("|[SQLcB-2] Creating statement...");}
			Statement statement = connection.createStatement();
			
			//Starts the SQL query.
			if (tp == true) {P.print("|[SQLcB-3] Executing SQL script...");}
			ResultSet results = statement.executeQuery(exeScript);
			
			//Returns the requested record.
			if (tp == true) {P.print("|[SQLcB-4] Outputting results...");}
			while (results.next()) {returnList.add(results.getString(toReturn));}
			
			//Closes the connection then returns the result.
			connection.close();
			return returnList;
		} catch (Exception e) {
			e.printStackTrace();
			return returnList;
		}
	}
	
	//-------------------------CREATE RECORD-------------------------
	/**
	 * @deprecated Only kept for code reference.
	 * @param username
	 * @param password
	 * @return A status message. It isn't for anything useful.
	 * @see update(String query, boolean tp)
	 */
	@Deprecated
	public static String createRecord(String username, String password) {	//[1]
		/*
		 * A legacy command intended for testing only.
		 * Only use this as template for SQL connections.
		 */
		
		
		//Initialization
		String returnMsg = "Attempting to create new record.";
		String exeScript = "insert into lazyjavie.member_registry(userid, userpass) values (\"" + username + "\", \"" + password + "\")";
		
		try {
			//Starts a connection to the database using the JDBC driver.
			P.print("[A-1] Starting connection with the database...");
			Connection connection = DriverManager.getConnection(dbAddress, dbID, dbPass);
			returnMsg = "Connection started.";
			
			//Creates a statement
			P.print("[A-2] Creating statement...");
			Statement statement = connection.createStatement();
			returnMsg = "Statement created.";
			
			//Starts the SQL query.
			P.print("[A-3] Executing SQL script...");
			statement.execute(exeScript);
			returnMsg = "Script executed.";
			
			P.print("[A-4] Done!");
			returnMsg = "All finished.";
			
			connection.close();
			return returnMsg;
		} catch (Exception e) {
			e.printStackTrace();
			return "Error encountered: " + e;
		}
	}
	
	//-------------------------AUTO CREATE DB-------------------------
	/**
	 * In the event that the database is missing, this function is called.
	 * 
	 * <p>NOTE: COMPILED .JAR WILL CRASH IF THIS METHOD IS CALLED.
	 * <br>Reason: NoDB_autofix.sql cannot be found</p>
	 * 
	 * 
	 * <p>ANOTHER NOTE: The note above only applies if a scanner is used to search for a file.
	 * As a substitute, the entire SQL sequence has been stored as a string in Java.
	 * This is subject to change.</p>
	 */
	public static void NoDBfixer() {
		try {
			P.print("\nStarting automatic database setup...");
			P.print("|[SQLcD-1] Running fixer script...");
			for (String line : noDB_autofix.get("sqlite")) {
				P.print("|MySQL Statement: " + line);
				updateOtherDB(line, defaultAddress);
			}
			P.print("|[SQLcD-4] New database created.\n");
		}
		catch (LoginException e2) {P.print("Error encountered: " + e2.toString()); return;}
		catch (SQLException e2) {P.print("Error encountered: " + e2.toString()); return;}
		catch (Exception e2) {P.print("Error encountered: " + e2.toString()); return;}
	}
	
	public static void updateOtherDB(String query, String dbAddressOverride) throws LoginException, SQLException {
		/*
		 * In the event that the target database is different, this function is called instead of update().
		 * updateOtherDB() requires one argument, query.
		 * 
		 * "query" is the SQL command to be executed.
		 */
		
		
		
		//Initialization
		String exeScript = query;
		dbPass = getPass();
		dbAddress = dbAddressOverride;
		
		try {
			//Starts a connection to the database using the JDBC driver.
			Connection connection = DriverManager.getConnection(dbAddress, dbID, dbPass);
			
			//Creates a statement
			Statement statement = connection.createStatement();
			
			//Starts the SQL query.
			P.print("|[SQLcD-2] Executing SQL script...");
			statement.execute(exeScript);
			
			//Closes the connection.
			connection.close();
		}
		catch (SQLException e) {
			if (e.toString().startsWith("java.sql.SQLException: Can not issue empty query.")) {P.print("");}
			else e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void newQuery(String userID, String message) {
		//P.print("Adding new query to log.\nUserID: " +userID+ "\nQuery: " +message);
		try {
			update("insert into lazyjavie.cmdlog (userid, userquery, eventdate) values ('" +userID+ "', '" +message+ "', current_time())", false);
		}
		catch (LoginException e) {P.print("Error encountered: " + e.toString());}
		catch (SQLException e) {P.print("Error encountered: " + e.toString());}
	}
	
	public static void callError(String message, String error) {
		//P.print("Calling error: " + error);
		try {
			update("update cmdlog set errorid ='" +error+ "' where userquery ='" +message+ "' order by eventdate desc limit 1", false);
		}
		catch (LoginException e) {P.print("Error encountered: " + e.toString());}
		catch (SQLException e) {P.print("Error encountered: " + e.toString());}
	}
	
	/**
	 * Keeps log of all uncaught errors, assuming they're within a try-catch block.
	 * @param errorType This is the shortened error message which includes the error type.
	 * @param errorStackTrace The message that appears when an exception isn't caught. This includes the origin of the error.
	 * @param version The version the app is running when the error happened.
	 */
	public static void errorLog(String errorType, String errorStackTrace, String version) {
		try {
			update("insert into errorlog (err_type, err_stacktrace, eventdate, appver) values ('" +errorType+ "', '" +errorStackTrace+ "', current_timestamp, '" +version+ "');", false);
		}
		catch (LoginException e) {P.print("Error encountered: " + e.toString());}
		catch (SQLException e) {P.print("Error encountered: " + e.toString());}
	}
	
	public static int[] getXY(String tableName) {
		int x = 0, y = 0;
		final String getX = "select count(*) from " +tableName+ ";";
		final String getY = "select * from " +tableName+ " where 1=2;";
		dbPass = getPass();
		
		try {
			//Starts a connection to the database using the JDBC driver.
			Connection connection = DriverManager.getConnection(dbAddress, dbID, dbPass);
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(getX);
			
			//Gets the number of rows.
			while (results.next()) {x = Integer.valueOf(results.getString("count(*)"));}
			
			//Gets the number of columns.
			results = statement.executeQuery(getY);
			y = results.getMetaData().getColumnCount();
			
			connection.close();
		} catch (SQLException e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
		
		int[] XY = {x, y};
		return XY;
	}
	
	/**Primarily used to get data outside of individual tables or about the table themselves.
	 * 
	 * @param query An SQL query like SELECT-WHERE. Note that you can only use this for one statement at a time.
	 * @return A ResultSet object containing data about the query.
	 */
	public static ResultSet getResultSet(String query) {
		try {
			Connection con = DriverManager.getConnection(dbAddress, dbID, dbPass);
			ResultSet resultSet = con.createStatement().executeQuery(query);
			//con.close();
			return resultSet;
		}
		catch (SQLException e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString()); return null;}
		catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString()); return null;}
	}
	
	static String getPass() {
		try {
			String dbpass = "";
			File file = new File("C:\\lazyjavie_token.txt");
			Scanner reader = new Scanner(file);
		    while (reader.hasNextLine()) {dbpass = reader.nextLine();}
		    reader.close();
		    return dbpass;
		    }
		catch (FileNotFoundException e) {
			SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print("404: C:\\lazyjavie_token.txt is missing.");
			return "";
			//TODO Autofix
			}
		catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString()); return "";}
	}
}