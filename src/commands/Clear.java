package commands;

import java.util.List;

import bot_init.LazyJavie;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Clear extends ListenerAdapter {
	
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
			}
			else {
				List<Message> messages = event.getChannel().getHistory().retrievePast(Integer.parseInt(args[1])).complete();
				try {
					event.getChannel().deleteMessages(messages).queue();
					
					EmbedBuilder success = new EmbedBuilder();
					success.setColor(0x77B255);
					success.setTitle(":white_check_mark: Sucessfully deleted " + args[1] + " messages.");
					event.getChannel().sendMessage(success.build()).queue();
				}
				catch (IllegalArgumentException e) {
 					if (e.toString().startsWith("java.lang.IllegalArgumentException: Message retrieval")) {
						// Too many messages
						EmbedBuilder error = new EmbedBuilder();
						error.setColor(0xD82D42);
						error.setTitle(":x: You've selected too many messages to be deleted");
						error.setDescription("You can only delete 1 - 100 messages once at a time.");
						event.getChannel().sendMessage(error.build()).queue();
					}

					else {
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
