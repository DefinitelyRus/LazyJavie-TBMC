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

/**
 * This serves as the connector between the program and the local SQL server.
 * Given the complexity of this program, we decided to put all MySQL-related
 * code in this module so all we have to do is call a function if we ever
 * need to get records from the database or update a table.
 * @author DefinitelyRus
 *
 */
public class SQLconnector {
	//Initializing variables
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_ADDRESS = "jdbc:sqlite:lazyjavie.db";
	static final String DEFAULT_ADDRESS = "jdbc:sqlite:lazyjavie.db";
	static final String DB_LOGIN_ID = "root";
	static String dbPass = "password"; //Placeholder. I'm not that dumb.
	static String ps_dir = "C:\\lazyjavie_token.txt";
	
	//-------------------------UPDATE-------------------------
	/**Can be used to add, delete, or modify tables and its contents. This method does not return any values.
	 * If you wish to retrieve records from the database, use SQLconnector.get().
	 * 
	 * <p>Usage:
	 * <br>update("UPDATE [table] SET [new value] WHERE [column] = [old value]", true) <p>
	 * <p>This will set all values in a column to 0 where the value is 10.<p>
	 * 
	 * @param query An SQL query like UPDATE-WHERE-SET. Note that you can only use this for one statement at a time.
	 * @param tp Boolean whether running this function should print what it's currently doing.
	 * @throws LoginException
	 * @throws SQLException
	 */
	public static void update(String query, boolean tp) {
		//Initialization
		String exeScript = query;
		dbPass = getPass();
		
		try {
			if (tp == true) {P.print("|[SQLcA-1] Starting connection with the database...");}
			Connection connection = DriverManager.getConnection(DB_ADDRESS, DB_LOGIN_ID, dbPass);
			
			if (tp == true) {P.print("|[SQLcA-2] Creating statement...");}
			Statement statement = connection.createStatement();
			
			if (tp == true) {P.print("|[SQLcA-3] Executing SQL script...");}
			statement.execute(exeScript);
			
			if (tp == true) {P.print("|[SQLcA-4] Done!");}
			
			connection.close();
		}
		catch (SQLException e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
		catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
	}
	
	//-------------------------GET VALUE-------------------------
	/**Returns a record from the database.
	 * 
	 * <p>Usage:
	 * <br>SQLconnector.get("select * from database.table where username is not null", "username")
	 * <br>This will return the last item in the table that fits the condition.<p>
	 * 
	 * @param query An SQL query like SELECT-WHERE. Note that you can only use this for one statement at a time.
	 * @param toReturn A column or value you want returned.
	 * @param tp Boolean whether running this function should print what it's currently doing.
	 * @return The last or only string value that qualify the SQL query.
	 * @see getList(String query, String toReturn, boolean tp) - for getting a list of values.
	 */
	public static String get(String query, String toReturn, boolean tp) {
		//Initialization
		String returnMsg = null;
		dbPass = getPass();
		
		try {
			//Starts a connection to the database using the JDBC driver.
			if (tp == true) {P.print("|[SQLcB-1] Starting connection with the database...");}
			Connection connection = DriverManager.getConnection(DB_ADDRESS, DB_LOGIN_ID, dbPass);
			
			//Creates a statement
			if (tp == true) {P.print("|[SQLcB-2] Creating statement...");}
			Statement statement = connection.createStatement();
			
			//Starts the SQL query.
			if (tp == true) {P.print("|[SQLcB-3] Executing SQL script...");}
			ResultSet results = statement.executeQuery(query);
			
			//Returns the requested record.
			if (tp == true) {P.print("|[SQLcB-4] Outputting results...");}
			while (results.next()) {returnMsg = results.getString(toReturn);}
			
			//Closes the connection then returns the result.
			connection.close();
			return returnMsg;
		}
		catch (SQLException e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString()); return "Error encountered: " + e;}
		catch (Exception e) {
			SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());
			return "Error encountered: " + e;
		}
	}
	
	//-------------------------GET LIST-------------------------
	/**Returns a list of records from the database.
	 * 
	 * <p>Usage:
	 * <br>SQLconnector.get("select * from database.table where username is not null", "username")
	 * <br>This will return all the items in the table that fit this condition into a linked list.<p>
	 * 
	 * @param query An SQL query like SELECT-WHERE. Note that you can only use this for one statement at a time.
	 * @param toReturn A column or value you want returned.
	 * @param tp Boolean whether running this function should print what it's currently doing.
	 * @return A list (LinkedList) of results according to the SQL query and toReturn (column) parameters.
	 * @throws LoginException
	 * @throws SQLException
	 * @see get(String query, String toReturn, boolean tp) - for getting single values.
	 */
	public static LinkedList<String> getList(String query, String toReturn, boolean tp) {
		//Initialization
		LinkedList<String> returnList = new LinkedList<String>(); returnList.add("");
		String exeScript = query;
		dbPass = getPass();
		
		try {
			//Starts a connection to the database using the JDBC driver.
			if (tp == true) {P.print("|[SQLcB-1] Starting connection with the database...");}
			Connection connection = DriverManager.getConnection(DB_ADDRESS, DB_LOGIN_ID, dbPass);
			
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
		}
		catch (SQLException e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString()); return returnList;}
		catch (Exception e) {
			SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());
			return returnList;
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
				updateExternal(line, DEFAULT_ADDRESS, true);
			}
			P.print("|[SQLcD-4] New database created.\n");
		}
		catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
	}
	
	//-------------------------UPDATE EXTERNAL-------------------------
	/**<p>In the event that the target database is different, this function is called instead of update().<p>
	 * @param query An SQL query like SELECT-WHERE. Note that you can only use this for one statement at a time.
	 * @param dbAddress The target database address.
	 * @param tp (Boolean) Whether running this function should print what it's currently doing.
	 * @throws LoginException
	 * @throws SQLException
	 */
	public static void updateExternal(String query, String dbAddress, boolean tp) {
		
		dbPass = getPass();
		
		try {
			//Starts a connection to the database using the JDBC driver.
			if (tp == true) {P.print("|[SQLcB-1] Starting connection with the database...");}
			Connection connection = DriverManager.getConnection(dbAddress, DB_LOGIN_ID, dbPass);
			
			//Creates a statement
			if (tp == true) {P.print("|[SQLcB-2] Creating statement...");}
			Statement statement = connection.createStatement();
			
			//Starts the SQL query.
			if (tp == true) {P.print("|[SQLcB-3] Executing SQL script...");}
			statement.execute(query);
			
			//Closes the connection to the database.
			connection.close();
		}
		catch (SQLException e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
		catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
	}
	
	//-------------------------CALL ERROR-------------------------
	/**<p>Saves the error message to the database for later debugging. Note that this is saved along with the discord bot's settings and saved data.
	 * <br><br>Usage:
	 * <br>catch (Exception e) callError(e.toString(), ExceptionUtils.getStackTrace(e));
	 * <br><br>Shortcut with print:
	 * <br>SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());</p>
	 * 
	 * @param message Exception.toString() - The error code and a short description.
	 * @param error ExceptionUtils.getStackTrace(e)) - The error code, description, and stack tracing.
	 */
	public static void callError(String message, String error) {
		P.print("\nError Received: \n" + error);
		String exeScript = "insert into errorlog (err_type, err_stacktrace, eventdate, appver) values ('" +message+ "', '" +error+ "', datetime(), '" +Bot.VERSION+ "');";
		dbPass = getPass();
		
		try {
			Connection connection = DriverManager.getConnection(DB_ADDRESS, DB_LOGIN_ID, dbPass);
			Statement statement = connection.createStatement();
			statement.execute(exeScript);
			connection.close();
		}
		catch (SQLException e) {P.print("callError() failed. Please send the following error code to the developer:\n" +ExceptionUtils.getStackTrace(e));}
		catch (Exception e) {P.print("callError() failed. Please send the following error code to the developer:\n" +ExceptionUtils.getStackTrace(e));}
	}
	
	//-------------------------GET ROW & COLUMN SIZE-------------------------
	/**
	 * Gets the number of rows and columns in a given table.
	 * @param tableName The target table.
	 * @return int[rows, columns]
	 */
	public static int[] getXY(String tableName) {
		int x = 0, y = 0;
		final String getX = "select count(*) from " +tableName+ ";";
		final String getY = "select * from " +tableName+ " where 1=2;";
		dbPass = getPass();
		
		try {
			//Starts a connection to the database using the JDBC driver.
			Connection connection = DriverManager.getConnection(DB_ADDRESS, DB_LOGIN_ID, dbPass);
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
	
	//-------------------------GET RESULT SET-------------------------
	public static Connection con = null;
	/**Primarily used to get data outside of individual tables or about the table themselves.
	 * 
	 * @param query An SQL query like SELECT-WHERE. Note that you can only use this for one statement at a time.
	 * @return A ResultSet object containing data about the query.
	 * @throws SQLException 
	 */
	public static ResultSet getResultSet(String query) throws SQLException {
		con = DriverManager.getConnection(DB_ADDRESS, DB_LOGIN_ID, dbPass);
		ResultSet resultSet = con.createStatement().executeQuery(query);
		return resultSet;
	}
	
	//-------------------------GET DATABASE PASSWORD-------------------------
	/**
	 * Gets the password for the database from a locally-stored text file.
	 * @return The database's password.
	 */
	static String getPass() {
		try {
			String dbpass = "";
			File file = new File(ps_dir);
			Scanner reader = new Scanner(file);
		    while (reader.hasNextLine()) {dbpass = reader.nextLine();}
		    reader.close();
		    return dbpass;
		    }
		catch (FileNotFoundException e) {
			P.print("404: lazyjavie_token.txt is missing from target directory.");
			return null;
			
			//TODO Autofix
			}
		catch (Exception e) {
			P.print(ExceptionUtils.getStackTrace(e));
			return null;
			}
	}
	
	//-------------------------PRINT SHORTCUT-------------------------
	/**
	 * Identical in function to System.out.println().
	 * This function was made to shorten the process of printing text to console.
	 * @param s String to print.
	 */
	@SuppressWarnings("unused")
	private static void print(String s) {System.out.println(s);}
}