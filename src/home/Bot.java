package home;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import javax.security.auth.login.LoginException;

import org.apache.commons.lang3.exception.ExceptionUtils;

import commands.MessageReceivedEvent;
import commands.P;
import commands.Quit;
import commands.Returns;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Bot {
	
	//Initialization of user-coded objects and variables
	public static final String VERSION = "LazyJavie v2.0 ALPHA";
	public static JDA jda;
	
	//Variables changeable in UI.
	public static boolean tokenOverride = false;
	public static String token = "";
	public static String prefix = "$";
	public static Object currentChannel;
	
	public static void start() {
		try {
			//SQLconnector.NoDBfixer();
			
			P.print("Starting " + VERSION + "...");
			//[A] Getting the Token----------------------------------------
			
			/*
			 * This part is important because if this step is skipped,
			 * the discord bot's API token is visible publicly on GitHub.
			 * 
			 * This solution allows the token to be stored locally on the host's computer,
			 * while still having the source code be readily available on the GitHub repository.
			 */
			
			if (tokenOverride == false) {
				//Looks for the text file "lazyjavie_token.txt".
				P.print("[A-1] Getting token from file");
				File file = new File("C:\\lazyjavie_token.txt");
				P.print("[A-2] File found.");
				
				//Scans the file.
			    Scanner reader = new Scanner(file);
			    P.print("[A-3] Scanning...");
			    while (reader.hasNextLine()) {token = reader.nextLine(); break;}
			    P.print("[A-4] Token assigned: " + token);
			    
			    //Closes the scanner.
			    reader.close();
			    P.print("[A-5] Scanner closed.");
			    
			} else {P.print("[A-1] Getting token from control panel"); P.print("[A-2] Token assigned: " + token);}
		    
		      
			//[B] Logging in the bot----------------------------------------
			P.print("[B-1] Logging in...");
			try {jda = JDABuilder.createDefault(token).enableIntents(GatewayIntent.GUILD_MEMBERS).build();}
			catch (LoginException loginExc) {P.print("'" +token+ "' is not a valid token."); return;}
			catch (Exception otherExc) {P.print(otherExc.toString());}
			
			P.print("[B-2] Setting status...");
			jda.getPresence().setStatus(OnlineStatus.ONLINE);
			jda.getPresence().setActivity(Activity.listening("to Rick Astley - Never Gonna Give You Up"));
			
			P.print("[B-3] Opening to commands...");
			//[IMPORTANT] Add new commands here.
			jda.addEventListener(new MessageReceivedEvent());
			jda.addEventListener(new Quit());
			jda.addEventListener(new Returns());

			P.print("[B-4] Ready!");
			return;
		}
		catch (FileNotFoundException e) {
			//[A] File not found.
			P.print("Missing file error: " + e.toString());
			SQLconnector.errorLog(e.toString(), ExceptionUtils.getStackTrace(e), Bot.VERSION);
			return;
		}
		catch (NullPointerException e) {
			//[B] Bot likely not initialized
			P.print(e.toString() + " - Likely caused by an invalid token.");
			SQLconnector.errorLog(e.toString(), ExceptionUtils.getStackTrace(e), Bot.VERSION);
			return;
		}
		catch (Exception e) {
			//[A-B] Every other exception.
			P.print(e.toString());
			e.printStackTrace();
			SQLconnector.errorLog(e.toString(), ExceptionUtils.getStackTrace(e), Bot.VERSION);
			return;
		}
	}

	public static List<Guild> getGuilds() {
		return jda.getGuilds();
	}
}
