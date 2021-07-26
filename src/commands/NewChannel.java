package commands;

import java.awt.desktop.PrintFilesEvent;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.exception.ExceptionUtils;

import home.Bot;
import home.SQLconnector;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NewChannel extends ListenerAdapter{
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		
		if (args[0].equalsIgnoreCase(Bot.prefix + "newch")) {
			//try-catch
			String channelName = args[1];
			String categoryName = args[2];
			
			categoryName.replace('_', ' ');
			
			List<Category> categoryList = event.getGuild().getCategories();
			event.getChannel().sendMessage("Creating new channel `" +channelName+ "`.");
			
			for (Category c : categoryList) {
				if (c.getName().equals(categoryName)) {
					P.print("YEAH");
					c.createTextChannel(channelName).queue();
				}
			}
		}
		
		/**
		 * Creates a channel where members can create tickets
		 * by clicking on an emote on a premade bot message.
		 */
		if (args[0].equalsIgnoreCase(Bot.prefix + "createTicketChannel")) {

			String channelName = null;
			String categoryName = null;
			String emote = ":incoming_envelope:868713407749173248";
			String ticketMessage = "Need help? Click the emote below!";
			
			try {channelName = args[1].toLowerCase();}
			catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}

			try {categoryName = args[2].toLowerCase();}
			catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
			
			try {emote = args[3];}
			catch (ArrayIndexOutOfBoundsException e) {P.print("Optional custom emote not specified. Setting to default...");}
			catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
			
			if (channelName.equals(null) || categoryName.equals(null)) {P.print("Channel name and/or category names were not specified."); return;}
			
			//Filters the inputs.
			categoryName = categoryName.replace('_', ' ');
			emote = emote.replace("<", "");
			emote = emote.replace(">", "");
			
			P.print("Creating ticket channel `" + channelName + "` in `" + categoryName + "`.");
			event.getChannel().sendMessage("Dedicating ticket channel `" + channelName + "` in `" + categoryName + "`...").queue();
			
			long id = event.getGuild().getIdLong();
			List<Category> categoryList = Bot.jda.getGuildById(id).getCategories();
			
			for (Category c : categoryList) {
				if (c.getName().equalsIgnoreCase(categoryName)) {
					P.print("Category '" + categoryName + "' found. Finding channel '" + channelName + "'...");
					List<TextChannel> channels = c.getTextChannels();
					
					for (TextChannel ch : channels) {
						if (ch.getName().equalsIgnoreCase(channelName)) {
							P.print("Channel '" + channelName + "' found. Sending prompt message...");
							ch.sendMessage(ticketMessage).queue();
							
							//TODO Store in database.
							String txtch_id = ch.getId();
							
							P.print("Prompt sent. Adding emote...");
							List<Message> msgs = Bot.jda.getTextChannelById(txtch_id).getHistory().retrievePast(1).complete();
							for (Message m : msgs) {
								P.print(m.getContentRaw());
								m.addReaction(emote).queue();
								P.print("Emote added to message.");
								//break;
							}
							break;
						}
					}
					break;
				}
			}
		}
	}
}
