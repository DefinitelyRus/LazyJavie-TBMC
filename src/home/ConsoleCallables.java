package home;

import javax.annotation.Nullable;

import org.apache.commons.lang3.exception.ExceptionUtils;
import commands.P;
import commands.Quit;

public class ConsoleCallables {
	
	public static void main(String args[]) {
		startBot("Start", null);
	}
	
	public static void startBot(String taskName, @Nullable String botTokenOverride) {
		if (taskName.equalsIgnoreCase("Start")) {
			Bot.isAwake = true;
			
			//Determines whether the token should be grabbed from the UI or from system.
			if (botTokenOverride.equals("") || botTokenOverride.equals(null)) {Bot.tokenOverride = false;}
			else {Bot.tokenOverride = true; Bot.token = botTokenOverride;}
			
			//Finally starts the bot.
			Bot.start();
			
			//Waits until the bot is ready. (Otherwise, the code will continue but the cache isn't yet ready, causing errors.)
			try {Bot.jda.awaitReady();}
			catch (InterruptedException e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
			catch (NullPointerException e) {SQLconnector.callError(e.toString() + " - likely caused by bad connection.", ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
			
			P.print("Console ready!");
			return;
		
		} else if (taskName.equalsIgnoreCase("Stop")) {
			Bot.isAwake = false;
			Quit.softExit();
			
			//Look for an alternative way to delay changing the text
			return;
		}
	}
}
