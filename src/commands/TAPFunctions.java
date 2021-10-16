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
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public class TAPFunctions {
	final static int color = 0xFF8822;
	final static String utf1 = "\u0031\u20E3";
	final static String utf2 = "\u0032\u20E3";
	final static String utf3 = "\u0033\u20E3";
	final static String utf4 = "\u0034\u20E3";
	final static String utf5 = "\u0035\u20E3";
	final static String utfCheck = "\u2705";
	
	
	//[@1] newJoin								---------------------------------------------------------------------------
	protected static void newJoin(GenericGuildMessageEvent event) {
		String desc = "**What does it say when you try to join?**\n\n" + 
					"Press :one: if `Only premium Minecraft accounts are allowed to join the server.`\n\n" +
					"Press :two: if `io.netty.channel.abstractchannel$annotatedconnectexception`\n\n" +
					"Press :three: if **you can join** but you **can't move** in-game.\n\n" +
					"Press :four: if it's **none of the above**.\n";
		String footer = "PLEASE BE PATIENT! DO NOT @mention unless absolutely necessary.";
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(color);
		embed.setTitle("So you're having trouble joining **for the first time**...");
		embed.setDescription(desc);
		embed.setFooter(footer);
		embed.setAuthor("TetrabearMC <@1>");
		P.send(event, embed.build());
		
		try {TimeUnit.MILLISECONDS.sleep(500);} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
		
		List<Message> history = event.getChannel().getHistory().retrievePast(100).complete();
		
		for (Message m : history) {
			String coords = null;
			
			//The NULL check is necessary because lists always contain 1 null value in addition to its other contents.
			for (MessageEmbed em : m.getEmbeds()) {if (em.getAuthor() != null) coords = em.getAuthor().getName().replace("TetrabearMC ", "");}
			
			if (m.getMember().getId().equals(Bot.jda.getSelfUser().getId()) && coords.equals("<@1>")) {
				try {
					//[@1-1] nonPremium
					m.addReaction(utf1).queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//[@1-2] badIp
					m.addReaction(utf2).queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//[@1-3] discordNotLinked
					m.addReaction(utf3).queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//[@1-4] otherBadConn
					m.addReaction(utf4).queue();
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
			}
		}
	}
	
	//[@1-1] nonPremium							--------------------------------------------------
	protected static void nonPremium(GuildMessageReactionAddEvent event) {
		boolean isMember = false;
		List<Role> memRoles = event.getMember().getRoles();
		for (Role r : memRoles) {if (r.getName().equalsIgnoreCase("Member")) isMember = true;}
		
		String desc = "**Do the steps in #how-to-join first!**\n\n" + 
				"Press :one: if you're playing on a **cracked account**.\n\n" +
				"Press :two: if you're no longer have this issue.\n\n" +
				"Press :three: if you're **none** of the above.\n";
		String footer = "PLEASE BE PATIENT! DO NOT @mention unless absolutely necessary.";
		
		if (!isMember) desc = desc + "\n**IMPORTANT:**\nDo step 1 in #how-to-join first. This will save you plenty of trouble later on.\n";
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(color);
		embed.setTitle("Are you playing on a cracked account?");
		embed.setDescription(desc);
		embed.setFooter(footer);
		embed.setAuthor("TetrabearMC <@1-1>");
		P.send(event, embed.build());
		
		try {TimeUnit.MILLISECONDS.sleep(400);} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
		
		List<Message> history = event.getChannel().getHistory().retrievePast(50).complete();
		
		for (Message m : history) {
			String coords = null;
			
			//The NULL check is necessary because lists always contain 1 null value in addition to its other contents.
			for (MessageEmbed em : m.getEmbeds()) {if (em.getAuthor() != null) coords = em.getAuthor().getName().replace("TetrabearMC ", "");}
			
			if (m.getMember().getId().equals(Bot.jda.getSelfUser().getId()) && coords.equals("<@1-1>")) {
				try {
					//[@1-1-1] needWhitelist
					m.addReaction(utf1).queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//[@1-1-2] allDone1-1
					m.addReaction(utf2).queue();
					TimeUnit.MILLISECONDS.sleep(100);

					//[@1-1-3] otherCantJoin
					m.addReaction(utf3).queue();
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
			}
		}
	}
	
	//[@1-1-1] needWhitelist
	protected static void needWhitelist(GenericGuildMessageEvent event) {
		String desc = "**Please send us these details:**\n\n" +
					"- Your in-game username: \n\n" +
					"- DiscordTag#XXXX of who invited you: \n\n" +
					"Press :white_check_mark: when you're done!\n";
		String footer = "If this is an ALT account, please include your main account's DiscordTag#XXXX as well.";
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(color);
		embed.setTitle("You need to be whitelisted!");
		embed.setDescription(desc);
		embed.setFooter(footer);
		embed.setAuthor("TetrabearMC <@1-1-1>");
		P.send(event, embed.build());

		try {TimeUnit.MILLISECONDS.sleep(400);} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
		
		List<Message> history = event.getChannel().getHistory().retrievePast(100).complete();
		
		for (Message m : history) {
			String coords = null;
			
			//The NULL check is necessary because lists always contain 1 null value in addition to its other contents.
			for (MessageEmbed em : m.getEmbeds()) {if (em.getAuthor() != null) coords = em.getAuthor().getName().replace("TetrabearMC ", "");}
			
			if (m.getMember().getId().equals(Bot.jda.getSelfUser().getId()) && coords.equals("<@1-1-1>")) {
				try {
					//[@1-1-1 Done]
					m.addReaction(utfCheck).queue();
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
			}
		}
	}

	//[@1-2] badIp
	protected static void badIp(GenericGuildMessageEvent event) {
		String desc = "`tetrabear.leanghosting.com` or `play.tetrabear.xyz:32115`\n\n" + 
				"Press :one: if you are now **able to join**.\n\n" +
				"Press :two: if you are **still unable to join**.\n\n" +
				"Press :three: if it **shows a different error code**.\n";
		String footer = "PLEASE BE PATIENT! DO NOT @mention unless absolutely necessary.";
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(color);
		embed.setTitle("Try these IP addresses!");
		embed.setDescription(desc);
		embed.setFooter(footer);
		embed.setAuthor("TetrabearMC <@1-2>");
		P.send(event, embed.build());
		
		try {TimeUnit.MILLISECONDS.sleep(400);} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
		
		List<Message> history = event.getChannel().getHistory().retrievePast(100).complete();
		
		for (Message m : history) {
			String coords = null;
			for (MessageEmbed em : m.getEmbeds()) {coords = em.getAuthor().getName().replace("TetrabearMC ", "");}
			
			if (m.getMember().getId().equals(Bot.jda.getSelfUser().getId()) && coords.equals("<@1-1>")) {
				try {
					//[@1-2-1] allDone1-2
					m.addReaction(utf1).queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//[@1-2-2] clientIssue
					m.addReaction(utf2).queue();
					TimeUnit.MILLISECONDS.sleep(100);

					//[@1-2-3] otherError
					m.addReaction(utf3).queue();
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
			}
		}
	}
	
	//[@1-2-2] clientIssue
	//TODO Collect a list of potential paths then add them here.
	protected static void clientIssue(GenericGuildMessageEvent event) {
		String desc = "`tetrabear.leanghosting.com` or `play.tetrabear.xyz:32115`\n\n" + 
				"Press :one: if you are now **able to join**.\n\n" +
				"Press :two: if you are **still unable to join**.\n\n" +
				"Press :three: if it **shows a different error code**.\n";
		String footer = "PLEASE BE PATIENT! DO NOT @mention unless absolutely necessary.";
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(color);
		embed.setTitle("Try these IP addresses!");
		embed.setDescription(desc);
		embed.setFooter(footer);
		embed.setAuthor("TetrabearMC <@1-2>");
		P.send(event, embed.build());
		
		try {TimeUnit.MILLISECONDS.sleep(400);} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
		
		List<Message> history = event.getChannel().getHistory().retrievePast(100).complete();
		
		for (Message m : history) {
			String coords = null;
			for (MessageEmbed em : m.getEmbeds()) {coords = em.getAuthor().getName().replace("TetrabearMC ", "");}
			
			if (m.getMember().getId().equals(Bot.jda.getSelfUser().getId()) && coords.equals("<@1-1>")) {
				try {
					//[@1-2-1] allDone1-2
					m.addReaction(utf1).queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//[@1-2-2] clientIssue
					m.addReaction(utf2).queue();
					TimeUnit.MILLISECONDS.sleep(100);

					//[@1-2-3] otherError
					m.addReaction(utf3).queue();
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
			}
		}
	}
	
	//[@1-2-3] otherError
	//TODO A modified unspecificQuery() to ask for more technical details.
	protected static void otherError(GenericGuildMessageEvent event) {
		String desc = "`tetrabear.leanghosting.com` or `play.tetrabear.xyz:32115`\n\n" + 
				"Press :one: if you are now **able to join**.\n\n" +
				"Press :two: if you are **still unable to join**.\n\n" +
				"Press :three: if it **shows a different error code**.\n";
		String footer = "PLEASE BE PATIENT! DO NOT @mention unless absolutely necessary.";
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(color);
		embed.setTitle("Try these IP addresses!");
		embed.setDescription(desc);
		embed.setFooter(footer);
		embed.setAuthor("TetrabearMC <@1-2>");
		P.send(event, embed.build());
		
		try {TimeUnit.MILLISECONDS.sleep(400);} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
		
		List<Message> history = event.getChannel().getHistory().retrievePast(100).complete();
		
		for (Message m : history) {
			String coords = null;
			for (MessageEmbed em : m.getEmbeds()) {coords = em.getAuthor().getName().replace("TetrabearMC ", "");}
			
			if (m.getMember().getId().equals(Bot.jda.getSelfUser().getId()) && coords.equals("<@1-1>")) {
				try {
					//[@1-2-1] allDone1-2
					m.addReaction(utf1).queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//[@1-2-2] clientIssue
					m.addReaction(utf2).queue();
					TimeUnit.MILLISECONDS.sleep(100);

					//[@1-2-3] otherError
					m.addReaction(utf3).queue();
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
			}
		}
	}
	
	//[1-3] discordNotLinked
	protected static void discordNotLinked(GuildMessageReactionAddEvent event) {
		boolean isMember = false;
		List<Role> memRoles = event.getMember().getRoles();
		for (Role r : memRoles) {if (r.getName().equalsIgnoreCase("Member")) isMember = true;}
		
		String desc = "**Do the steps in #how-to-join first!**\n\n" + 
				"Press :one: if you've **already followed the instructions** in #rules.\n\n" +
				"Press :two: if `/discord link` **command isn't working**.\n\n" +
				"Press :three: if you **sent @TetrabearMC** the 4-digit code, but **nothing happened**.\n\n" +
				"Press :four: if it's **NONE of the above**.";
		String footer = "PLEASE BE PATIENT! DO NOT @mention unless absolutely necessary.";
		
		if (!isMember) desc = desc + "\n**IMPORTANT:**\nDo step 1 in #how-to-join first. You **need** this in order to proceed.\n";
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(color);
		embed.setTitle("Are you playing on a cracked account?");
		embed.setDescription(desc);
		embed.setFooter(footer);
		embed.setAuthor("TetrabearMC <@1-1>");
		P.send(event, embed.build());
		
		try {TimeUnit.MILLISECONDS.sleep(400);} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
		
		List<Message> history = event.getChannel().getHistory().retrievePast(100).complete();
		
		for (Message m : history) {
			String coords = null;
			
			//The NULL check is necessary because lists always contain 1 null value in addition to its other contents.
			for (MessageEmbed em : m.getEmbeds()) {if (em.getAuthor() != null) coords = em.getAuthor().getName().replace("TetrabearMC ", "");}
			
			if (m.getMember().getId().equals(Bot.jda.getSelfUser().getId()) && coords.equals("<@1-1>")) {
				try {
					//[@1-3-1] followedInstructions
					m.addReaction(utf1).queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//[@1-3-2] commandNotWorking
					m.addReaction(utf2).queue();
					TimeUnit.MILLISECONDS.sleep(100);

					//[@1-3-3] botNotWorking
					m.addReaction(utf3).queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//[@1-3-4] otherLinkFail
					m.addReaction(utf4).queue();
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
			}
		}
	}
	
	//[@2] lostItem								---------------------------------------------------------------------------
	protected static void lostItem(GenericGuildMessageEvent event) {
		String desc = "\n" + 
				"Press :one: if you **died due to lag or a glitch**.\n\n" +
				"Press :two: if you lost items/blocks due to **lag, server crash, or a glitch.**\n\n" +
				"Press :three: if you think **you've been stolen from.**\n\n" +
				"Press :four: if it's **none of the above.**\n";
		String footer = "PLEASE BE PATIENT! DO NOT @mention unless absolutely necessary.";
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(color);
		embed.setTitle("How did you lose your items?");
		embed.setDescription(desc);
		embed.setFooter(footer);
		embed.setAuthor("TetrabearMC <@2>");
		P.send(event, embed.build());
		
		try {TimeUnit.MILLISECONDS.sleep(400);} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
		
		List<Message> history = event.getChannel().getHistory().retrievePast(100).complete();
		
		for (Message m : history) {
			String coords = null;
			for (MessageEmbed em : m.getEmbeds()) {coords = em.getAuthor().getName().replace("TetrabearMC ", "");}
			
			if (m.getMember().getId().equals(Bot.jda.getSelfUser().getId()) && coords.equals("<@2>")) {
				try {
					//[@2-1] deathLost
					m.addReaction(utf1).queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//[@2-2] crashLost
					m.addReaction(utf2).queue();
					TimeUnit.MILLISECONDS.sleep(100);

					//[@3-3] theftLost
					m.addReaction(utf3).queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//[@2-3] otherLost
					m.addReaction(utf4).queue();
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
			}
		}
	}

	//[@3] griefTheft							---------------------------------------------------------------------------
	protected static void griefTheft(GenericGuildMessageEvent event) {
		String desc = "\n" + 
				"Press :one: if you were **killed without consent**.\n\n" +
				"Press :two: if a **structure you built was damaged or altered** significantly without your permission.\n\n" +
				"Press :three: if you think **you've been stolen from.**\n\n" +
				"Press :four: if it's **none of the above.**\n";
		String footer = "PLEASE BE PATIENT! DO NOT @mention unless absolutely necessary.";
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(color);
		embed.setTitle("Describe how you were griefed.");
		embed.setDescription(desc);
		embed.setFooter(footer);
		embed.setAuthor("TetrabearMC <@3>");
		P.send(event, embed.build());
		
		try {TimeUnit.MILLISECONDS.sleep(400);} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
		
		List<Message> history = event.getChannel().getHistory().retrievePast(100).complete();
		
		for (Message m : history) {
			String coords = null;
			for (MessageEmbed em : m.getEmbeds()) {coords = em.getAuthor().getName().replace("TetrabearMC ", "");}
			
			if (m.getMember().getId().equals(Bot.jda.getSelfUser().getId()) && coords.equals("<@3>")) {
				try {
					//[@3-1] deathGrief
					m.addReaction(utf1).queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//[@3-2] structureGrief
					m.addReaction(utf2).queue();
					TimeUnit.MILLISECONDS.sleep(100);

					//[@3-3] theftLost
					m.addReaction(utf3).queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//[@3-4] otherGrief
					m.addReaction(utf4).queue();
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
			}
		}
		
	}

	//[@4] cantConnect							---------------------------------------------------------------------------
	protected static void cantConnect(GenericGuildMessageEvent event) {
		String desc = "**What does it say when you try to join?**\n\n" + 
				"Press :one: if `Only premium Minecraft accounts are allowed to join the server.`\n\n" +
				"Press :two: if `io.netty.channel.abstractchannel$annotatedconnectexception`\n\n" +
				"Press :three: if **you can join** but you **can't move** in-game.\n\n" +
				"Press :four: if you think the **server has crashed**.\n\n" +
				"Press :five: if it's **none of the above**.\n";
		String footer = "PLEASE BE PATIENT! DO NOT @mention unless absolutely necessary.";
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(color);
		embed.setTitle("Can't join?");
		embed.setDescription(desc);
		embed.setFooter(footer);
		embed.setAuthor("TetrabearMC <@1-1>");
		P.send(event, embed.build());
		
		try {TimeUnit.MILLISECONDS.sleep(400);} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
		
		List<Message> history = event.getChannel().getHistory().retrievePast(100).complete();
		
		for (Message m : history) {
			String coords = null;
			
			//The NULL check is necessary because lists always contain 1 null value in addition to its other contents.
			for (MessageEmbed em : m.getEmbeds()) {if (em.getAuthor() != null) coords = em.getAuthor().getName().replace("TetrabearMC ", "");}
			
			if (m.getMember().getId().equals(Bot.jda.getSelfUser().getId()) && coords.equals("<@1-1>")) {
				try {
					//[@1-1] nonPremium
					m.addReaction(utf1).queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//[@1-2] badIp
					m.addReaction(utf2).queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//[@1-3] discordNotLinked
					m.addReaction(utf3).queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//[@4-1] serverCrash
					m.addReaction(utf4).queue();
					TimeUnit.MILLISECONDS.sleep(100);
					
					//[@1-4] otherBadConn
					m.addReaction(utf5).queue();
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (Exception e) {SQLconnector.callError(e.toString(), ExceptionUtils.getStackTrace(e)); P.print(e.toString());}
			}
		}
				
	}
	
	//[@5] unspecifiedQuery						---------------------------------------------------------------------------
	protected static void unspecificQuery(GenericGuildMessageEvent event) {
		String desc = "Tell us about your issue and we'll talk about it as soon as we can.\n" +
				"Provide detailed information such as... \n" +
				"\nWHAT: \nWHEN: \nWHO: \nWHERE: \nWHY: \nHOW: \n\n(Put \"N/A\" if you're unsure or if it's unapplicable.)\n";
		String footer = "PLEASE BE PATIENT! DO NOT @mention unless absolutely necessary.";
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(color);
		embed.setTitle("What's up?");
		embed.setDescription(desc);
		embed.setFooter(footer);
		embed.setAuthor("TetrabearMC <@5>");
		P.send(event, embed.build());
	}
}
