/*
 * ---------------!!! ADD TO README !!!---------------
 * Clear command is a standard moderation tool for cleaning channels, usually to clear out verbal issues between members.
 * A limitation of 2 to 100 messages have been set by the JDA developers for server-safety.
 * 
 * -	$clear <# of messages>
 * 		Will delete a specified number of messages sent within 14 days prior to the command sent.
 */

package commands;

import java.util.List;
import bot_init.LazyJavie;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Clear extends ListenerAdapter {
	
	//Checks if string is a number
	public static boolean isNumeric(String str) {
	    for (char c : str.toCharArray()) {
	        if (!Character.isDigit(c)) return false;
	    } return true;
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "clear") && event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
			P.print("\n[Clear] Bulk clear request: " + event.getMember().getUser().getName());
			String requestby = event.getMember().getUser().getName();
			//<CLEAR: MISSING ARGS>
			if (args.length < 2) {
				P.print("No amount specified; cancelling.");
				//Embed block
				EmbedBuilder error = new EmbedBuilder();
				error.setColor(0xD82D42);
				error.setTitle(":x: You haven't specified the amount of messages to delete");
				error.setDescription("Command: `" + LazyJavie.prefix + "clear <2 to 100>`" + "\r\n" + "You can only delete 2 - 100 messages one at a time.");
				error.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
				event.getChannel().sendMessage(error.build()).queue();
				return;
			}
			//<CLEAR: NOT INT> Checks if args[1] is not a number
			else if (!isNumeric(args[1])) {
				P.print("Argument is not an integer; cancelling.");
				//Embed block
				EmbedBuilder high = new EmbedBuilder();
				high.setColor(0xD82D42);
				high.addField(":x: You've entered a non numeric input!", "Command: `" + LazyJavie.prefix + "clear <2 to 100>`" + "\r\n" + "You can only delete 2 - 100 messages one at a time.", true);
				high.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
				event.getChannel().sendMessage(high.build()).queue();
				return;
			}
			//<CLEAR: TOO HIGH> Checks if the number of messages to be deleted is over 100.
			else if (Long.parseLong(args[1]) > 100 || args[1].length() > 3) {
				P.print("Argument too high; cancelling. (>100)");
				//Embed block
				EmbedBuilder high = new EmbedBuilder();
				high.setColor(0xD82D42);
				high.addField(":x: You've selected too much messages to be deleted!", "Command: `" + LazyJavie.prefix + "clear <2 to 100>`" + "\r\n" + "You can only delete 2 - 100 messages one at a time.", true);
				high.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
				event.getChannel().sendMessage(high.build()).queue();
				return;
			}
			//<CLEAR: TOO LOW> Checks if the number of messages to be deleted is below 2.
			else if (Integer.parseInt(args[1]) < 2 || args[1].length() < 1) {
				P.print("Argument too low; cancelling. (<2)");
				//Embed block
				EmbedBuilder low = new EmbedBuilder();
				low.setColor(0xD82D42);
				low.addField(":x: You've selected too little of messages to be deleted!", "Command: `" + LazyJavie.prefix + "clear <2 to 100>`" + "\r\n" + "You can only delete 2 - 100 messages one at a time.", true);
				low.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
				event.getChannel().sendMessage(low.build()).queue();
				return;
			}
			
			List<Message> messages = event.getChannel().getHistory().retrievePast(Integer.parseInt(args[1])).complete();
			try {
				//<CLEAR: SUCCESS>
				event.getChannel().deleteMessages(messages).queue();
				//Embed block
				EmbedBuilder success = new EmbedBuilder();
				success.setColor(0x77B255);
				success.setTitle(":white_check_mark:  Sucessfully deleted " + args[1] + " messages.");
				success.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
				event.getChannel().sendMessage(success.build()).queue();
				P.print("Successfully deleted " +args[1]+ " messages.");
				return;
			}
			catch (IllegalArgumentException e) {
				//Checks if the messages are too old.
				if (e.toString().startsWith("java.lang.IllegalArgumentException: Message retrieval")) {
					P.print("Messages too old; cancelling.");
					//Embed block
					EmbedBuilder error = new EmbedBuilder();
					error.setColor(0xD82D42);
					error.setTitle(":x: Selected messages are older than 2 weeks or selected messages is not between 2 - 100.");
					error.setDescription("Messages that are older than 2 weeks cannot be deleted. You can only select messages between 2 - 100.");
					error.setFooter("Requested by " + requestby , event.getMember().getUser().getAvatarUrl());
					event.getChannel().sendMessage(error.build()).queue();
					return;
				} else e.printStackTrace();

			}
			
		}
	}

}
