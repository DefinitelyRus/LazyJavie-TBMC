/*
 * 
 */

package bot_init;

import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.ResultSet;
import java.sql.Statement;

public class SQLconnector {
	//Initializing variables
		static int records = 0;
		static String dbAdress = "jdbc:mysql://localhost:3306/lazydfjavie";
		static String dbID = "root";
		static String dbPass = "lol no";

		public static void main(String[] args) {
//			try {
//				//Starts a connection to the database using the JDBC driver.
//				System.out.println("[1] Starting connection with the database...");
//				Connection connection = DriverManager.getConnection(dbAdress, dbID, dbPass);
//				
//				//Creates a statement
//				System.out.println("[2] Creating statement...");
//				Statement statement = connection.createStatement();
//				
//				//Starts the SQL query.
//				System.out.println("[3] Executing SQL query...");
//				ResultSet resultset = statement.executeQuery("select * from member_registry");
//				
//				//Processes the result set.
//				System.out.println("[4] Outputting results...");
//				while (resultset.next()) {
//					//Counts how many records exists in the database.
//					records = resultset.getInt("id");
//				}
//				
//				System.out.println("[4.1] No. of records: " + records);
//				createRecord("TEST", "TEST");
//				
//				connection.close();
//			}
//			catch (Exception e) {
//				e.printStackTrace();
//			}
		}
		
		public static String createRecord(String username, String password) {
			String returnMsg = "Attempting to create new record.";
			String exeScript = "insert into lazyjavie.member_registry(userid, userpass) values (\"" + username + "\", \"" + password + "\")";
			try {
				//Starts a connection to the database using the JDBC driver.
				System.out.println("[8] Starting connection with the database...");
				Connection connection = DriverManager.getConnection(dbAdress, dbID, dbPass);
				returnMsg = "Connection started.";
				
				//Creates a statement
				System.out.println("[9] Creating statement...");
				Statement statement = connection.createStatement();
				returnMsg = "Statement created.";
				
				//Starts the SQL query.
				System.out.println("[10] Executing SQL script...");
				statement.execute(exeScript);
				returnMsg = "Script executed.";
				
				System.out.println("[11] Done!");
				returnMsg = "All finished.";
				
				connection.close();
				return returnMsg;
			}
			catch (Exception e) {
				e.printStackTrace();
				return "Error encountered" + e;
			}
		}
		
		public static String updatePassword(String username, String password, String s) {
			return "";
		}

}
