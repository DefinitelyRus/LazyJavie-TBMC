/*
 * 
 */
package commands;

import java.util.List;
import bot_init.LazyJavie;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Clear extends ListenerAdapter{
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		
		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "ping")) {
			P.print("\nClear (" + args[1] + ") request: " + event.getMember());
			int n = Integer.parseInt(args[1]);
			if (args.length < 2) {
				P.print("Request failed: No argument\n");
				event.getChannel().sendMessage("Specify how many messages you want to delete. (1-100)").queue();
			} else {
				P.print("Request considered.");
				if (n < 1) {
					P.print("Request is <2.");
					n = 2;
					args[1] = "2";
				} else if (n > 100) {
					P.print("Request is >100.");
					n = 100;
					args[1] = "100";
				}
				P.print("Attempting to clear...");
				List<Message> messages = event.getChannel().getHistory().retrievePast(n).complete();
				try {
					event.getChannel().deleteMessages(messages).queue();
					event.getChannel().sendMessage("Cleared " + args[1] + " messages.").queue();
					P.print("Cleared " + args[1] + " messages.\n");
				}
				catch (IllegalArgumentException e) {
					P.print("Request failed: " + e.toString());
					if (e.toString().startsWith("java.lang.IllegalArgumentException: Message retrieval limit")) {
						event.getChannel().sendMessage("I can only delete 2 to 100 messages.").queue();
						P.print("Failure cause: Out of range.\n");
					} else {
						event.getChannel().sendMessage("One or more messages in " + args[1] + " selected message are older than 2 weeks.").queue();
						P.print("Failure cause: Message too old.");
					}
				}
			}
		}
	}
}
