/*
 * 
 */
package commands;

import java.util.List;

import bot_init.LazyJavie;
import bot_init.SQLconnector;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TestRegister extends ListenerAdapter{
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		
		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "register")) {
			P.print("\nRegistration request: " + event.getMember().getUser());
			try {
				P.print("Attempting registration...");
				String password = args[1];
				List<Message> messages = event.getChannel().getHistory().retrievePast(2).complete();
				event.getChannel().deleteMessages(messages).queue();
				
				SQLconnector.createRecord(event.getMember().getId(), password);
				event.getChannel().sendMessage(event.getMember().getAsMention() + " has been registered successfully.").queue();
				P.print("Registered member: " + event.getMember().getUser() + "");
			}
			catch (Exception e) {
				if (e.toString().startsWith("java.lang.ArrayIndexOutOfBoundsException: Index")) {
					event.getChannel().sendMessage("Please enter a password. `>>register [password]`").queue();
				} else { event.getChannel().sendMessage("Error: `" + e + "`").queue();}

				P.print(e.toString());
			}
		}
	}
}
