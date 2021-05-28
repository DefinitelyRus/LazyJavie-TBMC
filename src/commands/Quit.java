package commands;

import java.util.concurrent.TimeUnit;

import bot_init.LazyJavie;
import bot_init.SQLconnector;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Quit extends ListenerAdapter{
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		String msg = event.getMessage().getContentRaw();
		
		try {
			boolean isAdmin = event.getMember().hasPermission(Permission.ADMINISTRATOR);
			
			if (args[0].equalsIgnoreCase(LazyJavie.prefix + "quit")) {
				P.print("\n[quit] Exit request by: " + event.getMember().getUser().getName());
			}
			
			if (args[0].equalsIgnoreCase(LazyJavie.prefix + "quit") && isAdmin == false) {
				P.print("Requester is not an admin; cancelling.");
				event.getChannel().sendMessage("Only admins can do that!").queue();
				return;
			}
			else if (args[0].equalsIgnoreCase(LazyJavie.prefix + "quit") && !args[1].equalsIgnoreCase("confirm") && isAdmin == true) {
				P.print("args[1] is not 'confirm'; cancelling.");
				event.getChannel().sendMessage(args[1] + " is not 'confirm'.").queue();
			}
			else if (args[0].equalsIgnoreCase(LazyJavie.prefix + "quit") && args[1].equalsIgnoreCase("confirm") && isAdmin == true) {
				P.print("Shutting down LazyJavie " +LazyJavie.version(false, false)+ ".");
				event.getChannel().sendMessage("Bye bye!").queue();
				TimeUnit.SECONDS.sleep(5);
				exit();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			P.print("Error ignored: Missing args.");
			event.getChannel().sendMessage(":warning: Are you sure you want to disconnect the bot?\nEnter `" +LazyJavie.prefix+ "quit confirm` to confirm.").queue();
			SQLconnector.callError(msg, "[QUIT] MISSING ARGS");
			return;
		} catch (Exception e) {P.print(e.toString()); return;}
	}
	
	public void exit() {
		System.exit(0);
		return;
	}
}
