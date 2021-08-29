package commands;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.exception.ExceptionUtils;

import home.Bot;
import home.P;
import home.SQLconnector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
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
			embed.setTitle("Need help?");
			embed.setDescription("\nPress :one: if you're having trouble **joining for the first time**." +
								"\n\nPress :two: if you **lost items** due to a **crash or glitch**." +
								"\n\nPress :three: if you've been **griefed or stolen from**." +
								"\n\nPress :four: if you **can't join** the server." +
								"\n\nPress :five: if it's **NONE** of the above.");
			embed.setFooter("DO NOT SKIP THIS PROMPT. This will help us gather information to fix your issue.\n" +
								"If the emotes don't appear automatically, you can still add them yourself or skip this process entirely.");
			embed.setAuthor("TetrabearMC <@0>");
			MessageEmbed msgEmbed = embed.build();
			
			P.print("Detected new ticket!");
			//P.print(msgEmbed.);]
			channel.sendMessage(msgEmbed).queue();
			
			try {TimeUnit.MILLISECONDS.sleep(400);} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
			
			List<Message> msgs = Bot.jda.getTextChannelById(channel.getId()).getHistory().retrievePast(1).complete();
			Message promptMsg = null;
			
			for (Message m : msgs) {
				m.getEmbeds().forEach((x) -> P.print(x.getAuthor().getName()));
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
	@SuppressWarnings("unused")
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		if (event.getUserId().equals(Bot.jda.getSelfUser().getId())) {return;}
		if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) return;
		
		String channelName = event.getChannel().getName();
		String channelId = event.getChannel().getId();
		String userId = event.getMember().getId();
		int count = 0;

		final int opt0 = 0;
		final int opt1 = 1;
		final int opt2 = 2;
		final int opt3 = 3;
		final int opt4 = 4;
		final int opt5 = 5;
		final int opt6 = 6;
		final int opt7 = 7;
		final int opt8 = 8;
		final int opt9 = 9;
		final String optX = "XXXXXXXXXX";
		final String zero = "U+30U+20e3";
		final String one = "U+31U+20e3";
		final String two = "U+32U+20e3";
		final String three = "U+33U+20e3";
		final String four = "U+34U+20e3";
		final String five = "U+35U+20e3";
		final String six = "U+36U+20e3";
		final String seven = "U+37U+20e3";
		final String eight = "U+38U+20e3";
		final String nine = "U+39U+20e3";
		
		List<Message> msgHistory = Bot.jda.getTextChannelById(channelId).getHistory().retrievePast(100).complete();
		for (Message m : msgHistory) {
			boolean isSelf = m.getMember().getId().equals(Bot.jda.getSelfUser().getId());
			boolean isTargetEmbed = false;
			List<MessageEmbed> embeds = m.getEmbeds();
			for (MessageEmbed MeEm : embeds) {isTargetEmbed = MeEm.getAuthor().getName().equals("TetrabearMC <@0>");}
			//Yes I went with that name (MeEm) just because it's funny.
			
			if (isSelf && isTargetEmbed) {
				switch (event.getReactionEmote().getAsCodepoints()) {
				case one:
					break;
				case two:
					break;
				case three:
					break;
				case four:
					break;
				case five:
					unspecificQuery(event);
					break;
				}
			}
		}
		
		
		
		if (channelName.matches("ticket-[0-9]{4}")) {
			P.print("[TicketAutoPrompter] Deploying ticket auto-responder to " + channelName + "...");
			
		}
	}
	
	private static void unspecificQuery(GuildMessageReactionAddEvent event) {
		String desc = "Tell us about your issue and we'll talk about it as soon as we can.\n" +
				"Provide detailed information such as... \n\nWHAT: \nWHEN: \nWHO: \nWHERE: \nWHY: \nHOW: \n\n(Put \"N/A\" if you're unsure or if it's unapplicable.)\n";
		String footer = "PLEASE BE PATIENT! DO NOT @mention unless absolutely necessary.";
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(0xFF8822);
		embed.setTitle("What's up?");
		embed.setDescription(desc);
		embed.setFooter(footer);
		embed.setAuthor("TetrabearMC <@5>");
		P.send(event, embed.build());
	}
}
