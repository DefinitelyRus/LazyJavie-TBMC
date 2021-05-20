package commands;

import java.util.List;

import bot_init.LazyJavie;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Clear extends ListenerAdapter {
	//Checks if string is a number
	public static boolean isNumeric(String str)
	{
	    for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "clear")) {

			if (args.length < 2) {
				// Usage
				EmbedBuilder error = new EmbedBuilder();
				error.setColor(0xD82D42);
				error.setTitle(":x: You haven't specified the amount of messages to delete");
				error.setDescription("Command: '" + LazyJavie.prefix + "clear [# of messages]'" + "\r\n" + "You can only delete 2 - 100 messages one at a time.");
				event.getChannel().sendMessage(error.build()).queue();
				return;
			}
			else {
				// Checks if args[1] is not a number
				if (!isNumeric(args[1])) {
					P.print("<ClearCommand>Error: args[1] is not numeric.");
					EmbedBuilder high = new EmbedBuilder();
					high.setColor(0xD82D42);
					high.addField(":x: You've entered a non numeric input!", "Command: '" + LazyJavie.prefix + "clear [# of messages]'" + "\r\n" + "You can only delete 2 - 100 messages one at a time.", true);
					event.getChannel().sendMessage(high.build()).queue();
					return;
				}
				// Checks if the number of messages to be deleted is too large
				else if (Long.parseLong(args[1]) > 100 || args[1].length() > 3) {
					P.print("<ClearCommand>Error: args[1] is too large");
					EmbedBuilder high = new EmbedBuilder();
					high.setColor(0xD82D42);
					high.addField(":x: You've selected too much messages to be deleted!", "Command: '" + LazyJavie.prefix + "clear [# of messages]'" + "\r\n" + "You can only delete 2 - 100 messages one at a time.", true);
					event.getChannel().sendMessage(high.build()).queue();
					return;
				}
				// Checks if the number of messages to be deleted is too little
				else if (Integer.parseInt(args[1]) < 2 || args[1].length() < 1) {
					P.print("<ClearCommand>Error: args[1] is too small");
					EmbedBuilder low = new EmbedBuilder();
					low.setColor(0xD82D42);
					low.addField(":x: You've selected too little of messages to be deleted!", "Command: '" + LazyJavie.prefix + "clear [# of messages]'" + "\r\n" + "You can only delete 2 - 100 messages one at a time.", true);
					event.getChannel().sendMessage(low.build()).queue();
					return;
				}
				
				List<Message> messages = event.getChannel().getHistory().retrievePast(Integer.parseInt(args[1])).complete();
				try {
					event.getChannel().deleteMessages(messages).queue();
					
					EmbedBuilder success = new EmbedBuilder();
					success.setColor(0x77B255);
					success.setTitle(":white_check_mark:  Sucessfully deleted " + args[1] + " messages.");
					event.getChannel().sendMessage(success.build()).queue();
					P.print("<ClearCommand>Success: deleted " + args[1] + " messages");
				}
				catch (IllegalArgumentException e) {
 					if (e.toString().startsWith("java.lang.IllegalArgumentException: Message retrieval")) {
						// Too old
						P.print("<ClearCommand>Error: Messages selected are 2 weeks old");
						EmbedBuilder error = new EmbedBuilder();
						error.setColor(0xD82D42);
						error.setTitle(":x: Selected messages are older than 2 weeks or selected messages is not between 2 - 100.");
						error.setDescription("Messages that are older than 2 weeks cannot be deleted. You can only select messages between 2 - 100.");
						event.getChannel().sendMessage(error.build()).queue();
					}

				}
			}
		}
	}

}
