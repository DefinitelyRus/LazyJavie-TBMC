package commands;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.exception.ExceptionUtils;

import home.Bot;
import home.SQLconnector;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TicketEmoteListener extends ListenerAdapter{
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		
		//Prevents bots from triggering this listener.
		if (event.getUserId().equals(Bot.jda.getSelfUser().getId())) {P.print("Self-report! Cancelling..."); return;}
		
		String msg_id1 = event.getMessageId();
		String msg_id2 = null;
		String ch_id = null;
		String cat_id = null;
		String newTicketName = null;
		String newMirrorName = null;
		
		try {
			msg_id2 = SQLconnector.get("select * from botsettings where name = 'ticket_message_id'", "value", false);
			ch_id = SQLconnector.get("select * from botsettings where name = 'ticket_channel_id'", "value", false);
			cat_id = SQLconnector.get("select * from botsettings where name = 'ticket_category_id'", "value", false);
		} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
		
		P.print("msg_id1 = " + msg_id1);
		P.print("msg_id2 = " + msg_id2);
		
		if (msg_id1.equals(msg_id2)) {
			P.print("New ticket in query requested by " + event.getUser().getAsTag() + "..."); //TODO Remove on final release.
			List<TextChannel> textChannels = event.getGuild().getTextChannels();
			List<TextChannel> ticketChannels = new LinkedList<TextChannel>();
			int highestValue = 0;
			
			//Adds every ticket channel to a list.
			for (TextChannel t : textChannels) {if (t.getName().startsWith("ticket")) ticketChannels.add(t);}
			
			//Adds every ticket channel's ID to a list.
			for (TextChannel ch : ticketChannels)
			{
				P.print("Adding " + ch.getName() + " to list.");
				highestValue = Integer.valueOf(ch.getName().replace("ticket-", ""));
				Bot.activeTickets.add((Integer) highestValue);
				P.print("Current highest value: " + highestValue);
			}
			
			//Loops until the new ticket ID has the largest value for redundancy.
			while (Bot.activeTickets.contains(highestValue)) highestValue++;
			
			//Assigns the channel names to a string variable.
			newTicketName = "ticket-" + String.format("%05d", highestValue);
			newMirrorName = "mirror-" + String.format("%05d", highestValue);
			
			//Creates new channels.
			P.print("Creating channels for #" + newTicketName);
			event.getGuild().getCategoryById(cat_id).createTextChannel(newTicketName).queue();
			event.getGuild().getCategoryById(cat_id).createTextChannel(newMirrorName).queue();
			
			//Pauses for 1 second; wait for local cache to refresh.
			try {TimeUnit.SECONDS.sleep(1);}
			catch (InterruptedException e) {e.printStackTrace();}
			
			//Refreshes text channel lists.
			ticketChannels = Bot.jda.getGuildById(event.getGuild().getId()).getTextChannelsByName("ticket", true);
			List<TextChannel> mirrorChannels = Bot.jda.getGuildById(event.getGuild().getId()).getTextChannelsByName("mirror", true);
			
			//TODO Automatically set roles for each ticket.
			
			for (TextChannel c : ticketChannels) {
				c.createPermissionOverride(c.getGuild().getRoleById("868739012301557851")).setAllow(Permission.ALL_TEXT_PERMISSIONS);
				
			}
		}
	}
}
