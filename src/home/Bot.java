package home;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.EnumSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import commands.NewMemberPrompter;
import commands.Quit;
import commands.Returns;
import commands.TicketAutoPrompter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Bot {
	
	//Initialization of user-coded objects and variables
	public static final String VERSION = "LazyJavie v2.0 ALPHA";
	public static JDA jda;
	public static List<Member> members = new LinkedList<Member>();
	public static boolean isAwake = false;
	public static String ticketMessage = "Need help? Click the emote below!";
	public static MessageEmbed ticketEmbed = null;
	public static List<Integer> activeTickets = new LinkedList<Integer>();
	public static Hashtable<String, String> ticketDictionary = new Hashtable<String, String>();
	
	//Variables changeable via commands.
	public static boolean tokenOverride = false;
	public static String token = "";
	public static String prefix = "$";
	public static Object currentChannel;
	public static boolean muted = false;
	
	/**
	 * Starts the bot with default settings.
	 */
	public static boolean start() {
		try {
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
			    Scanner scanner = new Scanner(file);
			    P.print("[A-3] Scanning...");
			    token = scanner.nextLine();
			    P.print("[A-4] Token assigned: " + StringUtils.substring(token, 0, 10) + "*".repeat(token.length()-10));
			    
			    //Closes the scanner.
			    scanner.close();
			    P.print("[A-5] Scanner closed.");
			    
			} else {P.print("[A-1] Getting token from control panel"); P.print("[A-2] Token assigned: " + token);}
		    
		    
			//[B] Logging in the bot----------------------------------------
			P.print("[B-1] Logging in...");
			try {
				jda = JDABuilder.createDefault(token)
						.setChunkingFilter(ChunkingFilter.ALL)
						.setMemberCachePolicy(MemberCachePolicy.ALL)
						.setEnabledIntents(EnumSet.allOf(GatewayIntent.class))
						.build();
				}
			catch (LoginException e) {P.print("'" +token+ "' is not a valid token."); return false;}
			catch (ErrorResponseException e) {P.print(e.toString() + " - likely caused by bad connection."); return false;}
			catch (Exception e) {P.print(e.toString());}
			
			
			P.print("[B-2] Setting status...");
			jda.getPresence().setStatus(OnlineStatus.ONLINE);
			jda.getPresence().setActivity(Activity.listening("to Rick Astley - Never Gonna Give You Up"));
			
			P.print("[B-3] Opening to commands...");
			//[IMPORTANT] Add new commands here.
			jda.addEventListener(new Quit());
			jda.addEventListener(new Returns());	
			jda.addEventListener(new TicketAutoPrompter());
			jda.addEventListener(new NewMemberPrompter());
			
			P.print("[B-4] Ready!");
			return true;
		}
		catch (FileNotFoundException e) {
			//[A] File not found.
			P.print("Missing file error:\n" + e.toString());
			SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e));
			return false;
		}
		catch (NoSuchElementException e) {
			//[A] File empty.
			P.print("Empty file error:\n" + e.toString());
			SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e));
			return false;
		}
		catch (NullPointerException e) {
			//[B] Bot likely not initialized
			P.print(e.toString() + " - Likely caused by a bad or no connection or an invalid token.");
			SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e));
			return false;
		}
		catch (Exception e) {
			//[A-B] Every other exception.
			P.print(ExceptionUtils.getStackTrace(e));
			SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e));
			return false;
		}
	}

	public static List<Guild> getGuilds() {
		return jda.getGuilds();
	}
	
	public static List<Member> getMembers(boolean removeDupes) {
		List<Member> members = new LinkedList<Member>();
		for (Guild g : jda.getGuilds()) {members.addAll(g.getMembers());}
		if (removeDupes == true) members = members.stream().distinct().collect(Collectors.toList());
		return members;
	}
}
