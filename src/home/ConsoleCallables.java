package home;

import javax.annotation.Nullable;

import org.apache.commons.lang3.exception.ExceptionUtils;

import commands.P;
import commands.Quit;

public class ConsoleCallables {
	
	public static void main(String args[]) {
		String tokenOverride = null;
		P.print("[LazyJavie] Pre-startup sequence...");
		P.print("|Looking for LazyJavie.db...");
		if (SQLconnector.dbCheck() == false) {
			P.print("|Attempting to create a new database...");
			SQLconnector.NoDBfixer();
		}
		
		//Attempts to get a replacement token saved from database.
		tokenOverride = SQLconnector.get("select * from botsettings where name = 'discord_bot_token_override'", "value", false);
		P.print(tokenOverride);
		
		//Prompts the bot for startup with the assigned token.
		P.print("|Attempting to start LazyJavie...");
		//startBot("Start", tokenOverride);
	}
	
	public static void startBot(String task, @Nullable String botTokenOverride) {
		if (task.equalsIgnoreCase("Start")) {
			Bot.isAwake = true;
			
			//Determines whether the token should be grabbed from the UI or from system.
			if (botTokenOverride == null) {Bot.tokenOverride = false;}
			else {Bot.tokenOverride = true; Bot.token = botTokenOverride;}
			
			/* Finally starts the bot.
			 * 
			 * Bot.start() returns a boolean value which corresponds to whether
			 * it was successful in starting up. Whenever it returns false,
			 * the following line will close the program automatically.
			 */
			P.print("Awaiting startup...");
			boolean status = Bot.start();
			if (status == false) return;
			
			//Waits until the bot is ready. (Otherwise, the code will continue but the cache isn't yet ready, causing errors.)
			try {Bot.jda.awaitReady();}
			catch (InterruptedException e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
			catch (NullPointerException e) {SQLconnector.callError(e.toString() + " - likely caused by bad connection.", ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
			
			return;
		
		} else if (task.equalsIgnoreCase("Stop")) {
			Bot.isAwake = false;
			Quit.softExit();
			return;
		}
	}
}
