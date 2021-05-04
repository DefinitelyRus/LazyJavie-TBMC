/*
 * 
 */

package bot_init;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.security.auth.login.LoginException;
import commands.Clear;
import commands.Returns;
import commands.TestRegister;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class LazyJavie {
	
	//Initialization of objects and variables
	public static JDA jda;
	public static String prefix = ">>";
	public static String token = "";
	
	//Startup
	public static void main(String[] args) throws LoginException {
		try {
			//[A] Getting the Token----------------------------------------
			print("[A-1] Getting token from file");
			/*
			 * This part is important because if this step is skipped,
			 * the discord bot's API token is visible publicly on GitHub.
			 * 
			 * This solution allows the token to be stored locally on the host's computer,
			 * while still having the source code be readily available on the GitHub repository.
			 */
			
			//Looks for the text file "lazyjavie_token.txt".
			File file = new File("F:\\Personal Files\\Project Files\\Programming Projects\\Git Repositories\\Discord Bot\\lazyjavie_token.txt");
			print("[A-2] File found.");
			//Scans the file.
		    Scanner reader = new Scanner(file);
		    print("[A-3] Scanning...");
		    while (reader.hasNextLine()) {token = reader.nextLine();}
		    print("[A-4] Token assigned.");
		    
		    //Closes the scanner.
		    reader.close();
		    print("[A-5] Scanner closed.");
		      
			//[B] Logging in the bot----------------------------------------
			print("[B-1] Logging in...");
			jda = JDABuilder.createDefault(token).build();
			
			print("[B-2] Setting status...");
			jda.getPresence().setStatus(OnlineStatus.ONLINE);
			jda.getPresence().setActivity(Activity.listening("to Rick Astley - Never Gonna Give You Up"));
			
			print("[B-3] Opening to commands...");
			//[IMPORTANT] Add new commands here.
			jda.addEventListener(new Returns());
			jda.addEventListener(new TestRegister());
			jda.addEventListener(new Clear());
			
			print("[B-4] Ready!");
			
		}
		catch (FileNotFoundException file404) {
			//[A] File not found.
			print("Missing file error.\n" + file404.toString());
		}
		catch (Exception e) {
			//[A-B] Every other exception.
			print(e.toString());
			e.printStackTrace();
		}
	}
	
	public static void print(String str) {
		//I couldn't be bothered to type this every single time I need to test something.
		//So I made this function to make life easier.
		System.out.println(str);
	}
}