/*
 * Lmao
 */
package commands;

import java.util.concurrent.TimeUnit;

import home.Bot;
import home.SQLconnector;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Quit extends ListenerAdapter{
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		String msg = event.getMessage().getContentRaw();
		
		try {
			boolean isAdmin = event.getMember().hasPermission(Permission.ADMINISTRATOR);
			
			if (args[0].equalsIgnoreCase(Bot.prefix + "quit")) {
				P.print("\n[quit] Exit request by: " + event.getMember().getUser().getName());
			}
			
			//Case: User is not an admin.
			if (args[0].equalsIgnoreCase(Bot.prefix + "quit") && isAdmin == false) {
				P.print("Requester is not an admin; cancelling.");
				event.getChannel().sendMessage("Only admins can do that!").queue();
				return;
			}
			
			//Case: User is an admin and completes the default prompt.
			else if (args[0].equalsIgnoreCase(Bot.prefix + "quit") && args[1].equalsIgnoreCase("confirm") && isAdmin == true) {
				P.print("Shutting down LazyJavie 2.0 ALPHA.");
				event.getChannel().sendMessage("Bye bye!").queue();
				TimeUnit.SECONDS.sleep(5);
				exit();
			}
			
			//Case: User is an admin and completes the soft-exit prompt.
			else if (args[0].equalsIgnoreCase(Bot.prefix + "quit") && args[1].equalsIgnoreCase("confirmKeepUI") && isAdmin == true) {
				event.getChannel().sendMessage("Bye bye!").queue();
				TimeUnit.SECONDS.sleep(5);
				softExit();
			}

			//Case: User is an admin, but doesn't complete the prompt.
			else if (args[0].equalsIgnoreCase(Bot.prefix + "quit") && isAdmin == true) {
				P.print("args[1] is not 'confirm'; cancelling.");
				event.getChannel().sendMessage(args[1] + " is not 'confirm' or 'confirmKeepUI'.").queue();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			P.print("Error ignored: Missing args.");
			event.getChannel().sendMessage(":warning: Are you sure you want to disconnect the bot?\nEnter `" +Bot.prefix+ "quit confirm` to confirm.").queue();
			SQLconnector.callError(msg, "[QUIT] MISSING ARGS");
			return;
		} catch (Exception e) {P.print(e.toString()); return;}
	}
	
	//For use in UI.
	public static void softExit() {
		P.print("Shutting down LazyJavie 2.0 ALPHA.");
		try {TimeUnit.SECONDS.sleep(5);}
		catch (InterruptedException e) {P.print(e.toString());}
		Bot.jda.shutdown();
	}
	
	public static void exit() {
		System.exit(0);
		return;
	}
}