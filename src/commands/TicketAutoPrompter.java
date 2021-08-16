package commands;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.exception.ExceptionUtils;

import home.Bot;
import home.SQLconnector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TicketAutoPrompter extends ListenerAdapter {
	public void onTextChannelCreate (TextChannelCreateEvent event) {
		String chName = event.getChannel().getName();
		TextChannel channel = event.getChannel();
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(0xFF8822);
		embed.setTitle("Need help? ");
		embed.setDescription("\nPress :one: if you're having trouble **joining for the first time**." +
							"\n\nPress :two: if you **lost items** due to a **crash or glitch**." +
							"\n\nPress :three: if you've been **griefed or stolen from**." +
							"\n\nPress :four: if you **can't join** the server." +
							"\n\nPress :five: if it's **NONE** of the above.");
		embed.setFooter("DO NOT SKIP THIS PROMPT. This will help us gather information to fix your issue.");
		embed.setAuthor("TetrabearMC");
		MessageEmbed msgEmbed = embed.build();
		
		P.print("New Channel Detected: " + chName);
		
		if (chName.matches("ticket-[0-9]{4}")) {
			P.print("Detected new ticket!");
			
			channel.sendMessage(msgEmbed).queue();
			
			try {TimeUnit.MILLISECONDS.sleep(400);} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
			
			List<Message> msgs = Bot.jda.getTextChannelById(channel.getId()).getHistory().retrievePast(1).complete();
			Message promptMsg = null;
			
			for (Message m : msgs) {
				//Application queries
				m.addReaction("\u0031\u20E3").queue();
				
				//Lost items
				m.addReaction("\u0032\u20E3").queue();
				
				//Griefing reports
				m.addReaction("\u0033\u20E3").queue();
				
				//Joining issues
				m.addReaction("\u0034\u20E3").queue();
				
				//Others
				m.addReaction("\u0035\u20E3").queue();
				
				//Assigns the current message to a variable for later reference.
				promptMsg = m;
			}
			
			if (promptMsg.equals(null)) {
				P.print("[TicketAutoPrompter] Issue: Prompt message not found.");
				event.getChannel().sendMessage(Bot.VERSION + " encountered an error.").queue();
			}
		}
	}
}
