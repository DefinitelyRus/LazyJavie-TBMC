/*
 * 
 */

package bot_init;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import commands.P;

public class SQLconnector {
	//static String pass
	

    
    //Closes the scanner.
	
	//Initializing variables
	static int records = 0;
	static String dbAdress = "jdbc:mysql://localhost:3306/lazyjavie";
	static String dbID = "root";
	static String dbPass = "Rus69420";

	public static void main(String[] args) {
		
	}
	
	public static String createRecord(String username, String password) {
		

		/*
		 * KNOWN ISSUE:
		 * Since we have to test connection to databases, we need to host the database individually.
		 * The problem is that we don't have the same database passwords,
		 * this means we have to save the password along with the token file, or in another file.
		 * 
		 * But for some reason, the program errors out whenever I use the command
		 * but there's no error message.
		 */
		//TODO FIX THIS BUG AAAAAAAAAAAAAAAAAAAAAAAA
		print("1");
		try {
			String pass = null;
			File file = new File("C:\\lazyjavie_token.txt");
			
			print("2");
			Scanner reader = new Scanner(file);
			
			print("3");
		    while (reader.hasNextLine()) {
	    		pass = reader.nextLine();
		    }
		    
		    P.print("Password: " + pass);
		    reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	    //dbPass = "Rus69420";
		
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
	
	public static void print(String string) {
		System.out.println(string);
	}
}
