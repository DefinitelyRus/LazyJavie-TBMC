package commands;

import java.util.Hashtable;
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
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TicketAutoPrompter extends ListenerAdapter {
	public void onTextChannelCreate (TextChannelCreateEvent event) {
		String chName = event.getChannel().getName();
		P.print("New Channel Detected: " + chName);
		
		if (chName.matches("ticket-[0-9]{4}")) {
			TextChannel channel = event.getChannel();
			EmbedBuilder embed = new EmbedBuilder();
			embed.setColor(0xFF8822);
			embed.setTitle("Need help? ");
			embed.setDescription("\nPress :one: if you're having trouble **joining for the first time**." +
								"\n\nPress :two: if you **lost items** due to a **crash or glitch**." +
								"\n\nPress :three: if you've been **griefed or stolen from**." +
								"\n\nPress :four: if you **can't join** the server." +
								"\n\nPress :five: if it's **NONE** of the above.");
			embed.setFooter("DO NOT SKIP THIS PROMPT. This will help us gather information to fix your issue.\n" +
								"If the emotes don't appear automatically, you can still add them yourself or skip this process entirely.");
			embed.setAuthor("TetrabearMC");
			MessageEmbed msgEmbed = embed.build();
			
			P.print("Detected new ticket!");
			
			channel.sendMessage(msgEmbed).queue();
			
			try {TimeUnit.MILLISECONDS.sleep(400);} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
			
			List<Message> msgs = Bot.jda.getTextChannelById(channel.getId()).getHistory().retrievePast(1).complete();
			Message promptMsg = null;
			
			for (Message m : msgs) {
				P.print(String.valueOf(m.getEmbeds().size()));
				
				try {
					//Application queries
					m.addReaction("\u0031\u20E3").queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//Lost items
					m.addReaction("\u0032\u20E3").queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//Griefing reports
					m.addReaction("\u0033\u20E3").queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//Joining issues
					m.addReaction("\u0034\u20E3").queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//Others
					m.addReaction("\u0035\u20E3").queue();
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
				
				//Assigns the current message to a variable for later reference.
				promptMsg = m;
			}
			
			if (promptMsg.equals(null)) {
				P.print("[TicketAutoPrompter] Issue: Prompt message not found.");
				event.getChannel().sendMessage("[TicketAutoPrompter] Encountered an unexpected error.").queue();
				return;
			}
			
			promptMsg.getId();
		}
	}
	
	//TODO Implement a non-binary tree to allow for modular and easily traversable data structure.
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		if (event.getUserId().equals(Bot.jda.getSelfUser().getId())) {P.print("[TicketAutoPrompter] Self-report! Cancelling..."); return;}
		String channelName = event.getChannel().getName();
		String channelId = event.getChannel().getId();
		String optionId = null;
		int count = 0;

		final String opt0 = "0";
		final String opt1 = "1";
		final String opt2 = "2";
		final String opt3 = "3";
		final String opt4 = "4";
		final String opt5 = "5";
		final String opt6 = "6";
		final String opt7 = "7";
		final String opt8 = "8";
		final String opt9 = "9";
		final String optX = "XXXXXXXXXX";
		
		boolean isSelf = false;
		Bot.jda.getTextChannelById(event.getChannel().getId()).getHistory().retrievePast(100).complete();
		
		Hashtable<String,String> ticketProgress = new Hashtable<String,String>();
		
		Hashtable<String,String> ticketInfo = Bot.ticketDictionary;
		if (channelName.matches("ticket-[0-9]{4}")) {
			P.print("Codepoint: " + event.getReactionEmote().getAsCodepoints());
			
			switch (event.getReactionEmote().getAsCodepoints()) {
				case "U+30U+20e3":
					ticketInfo.put("layer" + count + "-" + channelId, opt0);
					break;
					
				case "U+31U+20e3":
					ticketInfo.put("layer" + count + "-" + channelId, opt1);
					break;
	
				case "U+32U+20e3":
					ticketInfo.put("layer" + count + "-" + channelId, opt2);
					break;
					
				case "U+33U+20e3":
					ticketInfo.put("layer" + count + "-" + channelId, opt3);
					break;
					
				case "U+34U+20e3":
					ticketInfo.put("layer" + count + "-" + channelId, opt4);
					break;
					
				case "U+35U+20e3":
					ticketInfo.put("layer" + count + "-" + channelId, opt5);
					break;

				case "U+36U+20e3":
					ticketInfo.put("layer" + count + "-" + channelId, opt6);
					break;
	
				case "U+37U+20e3":
					ticketInfo.put("layer" + count + "-" + channelId, opt7);
					break;
					
				case "U+38U+20e3":
					ticketInfo.put("layer" + count + "-" + channelId, opt8);
					break;
					
				case "U+39U+20e3":
					ticketInfo.put("layer" + count + "-" + channelId, opt9);
					break;
			}
		}
	}
}
