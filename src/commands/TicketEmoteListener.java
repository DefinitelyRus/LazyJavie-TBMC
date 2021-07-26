package commands;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

//TODO Figure out how to make this work
public class TicketEmoteListener extends ListenerAdapter{
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		P.print("Emote received! " + event.getMessageId());
		String msg_id1 = event.getMessageId();
		String msg_id2 = "868733880973283359";
		String cat_id = "857170103993106432";
		String ticket_id = null;
		int ticket_id_int = 0;
		String newTicketName = null;
		P.print(String.valueOf(ticket_id));
		
		List<String> ticketList = new LinkedList<String>(); //SQLconnector.getList("select * from TABLE", "id", false);
		
		//TODO Add an option to ensable random ticket numbering.
		//Random randint = new Random();
		//ticket_id = String.format("%05d", randint.nextInt(99999));
		
		//Prevents duplicates.
		while (ticketList.contains(ticket_id)) {
			ticket_id_int++;
			
			//Add when random numbering is ready.
			//ticket_id = String.format("%05d", randint.nextInt(99999));
		}
		
		if (msg_id1.equals(msg_id2)) {
			P.print("New ticket in query...");
			
			event.getGuild().getCategoryById(cat_id).createTextChannel(newTicketName);
		}
	}
}
