/*
 * This module serves as the connector between the program and the local SQL server.
 * 
 * [1]	Update function
 * 		This function takes an SQL command and updates the selected table.
 * 		Both adding and updating records can be done with this function by putting
 * 		INSERT INTO instead of UPDATE.
 * 
 * [2]	Get function
 * 		This function takes an SQL query and a column to return then returns the result.
 * 		In case there are multiple results, this function will return the last one.
 * 
 * [3]	GetList function
 * 		This function takes an SQL query and a column to return then returns the results.
 * 		Only use this if you intend to get an ARRAY not a SINGLE STRING.
 * 
 * [4]	Test command
 * 		This checks if the connection between the local SQL database is stable.
 * 		It takes password as input, but it could really be anything under 256 characters.
 */

package bot_init;

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

public class SQLconnector {
	//Initializing variables
	static int records = 0;
	static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static String dbAdress = "jdbc:mysql://localhost:3306/lazyjavie";
	static String dbID = "root";
	static String dbPass = "password";
	
	public static String update(String query) throws LoginException, SQLException {
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
		    P.print(dbPass);
		    reader.close();}
		catch (FileNotFoundException e) {P.print("404: C:\\lazyjavie_token.txt is missing.");}
		catch (Exception e) {e.printStackTrace();}
		
		//Initialization
		String returnMsg = "Attempting to update table.";
		String exeScript = query;
		
		try {
			//Starts a connection to the database using the JDBC driver.
			P.print("[SQLcA-1] Starting connection with the database...");
			Connection connection = DriverManager.getConnection(dbAdress, dbID, dbPass);
			returnMsg = "Connection started.";
			
			//Creates a statement
			P.print("[SQLcA-2] Creating statement...");
			Statement statement = connection.createStatement();
			returnMsg = "Statement created.";
			
			//Starts the SQL query.
			P.print("[SQLcA-3] Executing SQL script...");
			statement.execute(exeScript);
			returnMsg = "Script executed.";
			
			P.print("[SQLcA-4] Done!");
			returnMsg = "All finished.";
			
			connection.close();
			return returnMsg;
		} catch (Exception e) {
			e.printStackTrace();
			return "Error encountered: " + e;
		}
	}
	
	public static String get(String query, String toReturn) {
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
			P.print("[SQLcB-1] Starting connection with the database...");
			Connection connection = DriverManager.getConnection(dbAdress, dbID, dbPass);
			returnMsg = "Connection started.";
			
			//Creates a statement
			P.print("[SQLcB-2] Creating statement...");
			Statement statement = connection.createStatement();
			returnMsg = "Statement created.";
			
			//Starts the SQL query.
			P.print("[SQLcB-3] Executing SQL script...");
			ResultSet results = statement.executeQuery(exeScript);
			
			//Returns the requested record.
			System.out.println("[SQLcB-4] Outputting results...");
			while (results.next()) {returnMsg = results.getString(toReturn);}
			
			//Closes the connection then returns the result.
			connection.close();
			return returnMsg;
		} catch (Exception e) {
			e.printStackTrace();
			return "Error encountered: " + e;
		}
	}
	
	public static LinkedList<String> getList(String query, String toReturn) {
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
			P.print("[SQLcB-1] Starting connection with the database...");
			Connection connection = DriverManager.getConnection(dbAdress, dbID, dbPass);
			
			//Creates a statement
			P.print("[SQLcB-2] Creating statement...");
			Statement statement = connection.createStatement();
			
			//Starts the SQL query.
			P.print("[SQLcB-3] Executing SQL script...");
			ResultSet results = statement.executeQuery(exeScript);
			
			//Returns the requested record.
			System.out.println("[SQLcB-4] Outputting results...");
			while (results.next()) {returnList.add(results.getString(toReturn));}
			
			//Closes the connection then returns the result.
			connection.close();
			return returnList;
		} catch (Exception e) {
			e.printStackTrace();
			return returnList;
		}
	}
	
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
			Connection connection = DriverManager.getConnection(dbAdress, dbID, dbPass);
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
}
