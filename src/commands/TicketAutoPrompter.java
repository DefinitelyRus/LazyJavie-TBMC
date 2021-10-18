package commands;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.exception.ExceptionUtils;

import home.Bot;
import home.P;
import home.SQLconnector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TicketAutoPrompter extends ListenerAdapter {
	
	private static String emoteCodePoint = null;
	private final static String cp1 = "U+31U+20e3";
	private final static String cp2 = "U+32U+20e3";
	private final static String cp3 = "U+33U+20e3";
	private final static String cp4 = "U+34U+20e3";
	private final static String cp5 = "U+35U+20e3";
	private final static String cpCheck = "U+2705";
	static String coords = null;
	
	public void onTextChannelCreate (TextChannelCreateEvent event) {
		String chName = event.getChannel().getName();
		
		//A toggle-able switch that enables/disables this entire program.
		if (!Bot.ticketsEnabled) {P.print("\n[TicketAutoPrompter] (Ignore) Ticket created; response disabled."); return;}
		
		if (chName.matches("ticket-[0-9]{4}")) {
			TextChannel channel = event.getChannel();
			EmbedBuilder embed = new EmbedBuilder();
			embed.setColor(TAPFunctions.color);
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
			
			P.print("\n[TicketAutoPrompter] Detected new ticket!");
			P.print("|Sending root prompt...");
			channel.sendMessage(msgEmbed).queue();
			
			//Waits for local cache to refresh.
			P.print("|Refreshing cache...");
			try {TimeUnit.MILLISECONDS.sleep(400);} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
			List<Message> msgs = Bot.jda.getTextChannelById(channel.getId()).getHistory().retrievePast(1).complete();
			Message promptMsg = null;
			
			for (Message m : msgs) {
				//TODO Add a check to make sure the sender is the bot.
				m.getEmbeds().forEach((x) -> P.print(x.getAuthor().getName()));
				try {
					//Application queries
					m.addReaction(TAPFunctions.utf1).queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//Lost items
					m.addReaction(TAPFunctions.utf2).queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//Griefing reports
					m.addReaction(TAPFunctions.utf3).queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//Joining issues
					m.addReaction(TAPFunctions.utf4).queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//Others
					m.addReaction(TAPFunctions.utf5).queue();
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
				
				//Assigns the current message to a variable for later reference.
				promptMsg = m;
			}
			
			if (promptMsg.equals(null)) {
				P.print("Issue: Prompt message not found.");
				event.getChannel().sendMessage("[TicketAutoPrompter] Encountered an unexpected error.").queue();
				return;
			}
			P.print("Ticket Auto-response is ready to use in " + chName + "!");
		}
	}
	
	//Implement a non-binary tree to allow for modular and easily traversable data structure.
	//This current one is awful to navigate through.
	//Update: I made it easier to navigate but still prefer to work with a tree data structure.
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		
		//Cancels if it detects itself.
		if (event.getUserId().equals(Bot.jda.getSelfUser().getId())) return;
		//if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) return;
		
		String channelName = event.getChannel().getName();
		String channelId = event.getChannel().getId();
		
		//Console print if name matches, cancel if not.
		if (channelName.matches("ticket-[0-9]{4}")) {P.print("\n[TicketAutoPrompter] Deploying ticket auto-responder to " + channelName + "...");}
		else return;

		P.print("|Retrieving history...");
		List<Message> msgHistory = Bot.jda.getTextChannelById(channelId).getHistory().retrievePast(100).complete();
		for (Message m : msgHistory) {
			
			//Skips the current iteration if the message is from the bot's self.
			boolean isSelf = m.getMember().getId().equals(Bot.jda.getSelfUser().getId());
			if (!isSelf) continue;
			
			//Initialization
			P.print("|Looking for coordinates...");
			emoteCodePoint = event.getReactionEmote().getAsCodepoints();
			List<MessageEmbed> embeds = m.getEmbeds();
			for (MessageEmbed MeEm : embeds) {coords = MeEm.getAuthor().getName().replace("TetrabearMC ", "");}
			//Yes I went with that name (MeEm) just because it's funny.
			
			/*
			 * INSTRUCTIONS:
			 * For those reading through this mess of a workaround, good luck, but here's some instructions to help you.
			 * 
			 * [@x] refers to the number of steps the member has already traversed through.
			 * x is the path the member has taken, this is for every path resulting in different outputs.
			 * x-y is a path inside x. x-y-z is a path inside y, which is inside x.
			 */
			
			if (coords.equals("<@0>")) {P.delete(m); rootPath(event);}
			else if (coords.startsWith("<@1")) {pathNewJoin(event, m);}
			else if (coords.startsWith("<@2")) {pathLostItem(event, m);}
			else if (coords.startsWith("<@3")) {pathGriefTheft(event, m);}
			else if (coords.startsWith("<@4")) {pathCantConnect(event, m);}
			else if (coords.startsWith("<@5")) {pathUnspecificQuery(event, m);}
		}
	}
	
	//TODO Add JavaDoc
	private static void rootPath(GenericGuildMessageEvent event) {
		switch (emoteCodePoint) {
		
		//To [@1] Path newJoin
		case cp1:
			TAPFunctions.newJoin(event);
			break;
			
		//To [@2] Path lostItem
		case cp2:
			TAPFunctions.lostItem(event);
			break;
		
		//To [@3] Path griefTheft
		case cp3:
			TAPFunctions.griefTheft(event);
			break;
			
		//To [@4] Path cantConnect
		case cp4:
			TAPFunctions.cantConnect(event);
			break;
			
		//To [@5] Path unspecificQuery
		//END
		case cp5:
			TAPFunctions.unspecificQuery(event);
			break;
		}
	}
	
	private static void pathNewJoin(GuildMessageReactionAddEvent event, Message m) {
		
		//From [@1] Path newJoin				--------------------------------------------------
		if (coords.equals("<@1>")) {
			switch (emoteCodePoint) {
			
			//To [@1-1] Path nonPremiun
			case cp1:
				P.delete(m);
				TAPFunctions.nonPremium(event);
				break;
				
			//To [@1-2] TODO Path badIp
			case cp2:
				P.delete(m);
				TAPFunctions.badIp(event);
				break;
			
			//To [@1-3] TODO Path discordNotLinked
			case cp3:
				P.delete(m);
				TAPFunctions.discordNotLinked(event);
				break;
				
			//To [@1-4] Path otherBadConn
			//END
			case cp4:
				P.delete(m);
				TAPFunctions.unspecificQuery(event);
				break;
			}
		}
		
		//From [@1-1...]
		else if (coords.startsWith("<@1-1")) {
			
			//From [@1-1] Path nonPremium		--------------------------------------------------
			if (coords.equals("<@1-1>")) {
				switch (emoteCodePoint) {
				
				//To [@1-1-1] Path needWhitelist		-------------------------
				case cp1:
					P.delete(m);
					TAPFunctions.needWhitelist(event);
					return;
				
				//To [@1-1-2] Path allDone1				-------------------------
				//END
				case cp2:
					P.delete(m);
					P.send(event, "All good then! You may now close the ticket.");
					return;
				
				//To [@1-1-3] Path otherCantJoin		-------------------------
				//END
				case cp3:
					P.delete(m);
					TAPFunctions.unspecificQuery(event);
					return;
				}
			}
			
			//From [@1-1-1] Path needWhitelist	-------------------------
			//END
			else if (coords.equals("<@1-1-1>")) {
				if (emoteCodePoint.equals(cpCheck)) {
					P.delete(m);
					P.send(event, "The " + Bot.modRoleAsTag + " will be assisting you as soon as possible. Please remain patient.");
					return;
				}
			}
		}
		
		//From [@1-2...]
		else if (coords.startsWith("<@1-2")) {

			//From [@1-2] Path badIp				--------------------------------------------------
			//TODO Finish [@1-2-2]
			if (coords.equals("<@1-2>")) {
				switch (emoteCodePoint) {
				
				//To [@1-2-1] Path allDone2
				//END
				case cp1:
					P.delete(m);
					P.send(event, "All good then! You may now close the ticket.");
					return;
				
				//To [@1-2-2] Path clientIssue
				//TODO
				case cp2:
					P.delete(m);
					P.print("Developer Note: This is temporary. This should perform another function but this should serve as a temporary placeholder.");
					TAPFunctions.unspecificQuery(event);
					//TAPFunctions.clientIssue(event);
					return;
					
				//To [@1-2-3] Path otherError
				case cp3:
					P.delete(m);
					TAPFunctions.otherError(event);
					return;
				}
			}
		}
		
		//From [@1-3...]
		else if (coords.startsWith("<@1-3")) {

			//From [@1-3] Path discordNotLinked	--------------------------------------------------
			//TODO Finish all [@1-3...] paths.
			if (coords.equals("<@1-3>")) {
				P.delete(m);
				P.print("Developer Note: This is temporary. This should perform another function but this should serve as a temporary placeholder.");
				TAPFunctions.unspecificQuery(event); return;
				/*
				switch (emoteCodePoint) {
				
				//!!!! Everything below this line in this block is unreachable for now. !!!!
				//To [@1-3-1] Path followedInstructions
				case cp1:
					P.delete(m);
					TAPFunctions.followedInstructions(event);
					return;
				
				//To [@1-3-2] Path clientIssue (alias: [@1-2-2])
				case cp2:
					P.delete(m);
					TAPFunctions.clientIssue(event);
					return;
				
				//To [@1-3-3] botNotWorking
				case cp3:
					P.delete(m);
					TAPFunctions.botNotWorking(event);
					return;
				
				//To [@1-3-4] otherLinkFail
				case cp4:
					P.delete(m);
					TAPFunctions.otherLinkFail(event);
					return;
				}
				*/
			}
		}
		
		//From [@1-4...] TODO
		else if (coords.startsWith("<@1-4")) {

			//From [@1-4] Path otherBadConn		--------------------------------------------------
			if (coords.equals("<@1-4>")) {
				P.delete(m);
				TAPFunctions.otherBadConn(event);
			}
		}
	}

	private static void pathLostItem(GenericGuildMessageEvent event, Message m) {
		
		//From [@2] Path lostItem				--------------------------------------------------
		if (coords.equals("<@2>")) {
			P.print("Developer Note: This is temporary. This should perform another function but this should serve as a temporary placeholder.");
			P.delete(m);
			TAPFunctions.unspecificQuery(event);
		}
		
		//!!!! Everything below this line in this block is unreachable for now. !!!!
		//From [@2-1] Path deathLost				-------------------------
		//TODO
		else if (coords.startsWith("<@2-1")) {
			P.delete(m);
			//TAPFunctions.deathLost(event);
		}

		//From [@2-2] Path crashLost				-------------------------
		//TODO
		else if (coords.startsWith("<@2-2")) {
			P.delete(m);
			//TAPFunctions.crashLost(event);
		}

		//From [@2-3] Path theftLost				-------------------------
		//TODO
		else if (coords.startsWith("<@2-3")) {
			P.delete(m);
			//TAPFunctions.theftLost(event);
		}
		
		//From [@2-4] Path theftLost				-------------------------
		//TODO
		else if (coords.startsWith("<@2-4")) {
			P.delete(m);
			//TAPFunctions.unspecificQuery(event);
		}
	}

	private static void pathGriefTheft(GenericGuildMessageEvent event, Message m) {
		//From [@3] Path griefTheft				--------------------------------------------------
		if (coords.equals("<@3>")) {
			P.delete(m);
			P.print("Developer Note: This is temporary. This should perform another function but this should serve as a temporary placeholder.");
			TAPFunctions.unspecificQuery(event);
		}
	}

	private static void pathCantConnect(GenericGuildMessageEvent event, Message m) {
		//From [@4] cantConnect					--------------------------------------------------
		if (coords.equals("<@4>")) {
			P.delete(m);
			P.print("Developer Note: This is temporary. This should perform another function but this should serve as a temporary placeholder.");
			TAPFunctions.unspecificQuery(event);
		}
	}

	private static void pathUnspecificQuery(GenericGuildMessageEvent event, Message m) {
		if (coords.equals("<@5>") && emoteCodePoint.equals(cpCheck)) {
			P.delete(m);
			P.send(event, "The " + Bot.modRoleAsTag + " will respond to you soon! *Please remain patient, **__this may take a while__.***");
			return;
		}
	}
}