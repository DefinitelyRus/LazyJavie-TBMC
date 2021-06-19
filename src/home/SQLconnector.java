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
	public static void update(String query, boolean tp) throws LoginException, SQLException {
		/*
		 * Updates or adds a record to the database, depends on what argument is entered.
		 * update() requires one argument, query.
		 * 
		 * "query" is the SQL command to be executed.
		 */
		
		//Local scan password
		try {
			File file = new File("C:\\lazyjavie_token.txt");
			Scanner reader = new Scanner(file);
		    while (reader.hasNextLine()) {dbPass = reader.nextLine();}
		    reader.close();}
		catch (FileNotFoundException e) {P.print("404: C:\\lazyjavie_token.txt is missing.");}
		catch (Exception e) {e.printStackTrace();}
		
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
	public static String get(String query, String toReturn, boolean tp) throws LoginException, SQLException {
		/*
		 * Returns a record from the database.
		 * get() requires two arguments, query and toReturn.
		 * 
		 * "query" is a SQL query like SELECT-WHERE.
		 * "toReturn" is the column you want to get.
		 * 
		 * For example:
		 * SQLconnector.get("select * from database.table where username is not null", "username")
		 * This will return the last item in the table.
		 */
		
		//Local scan password
		try {
			File file = new File("C:\\lazyjavie_token.txt");
			Scanner reader = new Scanner(file);
		    while (reader.hasNextLine()) {dbPass = reader.nextLine();}
		    reader.close();}
		catch (FileNotFoundException e) {P.print("404: C:\\lazyjavie_token.txt is missing.");}
		catch (Exception e) {e.printStackTrace();}
		
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
	public static LinkedList<String> getList(String query, String toReturn, boolean tp) throws LoginException, SQLException {
		/* WORK-IN-PROGRESS
		 * Returns an array of records from the database.
		 * getArray() requires two arguments, query and toReturn.
		 * 
		 * "query" is a SQL query like SELECT-WHERE.
		 * "toReturn" is the column you want to get.
		 * 
		 * For example:
		 * SQLconnector.get("select * from database.table where username is not null", "username")
		 * This will return all the items in the table that fit this condition.
		 */
		
		//Local scan password
		try {
			File file = new File("C:\\lazyjavie_token.txt");
			Scanner reader = new Scanner(file);
		    while (reader.hasNextLine()) {dbPass = reader.nextLine();}
		    reader.close();}
		catch (FileNotFoundException e) {P.print("404: C:\\lazyjavie_token.txt is missing.");}
		catch (Exception e) {e.printStackTrace();}
		
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
	@Deprecated
	public static String createRecord(String username, String password) {	//[1]
		/*
		 * A legacy command intended for testing only.
		 * Only use this as template for SQL connections.
		 */
		
		//TO PASTE----------------------------------------------------------------------------
		try {
			File file = new File("C:\\lazyjavie_token.txt");
			Scanner reader = new Scanner(file);
		    while (reader.hasNextLine()) {dbPass = reader.nextLine();}
		    reader.close();}
		catch (FileNotFoundException e) {P.print("404: C:\\lazyjavie_token.txt is missing.");}
		catch (Exception e) {e.printStackTrace();}
		/* TO PASTE----------------------------------------------------------------------------
		 * Paste this try-catch block for every function using JDBC.
		 * Its purpose is to get the password from lazyjavie_token.txt.
		 * This is needed because each contributor has to test the program locally.
		 * Local MySQL servers are set with different passwords.
		 * So the program has to adjust accordingly. 
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
	public static void NoDBfixer() {
		/*
		 * In the event that the database is missing, this function is called instead of update().
		 * 
		 * NOTE: COMPILED .JAR WILL CRASH IF THIS METHOD IS CALLED.
		 * Reason: NoDB_autofix.sql cannot be found
		 */
		try {
			P.print("\nError ecountered: Starting automatic database setup.");
			P.print("|[SQLcD-1] Running fixer script...");
			for (String line : noDB_autofix.get("createNew")) {
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
		
		dbAddress = dbAddressOverride;
		
		//Local scan password
		try {
			File file = new File("C:\\lazyjavie_token.txt");
			Scanner reader = new Scanner(file);
		    while (reader.hasNextLine()) {dbPass = reader.nextLine();}
		    reader.close();}
		catch (FileNotFoundException e) {P.print("404: C:\\lazyjavie_token.txt is missing.");}
		catch (Exception e) {e.printStackTrace();}
		
		//Initialization
		String exeScript = query;
		
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
			update("update lazyjavie.cmdlog set errorid ='" +error+ "' where userquery ='" +message+ "' order by eventdate desc limit 1", false);
		}
		catch (LoginException e) {P.print("Error encountered: " + e.toString());}
		catch (SQLException e) {P.print("Error encountered: " + e.toString());}
	}
		
}