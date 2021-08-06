package commands;

import java.util.List;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TicketMessageExchange extends ListenerAdapter{
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		String botID = event.getJDA().getSelfUser().getId();
		
		//Mirrors any message sent to their corresponding channel.
		if (event.getChannel().getName().matches("ticket-[0-9]{5}") && !event.getMember().getId().equals(botID)) {
			String mirrorName = event.getChannel().getName().replace("ticket-", "mirror-");
			
			List<TextChannel> mirrorSolo = event.getGuild().getTextChannelsByName(mirrorName, true);
			
			for (TextChannel c : mirrorSolo) {
				c.sendMessage(event.getMessage()).queue();
			}
		}
		
		//Same function as before, but reverse.
		if (event.getChannel().getName().matches("mirror-[0-9]{5}") && !event.getMember().getId().equals(botID)) {
			String mirrorName = event.getChannel().getName().replace("mirror-", "ticket-");
			
			List<TextChannel> mirrorSolo = event.getGuild().getTextChannelsByName(mirrorName, true);
			
			for (TextChannel c : mirrorSolo) {
				c.sendMessage(event.getMessage()).queue();
			}
		}
	}
}
