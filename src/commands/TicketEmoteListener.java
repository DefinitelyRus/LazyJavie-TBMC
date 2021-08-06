package commands;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.exception.ExceptionUtils;

import home.Bot;
import home.SQLconnector;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TicketEmoteListener extends ListenerAdapter{
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		
		//Prevents bots from triggering this listener.
		if (event.getUserId().equals(Bot.jda.getSelfUser().getId())) {P.print("[TicketEmoteListener] Self-report! Cancelling..."); return;}
		
		String msg_id1 = event.getMessageId();
		String msg_id2 = null;
		String ch_id = null;
		String cat_id = null;
		String resp_id = null;
		String newTicketName = null;
		String newMirrorName = null;
		
		//Gets the expected emote origin's message, channel, and the target category.
		try {
			msg_id2 = SQLconnector.get("select * from botsettings where name = 'ticket_message_id'", "value", false);
			ch_id = SQLconnector.get("select * from botsettings where name = 'ticket_channel_id'", "value", false);
			cat_id = SQLconnector.get("select * from botsettings where name = 'ticket_category_id'", "value", false);
			resp_id = SQLconnector.get("select * from botsettings where name = 'ticket_responder_role_id'", "value", false);
		} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
		
		if (msg_id1.equals(msg_id2)) {
			P.print("New ticket in query requested by " + event.getUser().getAsTag() + "..."); //TODO Remove on final release.
			List<TextChannel> textChannels = event.getGuild().getTextChannels();
			List<TextChannel> ticketChannels = new LinkedList<TextChannel>();
			int highestValue = 0;
			
			//Adds every ticket channel to a list.
			for (TextChannel t : textChannels) {if (t.getName().startsWith("ticket")) ticketChannels.add(t);}
			
			//Adds every ticket channel's ID to Bot.activeTickets.
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
			
			//Creates a ticket-maker role.
			event.getGuild().createRole().setName("ticketter-" + highestValue).queue();
			
			//Pauses for 1 second; wait for local cache to refresh.
			try {TimeUnit.SECONDS.sleep(1);}
			catch (InterruptedException e) {e.printStackTrace();}
			
			//Sets the role name and allows the role to view the ticket channel.
			List<TextChannel> ticketChannelSolo = new LinkedList<TextChannel>();
			ticketChannelSolo = Bot.jda.getGuildById(event.getGuild().getId()).getTextChannelsByName(newTicketName, false);
			
			//Binds a list of perms to a list.
			List<Permission> perms = new LinkedList<Permission>();
			perms.add(Permission.VIEW_CHANNEL); perms.add(Permission.MESSAGE_ADD_REACTION);
			perms.add(Permission.MESSAGE_ATTACH_FILES); perms.add(Permission.MESSAGE_EXT_EMOJI);
			perms.add(Permission.MESSAGE_HISTORY); perms.add(Permission.MESSAGE_READ);
			perms.add(Permission.MESSAGE_WRITE); perms.add(Permission.USE_SLASH_COMMANDS);

			//Gives ticket maker to chat in ticket-X but not mirror-X, and vice-versa.
			P.print("|Finding target channel...");
			for (TextChannel c : ticketChannelSolo) {
				P.print("|Main channel found! " + c.getName());
				c.createPermissionOverride(event.getMember()).setAllow(perms).queue();
				c.createPermissionOverride(Bot.jda.getRoleById(resp_id)).setDeny(perms).queue();
			}
			
			ticketChannelSolo = Bot.jda.getGuildById(event.getGuild().getId()).getTextChannelsByName(newMirrorName, false);
			
			for (TextChannel c : ticketChannelSolo) {
				P.print("|Mirror channel found! " + c.getName());
				c.createPermissionOverride(event.getMember()).setDeny(perms).queue();
				c.createPermissionOverride(Bot.jda.getRoleById(resp_id)).setAllow(perms).queue();
			}
			
			return;
		}
		
		
	}
}
