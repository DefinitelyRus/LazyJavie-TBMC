/*
 * 
 */
package commands;

import bot_init.LazyJavie;
import bot_init.SQLconnector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Returns extends ListenerAdapter{
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().split("\\s+");
		
		//BOT TOKEN. Returns the bot's token... not really.
		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "bottoken")) {
			event.getChannel().sendMessage("Bot token: ||Never gonna give you up~ Never gonna let you down~||").queue();
		}
		
		//PING. Returns the latency.
		else if (args[0].equalsIgnoreCase(LazyJavie.prefix + "ping")) {
			P.print("\nRequesting ping: " + event.getMember().getUser());
			long ping = event.getJDA().getGatewayPing();
			P.print("Latency gathered.");
			event.getChannel().sendMessage(ping + "ms").queue();
			P.print("Ping: " + ping + "ms");
		}
		
		//TEST. Just returns a confirmation message to see if the bot works.
		else if (args[0].equalsIgnoreCase(LazyJavie.prefix + "test")) {
			event.getChannel().sendMessage("Pong!").queue();
			P.print("[Returns] Pong! Sender: " + event.getMember());
		}
		
		//POINTS. Displays the points of the current user.
		if (args[0].equalsIgnoreCase(LazyJavie.prefix + "points") || args[0].equalsIgnoreCase(LazyJavie.prefix + "point")) {
			P.print("\n[Returns] Requesting point query: " + event.getMember().getUser());
			
			//Initialization
			String memberName = event.getMember().getUser().getName();
			String memberId = event.getMessage().getMember().getId();
			P.print("Getting query...");
			Integer pts = Integer.parseInt(SQLconnector.get("select points from lazyjavie.members WHERE userid=" + memberId + ";", "points"));
			
			P.print("Displaying points...");
			//Displaying points as embed.
			EmbedBuilder points = new EmbedBuilder();
			points.setColor(0xffae00);
			points.setTitle("💲 Your current points: 💲");
			points.setDescription("" + pts  + "");
			points.setFooter("Requested by " + memberName , event.getMember().getUser().getAvatarUrl());
			event.getChannel().sendMessage(points.build()).queue();
		}
	}
}
